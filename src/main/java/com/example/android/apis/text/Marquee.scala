package com.example.android.apis.text

import com.example.android.apis.R
import android.app.Activity
import android.os.Bundle
import org.scaloid.common.SActivity

//remove if not needed
import scala.collection.JavaConversions._

class Marquee extends SActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.marquee)
  }
}
