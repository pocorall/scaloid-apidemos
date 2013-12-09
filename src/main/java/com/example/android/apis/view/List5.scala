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

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

import android.app.ListActivity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.BaseAdapter
import android.widget.TextView

/**
 * A list view example with separators.
 */
class List5 extends ListActivity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setListAdapter(new MyListAdapter(this))
  }

  private var mStrings: Array[String] = Array("----------", "----------", "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "----------", "Abondance", "----------", "Ackawi", "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "----------", "Airedale", "Aisy Cendre", "----------", "Allgauer Emmentaler", "Alverca", "Ambert", "American Cheese", "Ami du Chambertin", "----------", "----------", "Anejo Enchilado", "Anneau du Vic-Bilh", "Anthoriro", "----------", "----------")

  private class MyListAdapter extends BaseAdapter {
    def this(context: Context) {
      this()
      mContext = context
    }

    def getCount: Int = {
      return mStrings.length
    }

    override def areAllItemsEnabled: Boolean = {
      return false
    }

    override def isEnabled(position: Int): Boolean = {
      return !mStrings(position).startsWith("-")
    }

    def getItem(position: Int): AnyRef = {
      return new Integer(position)
    }

    def getItemId(position: Int): Long = {
      return position.toLong
    }

    def getView(position: Int, convertView: View, parent: ViewGroup): View = {
      var tv: TextView = null
      if (convertView == null) {
        tv = LayoutInflater.from(mContext).inflate(android.R.layout.simple_expandable_list_item_1, parent, false).asInstanceOf[TextView]
      }
      else {
        tv = convertView.asInstanceOf[TextView]
      }
      tv.setText(mStrings(position))
      return tv
    }

    private var mContext: Context = null
  }

}