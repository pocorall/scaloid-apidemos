package com.example.android.apis

import android.app.ListActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.SimpleAdapter
import java.text.Collator
import java.util.ArrayList
import java.util.Collections
import java.util.Comparator
import java.util.HashMap
import java.util.List
import java.util.Map
import ApiDemos._
//remove if not needed
import scala.collection.JavaConversions._

object ApiDemos {

  private val sDisplayNameComparator = new Comparator[Map[String, Any]]() {

    private val collator = Collator.getInstance

    def compare(map1: Map[String, Any], map2: Map[String, Any]): Int = {
      collator.compare(map1.get("title"), map2.get("title"))
    }
  }
}

class ApiDemos extends ListActivity {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val intent = getIntent
    var path = intent.getStringExtra("com.example.android.apis.Path")
    if (path == null) {
      path = ""
    }
    setListAdapter(new SimpleAdapter(this, getData(path), android.R.layout.simple_list_item_1, Array("title"),
      Array(android.R.id.text1)))
    getListView.setTextFilterEnabled(true)
  }

  protected def getData(prefix: String): List[Map[String, Any]] = {
    val myData = new ArrayList[Map[String, Any]]()
    val mainIntent = new Intent(Intent.ACTION_MAIN, null)
    mainIntent.addCategory(Intent.CATEGORY_SAMPLE_CODE)
    val pm = getPackageManager
    val list = pm.queryIntentActivities(mainIntent, 0)
    if (null == list) return myData
    var prefixPath: Array[String] = null
    var prefixWithSlash = prefix
    if (prefix == "") {
      prefixPath = null
    } else {
      prefixPath = prefix.split("/")
      prefixWithSlash = prefix + "/"
    }
    val len = list.size
    val entries = new HashMap[String, Boolean]()
    for (i <- 0 until len) {
      val info = list.get(i)
      val labelSeq = info.loadLabel(pm)
      val label = if (labelSeq != null) labelSeq.toString else info.activityInfo.name
      if (prefixWithSlash.length == 0 || label.startsWith(prefixWithSlash)) {
        val labelPath = label.split("/")
        val nextLabel = if (prefixPath == null) labelPath(0) else labelPath(prefixPath.length)
        if ((if (prefixPath != null) prefixPath.length else 0) == labelPath.length - 1) {
          addItem(myData, nextLabel, activityIntent(info.activityInfo.applicationInfo.packageName, info.activityInfo.name))
        } else {
          if (entries.get(nextLabel) == null ) {
            addItem(myData, nextLabel, browseIntent(if (prefix == "") nextLabel else prefix + "/" + nextLabel))
            entries.put(nextLabel, true)
          }
        }
      }
    }
    Collections.sort(myData, sDisplayNameComparator)
    myData
  }

  protected def activityIntent(pkg: String, componentName: String): Intent = {
    val result = new Intent()
    result.setClassName(pkg, componentName)
    result
  }

  protected def browseIntent(path: String): Intent = {
    val result = new Intent()
    result.setClass(this, classOf[ApiDemos])
    result.putExtra("com.example.android.apis.Path", path)
    result
  }

  protected def addItem(data: List[Map[String, Any]], name: String, intent: Intent) {
    val temp = new HashMap[String, Any]()
    temp.put("title", name)
    temp.put("intent", intent)
    data.add(temp)
  }

  protected override def onListItemClick(l: ListView,
                                         v: View,
                                         position: Int,
                                         id: Long) {
    val map = l.getItemAtPosition(position).asInstanceOf[Map[String, Any]]
    val intent = map.get("intent").asInstanceOf[Intent]
    startActivity(intent)
  }
}
