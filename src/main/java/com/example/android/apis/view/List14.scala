/*
 * Copyright (C) 2008 The Android Open Source Project
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

import android.app.ListActivity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.ImageView
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import com.example.android.apis.R

/**
 * Demonstrates how to write an efficient list adapter. The adapter used in this example binds
 * to an ImageView and to a TextView for each row in the list.
 *
 * To work efficiently the adapter implemented here uses two techniques:
 * - It reuses the convertView passed to getView() to avoid inflating View when it is not necessary
 * - It uses the ViewHolder pattern to avoid calling findViewById() when it is not necessary
 *
 * The ViewHolder pattern consists in storing a data structure in the tag of the view returned by
 * getView(). This data structures contains references to the views we want to bind data to, thus
 * avoiding calls to findViewById() every time getView() is invoked.
 */
object List14 {
  private final val DATA: Array[String] = Cheeses.sCheeseStrings

  private object EfficientAdapter {

    private[view] class ViewHolder {
      private[view] var text: TextView = null
      private[view] var icon: ImageView = null
    }

  }

  private class EfficientAdapter extends BaseAdapter {
    private var mInflater: LayoutInflater = null
    private var mIcon1: Bitmap = null
    private var mIcon2: Bitmap = null

    def this(context: Context) {
      this()
      mInflater = LayoutInflater.from(context)
      mIcon1 = BitmapFactory.decodeResource(context.getResources, R.drawable.icon48x48_1)
      mIcon2 = BitmapFactory.decodeResource(context.getResources, R.drawable.icon48x48_2)
    }

    /**
     * The number of items in the list is determined by the number of speeches
     * in our array.
     *
     * @see android.widget.ListAdapter#getCount()
     */
    def getCount: Int = {
      return DATA.length
    }

    /**
     * Since the data comes from an array, just returning the index is
     * sufficent to get at the data. If we were using a more complex data
     * structure, we would return whatever object represents one row in the
     * list.
     *
     * @see android.widget.ListAdapter#getItem(int)
     */
    def getItem(position: Int): AnyRef = {
      return new Integer(position)
    }

    /**
     * Use the array index as a unique id.
     *
     * @see android.widget.ListAdapter#getItemId(int)
     */
    def getItemId(position: Int): Long = {
      return position
    }

    /**
     * Make a view to hold each row.
     *
     * @see android.widget.ListAdapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */
    def getView(position: Int, convertView: View, parent: ViewGroup): View = {
      var holder: List14.EfficientAdapter.ViewHolder = null

      var convertViewVar: View = convertView

      if (convertViewVar == null) {
        convertViewVar = mInflater.inflate(R.layout.list_item_icon_text, null)
        holder = new List14.EfficientAdapter.ViewHolder
        holder.text = convertViewVar.findViewById(R.id.text).asInstanceOf[TextView]
        holder.icon = convertViewVar.findViewById(R.id.icon).asInstanceOf[ImageView]
        convertViewVar.setTag(holder)
      }
      else {
        holder = convertViewVar.getTag.asInstanceOf[List14.EfficientAdapter.ViewHolder]
      }
      holder.text.setText(DATA(position))
      holder.icon.setImageBitmap(if ((position & 1) == 1) mIcon1 else mIcon2)
      return convertViewVar
    }
  }

}

class List14 extends ListActivity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setListAdapter(new List14.EfficientAdapter(this))
  }
}