package com.example.android.apis.view

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import java.util.List
import com.example.android.apis.R
//remove if not needed
import scala.collection.JavaConversions._

class Grid1 extends Activity {

  var mGrid: GridView = _

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    loadApps()
    setContentView(R.layout.grid_1)
    mGrid = findViewById(R.id.myGrid).asInstanceOf[GridView]
    mGrid.setAdapter(new AppsAdapter())
  }

  private var mApps: List[ResolveInfo] = _

  private def loadApps() {
    val mainIntent = new Intent(Intent.ACTION_MAIN, null)
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
    mApps = getPackageManager.queryIntentActivities(mainIntent, 0)
  }

  class AppsAdapter extends BaseAdapter {

    def getView(position: Int, convertView: View, parent: ViewGroup): View = {
      var i: ImageView = null
      if (convertView == null) {
        i = new ImageView(Grid1.this)
        i.setScaleType(ImageView.ScaleType.FIT_CENTER)
        i.setLayoutParams(new android.widget.AbsListView.LayoutParams(50, 50))
      } else {
        i = convertView.asInstanceOf[ImageView]
      }
      val info = mApps.get(position)
      i.setImageDrawable(info.activityInfo.loadIcon(getPackageManager))
      i
    }

    def getCount(): Int = mApps.size

    def getItem(position: Int): AnyRef = mApps.get(position)

    def getItemId(position: Int): Long = position
  }
}
