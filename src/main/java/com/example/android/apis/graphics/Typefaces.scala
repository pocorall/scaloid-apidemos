package com.example.android.apis.graphics

import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view.View
import Typefaces._
//remove if not needed
import scala.collection.JavaConversions._

object Typefaces {

  private class SampleView(context: Context) extends View(context) {

    private var mPaint: Paint = new Paint(Paint.ANTI_ALIAS_FLAG)

    private var mFace: Typeface = Typeface.createFromAsset(getContext.getAssets, "fonts/samplefont.ttf")

    mPaint.setTextSize(64)

    protected override def onDraw(canvas: Canvas) {
      canvas.drawColor(Color.WHITE)
      mPaint.setTypeface(null)
      canvas.drawText("Default", 10, 100, mPaint)
      mPaint.setTypeface(mFace)
      canvas.drawText("Custom", 10, 200, mPaint)
    }
  }
}

class Typefaces extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
