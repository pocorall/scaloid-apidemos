package com.example.android.apis.view

import com.example.android.apis.R
import android.app.Activity
import android.content.ContentResolver
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract.Contacts
import android.widget.AutoCompleteTextView
//remove if not needed
import scala.collection.JavaConversions._

class AutoComplete5 extends Activity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.autocomplete_5)
    val content = getContentResolver
    val cursor = content.query(Contacts.CONTENT_URI, AutoComplete4.CONTACT_PROJECTION, null, null, null)
    val adapter = new AutoComplete4.ContactListAdapter(this, cursor)
    val textView = findViewById(R.id.edit).asInstanceOf[AutoCompleteTextView]
    textView.setAdapter(adapter)
  }
}
