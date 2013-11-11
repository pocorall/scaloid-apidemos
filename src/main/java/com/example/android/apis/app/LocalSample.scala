package com.example.android.apis.app

import android.app.Activity
import android.content.ComponentName
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import com.example.android.apis.R
import org.scaloid.common._

class LocalSample extends SActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.local_sample)
    val button = findViewById(R.id.go).asInstanceOf[Button]
    button.setOnClickListener(mGoListener)
  }

  private var mGoListener: OnClickListener = new OnClickListener() {

    def onClick(v: View) {
      startInstrumentation(new ComponentName(LocalSample.this, classOf[LocalSampleInstrumentation]),
        null, null)
    }
  }
}
