package com.example.android.apis.view

import com.example.android.apis.R
import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ContextMenu.ContextMenuInfo
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Gallery
import android.widget.ImageView
import android.widget.Toast
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.AdapterView.OnItemClickListener
//remove if not needed
import scala.collection.JavaConversions._

class Gallery1 extends Activity {
  private val ITEM_WIDTH = 136

  private val ITEM_HEIGHT = 88

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.gallery_1)
    val g = findViewById(R.id.gallery).asInstanceOf[Gallery]
    g.setAdapter(new ImageAdapter(this))
    g.setOnItemClickListener(new OnItemClickListener() {

      def onItemClick(parent: AdapterView[_],
                      v: View,
                      position: Int,
                      id: Long) {
        Toast.makeText(Gallery1.this, "" + position, Toast.LENGTH_SHORT)
          .show()
      }
    })
    registerForContextMenu(g)
  }

  override def onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo) {
    menu.add(R.string.gallery_2_text)
  }

  override def onContextItemSelected(item: MenuItem): Boolean = {
    val info = item.getMenuInfo.asInstanceOf[AdapterContextMenuInfo]
    Toast.makeText(this, "Longpress: " + info.position, Toast.LENGTH_SHORT)
      .show()
    true
  }

  class ImageAdapter(private val mContext: Context) extends BaseAdapter {

    val a = obtainStyledAttributes(R.styleable.Gallery1)

    private val mGalleryItemBackground = a.getResourceId(R.styleable.Gallery1_android_galleryItemBackground,
      0)

    private val mImageIds = Array(R.drawable.gallery_photo_1, R.drawable.gallery_photo_2, R.drawable.gallery_photo_3, R.drawable.gallery_photo_4, R.drawable.gallery_photo_5, R.drawable.gallery_photo_6, R.drawable.gallery_photo_7, R.drawable.gallery_photo_8)

    private val mDensity = mContext.getResources.getDisplayMetrics.density

    a.recycle()

    def getCount(): Int = mImageIds.length

    def getItem(position: Int): AnyRef = new Integer(position)

    def getItemId(position: Int): Long = position

    def getView(position: Int, convertView: View, parent: ViewGroup): View = {
      var imageView: ImageView = null
      var convertViewVar: View = convertView
      if (convertViewVar == null) {
        convertViewVar = new ImageView(mContext)
        imageView = convertViewVar.asInstanceOf[ImageView]
        imageView.setScaleType(ImageView.ScaleType.FIT_XY)
        imageView.setLayoutParams(new Gallery.LayoutParams((ITEM_WIDTH * mDensity + 0.5f).toInt, (ITEM_HEIGHT * mDensity + 0.5f).toInt))
        imageView.setBackgroundResource(mGalleryItemBackground)
      } else {
        imageView = convertViewVar.asInstanceOf[ImageView]
      }
      imageView.setImageResource(mImageIds(position))
      imageView
    }
  }
}
