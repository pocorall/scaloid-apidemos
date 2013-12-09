package com.example.android.apis.view

import android.app.Activity
import android.database.Cursor
import android.provider.ContactsContract.Contacts
import android.os.Bundle
import android.widget.Gallery
import android.widget.SimpleCursorAdapter
import android.widget.SpinnerAdapter
import com.example.android.apis.R
//remove if not needed
import scala.collection.JavaConversions._

class Gallery2 extends Activity {
  // Scaloid >>
  val Contacts__ID = android.provider.BaseColumns._ID
  val Contacts_DISPLAY_NAME = "display_name" //android.provider.ContactsContract.ContactsColumns.DISPLAY_NAME
  // Scaloid <<
  private val CONTACT_PROJECTION = Array(Contacts__ID, Contacts_DISPLAY_NAME)

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.gallery_2)
    val c = getContentResolver.query(Contacts.CONTENT_URI, CONTACT_PROJECTION, null, null, null)
    startManagingCursor(c)
    val adapter = new SimpleCursorAdapter(this, android.R.layout.simple_gallery_item, c, Array(Contacts_DISPLAY_NAME),
      Array(android.R.id.text1))
    val g = findViewById(R.id.gallery).asInstanceOf[Gallery]
    g.setAdapter(adapter)
  }
}
