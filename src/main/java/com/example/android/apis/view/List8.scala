/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.apis.view

import com.example.android.apis.R
import android.app.ListActivity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.AbsListView
import java.util.ArrayList

/**
 * A list view that demonstrates the use of setEmptyView. This example alos uses
 * a custom layout file that adds some extra buttons to the screen.
 */
class List8 extends ListActivity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.list_8)
    getListView.setEmptyView(findViewById(R.id.empty))
    mAdapter = new PhotoAdapter(this)
    setListAdapter(mAdapter)
    val clear: Button = findViewById(R.id.clear).asInstanceOf[Button]
    clear.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        mAdapter.clearPhotos
      }
    })
    val add: Button = findViewById(R.id.add).asInstanceOf[Button]
    add.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        mAdapter.addPhotos
      }
    })
  }

  private[view] var mAdapter: PhotoAdapter = null

  /**
   * A simple adapter which maintains an ArrayList of photo resource Ids.
   * Each photo is displayed as an image. This adapter supports clearing the
   * list of photos and adding a new photo.
   *
   */
  class PhotoAdapter extends BaseAdapter {
    def this(c: Context) {
      this()
      mContext = c
    }

    def getCount: Int = {
      return mPhotos.size
    }

    def getItem(position: Int): AnyRef = {
      return new Integer(position)
    }

    def getItemId(position: Int): Long = {
      return position.toLong
    }

    def getView(position: Int, convertView: View, parent: ViewGroup): View = {
      val i: ImageView = new ImageView(mContext)
      i.setImageResource(mPhotos.get(position))
      i.setAdjustViewBounds(true)
      i.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
      i.setBackgroundResource(R.drawable.picture_frame)
      return i
    }

    def clearPhotos {
      mPhotos.clear
      notifyDataSetChanged
    }

    def addPhotos {
      val whichPhoto: Int = Math.round(Math.random * (mPhotoPool.length - 1)).asInstanceOf[Int]
      val newPhoto: Int = mPhotoPool(whichPhoto)
      mPhotos.add(newPhoto)
      notifyDataSetChanged
    }

    private var mPhotoPool: Array[Integer] = Array(R.drawable.sample_thumb_0, R.drawable.sample_thumb_1, R.drawable.sample_thumb_2, R.drawable.sample_thumb_3, R.drawable.sample_thumb_4, R.drawable.sample_thumb_5, R.drawable.sample_thumb_6, R.drawable.sample_thumb_7)
    private var mPhotos: ArrayList[Integer] = new ArrayList[Integer]
    private var mContext: Context = null
  }

}