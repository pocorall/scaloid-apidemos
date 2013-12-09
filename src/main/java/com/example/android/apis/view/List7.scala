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

import com.example.android.apis.R
import android.app.ListActivity
import android.database.Cursor
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ListAdapter
import android.widget.SimpleCursorAdapter
import android.widget.TextView

/**
 * A list view example where the data comes from a cursor.
 */
class List7 extends ListActivity with OnItemSelectedListener {
  // Scaloid >>
  val Phone__ID = android.provider.BaseColumns._ID
  val Phone_TYPE = "data2" //android.provider.ContactsContract.CommonDataKinds.CommonColumns.TYPE
  val Phone_LABEL = "data3" //android.provider.ContactsContract.CommonDataKinds.CommonColumns.LABEL
  val Phone_TYPE_CUSTOM = android.provider.ContactsContract.CommonDataKinds.BaseTypes.TYPE_CUSTOM
  val Phone_DISPLAY_NAME = "display_name"; // android.provider.ContactsContract.ContactsColumns.DISPLAY_NAME
  // Scaloid <<

  private final val PHONE_PROJECTION: Array[String] = Array[String](Phone__ID, Phone_TYPE, Phone_LABEL, Phone.NUMBER, Phone_DISPLAY_NAME)
  private final val COLUMN_PHONE_TYPE: Int = 1
  private final val COLUMN_PHONE_LABEL: Int = 2
  private final val COLUMN_PHONE_NUMBER: Int = 3

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.list_7)
    mPhone = findViewById(R.id.phone).asInstanceOf[TextView]
    getListView.setOnItemSelectedListener(this)
    val c: Cursor = getContentResolver.query(Phone.CONTENT_URI, PHONE_PROJECTION, Phone.NUMBER + " NOT NULL", null, null)
    startManagingCursor(c)
    val adapter: ListAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c, Array[String](Phone_DISPLAY_NAME), Array[Int](android.R.id.text1))
    setListAdapter(adapter)
  }

  def onItemSelected(parent: AdapterView[_], v: View, position: Int, id: Long) {
    if (position >= 0) {
      val c: Cursor = parent.getItemAtPosition(position).asInstanceOf[Cursor]
      val `type`: Int = c.getInt(COLUMN_PHONE_TYPE)
      val phone: String = c.getString(COLUMN_PHONE_NUMBER)
      var label: String = null
      if (`type` == Phone_TYPE_CUSTOM) {
        label = c.getString(COLUMN_PHONE_LABEL)
      }
      val numberType: String = Phone.getTypeLabel(getResources, `type`, label).asInstanceOf[String]
      val text: String = numberType + ": " + phone
      mPhone.setText(text)
    }
  }

  def onNothingSelected(parent: AdapterView[_]) {
  }

  private var mPhone: TextView = null
}