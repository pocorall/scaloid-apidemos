package com.example.android.apis.graphics

import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view.View
import DrawPoints._
//remove if not needed
import scala.collection.JavaConversions._

object DrawPoints {

  private val SIZE = 300

  private val SEGS = 32

  private val X = 0

  private val Y = 1


  private class SampleView(context: Context) extends View(context) {

    private var mPaint: Paint = new Paint()

    private var mPts: Array[Float] = _

    private def buildPoints() {
      val ptCount = (SEGS + 1) * 2
      mPts = Array.ofDim[Float](ptCount * 2)
      var value = 0
      val delta = SIZE / SEGS
      var i = 0
      while (i <= SEGS) {
        mPts(i * 4 + X) = SIZE - value
        mPts(i * 4 + Y) = 0
        mPts(i * 4 + X + 2) = 0
        mPts(i * 4 + Y + 2) = value
        value += delta
        i += 1
      }
    }

    buildPoints()

    protected override def onDraw(canvas: Canvas) {
      val paint = mPaint
      canvas.translate(10, 10)
      canvas.drawColor(Color.WHITE)
      paint.setColor(Color.RED)
      paint.setStrokeWidth(0)
      canvas.drawLines(mPts, paint)
      paint.setColor(Color.BLUE)
      paint.setStrokeWidth(3)
      canvas.drawPoints(mPts, paint)
    }
  }
}

class DrawPoints extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
