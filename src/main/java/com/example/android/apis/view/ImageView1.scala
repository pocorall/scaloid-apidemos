package com.example.android.apis.view

import android.app.Activity
import android.os.Bundle
import com.example.android.apis.R
//remove if not needed
import scala.collection.JavaConversions._

class ImageView1 extends Activity {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.image_view_1)
  }
}
