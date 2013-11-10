package com.example.android.apis.app

import com.example.android.apis.R
import android.app.Activity
import android.os.Bundle
import org.scaloid.common._


class HelloWorld extends SActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.hello_world)
  }
}
