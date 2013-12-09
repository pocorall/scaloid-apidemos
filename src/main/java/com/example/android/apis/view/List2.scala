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
import android.provider.ContactsContract.Contacts
import android.os.Bundle
import android.widget.ListAdapter
import android.widget.SimpleCursorAdapter

/**
 * A list view example where the
 * data comes from a cursor.
 */
class List2 extends ListActivity {
  // Scaloid >>
  val Contacts__ID = android.provider.BaseColumns._ID
  val Contacts_DISPLAY_NAME = "display_name" // android.provider.ContactsContract.ContactsColumns.DISPLAY_NAME
  // Scaloid <<

  private final val CONTACT_PROJECTION: Array[String] = Array[String](Contacts__ID, Contacts_DISPLAY_NAME)

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val c: Cursor = getContentResolver.query(Contacts.CONTENT_URI, CONTACT_PROJECTION, null, null, null)
    startManagingCursor(c)
    val adapter: ListAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c, Array[String](Contacts_DISPLAY_NAME), Array[Int](android.R.id.text1))
    setListAdapter(adapter)
  }
}