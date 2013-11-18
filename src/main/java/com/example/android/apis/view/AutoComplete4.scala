package com.example.android.apis.view

import com.example.android.apis.R
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.Contacts
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.CursorAdapter
import android.widget.FilterQueryProvider
import android.widget.Filterable
import android.widget.TextView
import AutoComplete4._
//remove if not needed
import scala.collection.JavaConversions._

object AutoComplete4 {
  // Scaloid >>
  val Contacts__ID = android.provider.BaseColumns._ID
  val Contacts_DISPLAY_NAME = "display_name" //android.provider.ContactsContract.ContactsColumns.DISPLAY_NAME
  // Scaloid <<

  class ContactListAdapter(context: Context, c: Cursor) extends CursorAdapter(context, c) with Filterable {

    override def newView(context: Context, cursor: Cursor, parent: ViewGroup): View = {
      val inflater = LayoutInflater.from(context)
      val view = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false).asInstanceOf[TextView]
      view.setText(cursor.getString(COLUMN_DISPLAY_NAME))
      view
    }

    override def bindView(view: View, context: Context, cursor: Cursor) {
      view.asInstanceOf[TextView].setText(cursor.getString(COLUMN_DISPLAY_NAME))
    }

    override def convertToString(cursor: Cursor): String = cursor.getString(COLUMN_DISPLAY_NAME)

    override def runQueryOnBackgroundThread(constraint: CharSequence): Cursor = {
      val filter = getFilterQueryProvider
      if (filter != null) {
        return filter.runQuery(constraint)
      }
      val uri = Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI, Uri.encode(constraint.toString))
      mContent.query(uri, CONTACT_PROJECTION, null, null, null)
    }

    private var mContent: ContentResolver = context.getContentResolver
  }

  val CONTACT_PROJECTION = Array(Contacts__ID, Contacts_DISPLAY_NAME)

  private val COLUMN_DISPLAY_NAME = 1
}

class AutoComplete4 extends Activity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.autocomplete_4)
    val content = getContentResolver
    val cursor = content.query(Contacts.CONTENT_URI, CONTACT_PROJECTION, null, null, null)
    val adapter = new ContactListAdapter(this, cursor)
    val textView = findViewById(R.id.edit).asInstanceOf[AutoCompleteTextView]
    textView.setAdapter(adapter)
  }
}
