package com.example.android.apis.view

import com.example.android.apis.R
import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
//remove if not needed
import scala.collection.JavaConversions._

class AutoComplete3 extends Activity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.autocomplete_3)
    val adapter = new ArrayAdapter[String](this, android.R.layout.simple_dropdown_item_1line, AutoComplete1.COUNTRIES)
    var textView = findViewById(R.id.edit).asInstanceOf[AutoCompleteTextView]
    textView.setAdapter(adapter)
    textView = findViewById(R.id.edit2).asInstanceOf[AutoCompleteTextView]
    textView.setAdapter(adapter)
  }
}
