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

import android.app.ListActivity
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.view.View
import android.widget.SimpleCursorAdapter
import android.widget.TextView

/**
 * A list view example where the
 * data comes from a cursor, and a
 * SimpleCursorListAdapter is used to map each item to a two-line
 * display.
 */
class List3 extends ListActivity {
  // Scaloid >>
  val Phone__ID = android.provider.BaseColumns._ID
  val Phone_TYPE = "data2" //android.provider.ContactsContract.CommonDataKinds.CommonColumns.TYPE
  val Phone_LABEL = "data3" //android.provider.ContactsContract.CommonDataKinds.CommonColumns.LABEL
  val Phone_TYPE_CUSTOM = android.provider.ContactsContract.CommonDataKinds.BaseTypes.TYPE_CUSTOM
  // Scaloid <<

  private final val PHONE_PROJECTION: Array[String] = Array[String](Phone__ID, Phone_TYPE, Phone_LABEL, Phone.NUMBER)
  private final val COLUMN_TYPE: Int = 1
  private final val COLUMN_LABEL: Int = 2

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val c: Cursor = getContentResolver.query(Phone.CONTENT_URI, PHONE_PROJECTION, null, null, null)
    startManagingCursor(c)
    val adapter: SimpleCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, c, Array[String](Phone_TYPE, Phone.NUMBER), Array[Int](android.R.id.text1, android.R.id.text2))
    adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder {
      def setViewValue(view: View, cursor: Cursor, columnIndex: Int): Boolean = {
        if (columnIndex != COLUMN_TYPE) {
          return false
        }
        val `type`: Int = cursor.getInt(COLUMN_TYPE)
        var label: String = null
        if (`type` == Phone_TYPE_CUSTOM) {
          label = cursor.getString(COLUMN_LABEL)
        }
        val text: String = Phone.getTypeLabel(getResources, `type`, label).asInstanceOf[String]
        (view.asInstanceOf[TextView]).setText(text)
        return true
      }
    })
    setListAdapter(adapter)
  }
}