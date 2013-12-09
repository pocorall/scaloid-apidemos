package com.example.android.apis.view

import com.example.android.apis.R
import android.app.Activity
import android.os.Bundle
//remove if not needed
import scala.collection.JavaConversions._

class GridLayout1 extends Activity {

  override protected def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.grid_layout_1)
  }
}
