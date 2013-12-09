package com.example.android.apis.content

import com.example.android.apis.R
import android.app.Activity
import android.os.Bundle
import org.scaloid.common._

//remove if not needed
import scala.collection.JavaConversions._

class ResourcesWidthAndHeight extends SActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.resources_width_and_height)
  }
}
