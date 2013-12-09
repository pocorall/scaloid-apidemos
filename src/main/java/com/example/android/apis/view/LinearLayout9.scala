package com.example.android.apis.view

import com.example.android.apis.R
import android.app.Activity
import android.os.Bundle
import android.widget.ListView
import android.widget.ArrayAdapter
//remove if not needed
import scala.collection.JavaConversions._

class LinearLayout9 extends Activity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.linear_layout_9)
    val list = findViewById(R.id.list).asInstanceOf[ListView]
    list.setAdapter(new ArrayAdapter[String](this, android.R.layout.simple_list_item_1, AutoComplete1.COUNTRIES))
  }
}
