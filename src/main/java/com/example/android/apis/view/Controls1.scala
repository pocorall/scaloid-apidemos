package com.example.android.apis.view

import com.example.android.apis.R
import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import Controls1._
//remove if not needed
import scala.collection.JavaConversions._

object Controls1 {

  private val mStrings = Array("Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune")
}

class Controls1 extends Activity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.controls_1)
    val disabledButton = findViewById(R.id.button_disabled).asInstanceOf[Button]
    disabledButton.setEnabled(false)
    val s1 = findViewById(R.id.spinner1).asInstanceOf[Spinner]
    val adapter = new ArrayAdapter[String](this, android.R.layout.simple_spinner_item, mStrings)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    s1.setAdapter(adapter)
  }
}
