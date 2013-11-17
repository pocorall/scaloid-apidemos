package com.example.android.apis.content

import com.example.android.apis.R
import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import org.scaloid.common._

//remove if not needed
import scala.collection.JavaConversions._

class StyledText extends SActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.styled_text)
    val str = getText(R.string.styled_text)
    val tv = findViewById(R.id.text).asInstanceOf[TextView]
    tv.setText(str)
  }
}
