package com.example.android.apis.graphics

import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view._
import Layers._
//remove if not needed
import scala.collection.JavaConversions._

object Layers {

  object SampleView {

    private val LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
      Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
      Canvas.CLIP_TO_LAYER_SAVE_FLAG
  }

  private class SampleView(context: Context) extends View(context) {

    private var mPaint: Paint = new Paint()

    setFocusable(true)

    mPaint.setAntiAlias(true)

    protected override def onDraw(canvas: Canvas) {
      canvas.drawColor(Color.WHITE)
      canvas.translate(10, 10)
      canvas.saveLayerAlpha(0, 0, 200, 200, 0x88, SampleView.LAYER_FLAGS)
      mPaint.setColor(Color.RED)
      canvas.drawCircle(75, 75, 75, mPaint)
      mPaint.setColor(Color.BLUE)
      canvas.drawCircle(125, 125, 75, mPaint)
      canvas.restore()
    }
  }
}

class Layers extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
