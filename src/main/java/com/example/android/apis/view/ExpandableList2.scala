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

import android.app.ExpandableListActivity
import android.content.ContentUris
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.widget.SimpleCursorTreeAdapter
import net.pocorall.android.Workarounds._
import android.provider.ContactsContract.Contacts
import scala.concurrent.ops._
import org.scaloid.common._

/**
 * Demonstrates expandable lists backed by Cursors
 */
object ExpandableList2 {
  private final val CONTACTS_PROJECTION: Array[String] = Array(BaseColumns._ID, ContactsColumns.DISPLAY_NAME)
  private final val GROUP_ID_COLUMN_INDEX: Int = 0
  private final val PHONE_NUMBER_PROJECTION: Array[String] = Array(BaseColumns._ID, Phone.NUMBER)
}

import ExpandableList2._

class ExpandableList2 extends ExpandableListActivity with RunOnUiThread {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    mAdapter = new SimpleCursorTreeAdapter(this, null, android.R.layout.simple_expandable_list_item_1,
      Array(ContactsColumns.DISPLAY_NAME), Array(android.R.id.text1), android.R.layout.simple_expandable_list_item_1,
      Array(Phone.NUMBER), Array(android.R.id.text1)) {
      protected def getChildrenCursor(groupCursor: Cursor): Cursor = {
        val builder = Contacts.CONTENT_URI.buildUpon
        ContentUris.appendId(builder, groupCursor.getLong(GROUP_ID_COLUMN_INDEX))
        builder.appendEncodedPath(Contacts.Data.CONTENT_DIRECTORY)
        val phoneNumbersUri = builder.build

        spawn {
          val pos = groupCursor.getPosition
          val cursor = getContentResolver.query(phoneNumbersUri,
            PHONE_NUMBER_PROJECTION, DataColumns.MIMETYPE + "=?", Array(Phone.CONTENT_ITEM_TYPE), null)
          runOnUiThread(mAdapter.setChildrenCursor(pos, cursor))
        }

        null
      }
    }

    setListAdapter(mAdapter)

    spawn {
      val cursor = getContentResolver.query(Contacts.CONTENT_URI, CONTACTS_PROJECTION,
        ContactsColumns.HAS_PHONE_NUMBER + "=1", null, null)
      runOnUiThread(mAdapter.setGroupCursor(cursor))
    }
  }

  protected override def onDestroy() {
    super.onDestroy()
    // Null out the group cursor. This will cause the group cursor and all of the child cursors to be closed.
    mAdapter.changeCursor(null)
  }

  private var mAdapter: SimpleCursorTreeAdapter = null
}