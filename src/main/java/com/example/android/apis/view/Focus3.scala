package com.example.android.apis.view

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import com.example.android.apis.R
//remove if not needed
import scala.collection.JavaConversions._

class Focus3 extends Activity {

  private var mTopButton: Button = _

  private var mBottomButton: Button = _

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.focus_3)
    mTopButton = findViewById(R.id.top).asInstanceOf[Button]
    mBottomButton = findViewById(R.id.bottom).asInstanceOf[Button]
  }

  def getTopButton(): Button = mTopButton

  def getBottomButton(): Button = mBottomButton
}
