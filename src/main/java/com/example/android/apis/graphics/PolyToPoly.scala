package com.example.android.apis.graphics

import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view.View
import PolyToPoly._
//remove if not needed
import scala.collection.JavaConversions._

object PolyToPoly {

  private class SampleView(context: Context) extends View(context) {

    private var mPaint: Paint = new Paint(Paint.ANTI_ALIAS_FLAG)

    private var mMatrix: Matrix = new Matrix()

    private var mFontMetrics: Paint.FontMetrics = mPaint.getFontMetrics

    private def doDraw(canvas: Canvas, src: Array[Float], dst: Array[Float]) {
      canvas.save()
      mMatrix.setPolyToPoly(src, 0, dst, 0, src.length >> 1)
      canvas.concat(mMatrix)
      mPaint.setColor(Color.GRAY)
      mPaint.setStyle(Paint.Style.STROKE)
      canvas.drawRect(0, 0, 64, 64, mPaint)
      canvas.drawLine(0, 0, 64, 64, mPaint)
      canvas.drawLine(0, 64, 64, 0, mPaint)
      mPaint.setColor(Color.RED)
      mPaint.setStyle(Paint.Style.FILL)
      val x = 64 / 2
      val y = 64 / 2 - (mFontMetrics.ascent + mFontMetrics.descent) / 2
      canvas.drawText(src.length / 2 + "", x, y, mPaint)
      canvas.restore()
    }

    mPaint.setStrokeWidth(4)

    mPaint.setTextSize(40)

    mPaint.setTextAlign(Paint.Align.CENTER)

    protected override def onDraw(canvas: Canvas) {
      canvas.drawColor(Color.WHITE)
      canvas.save()
      canvas.translate(10, 10)
      doDraw(canvas, Array(0, 0), Array(5, 5))
      canvas.restore()
      canvas.save()
      canvas.translate(160, 10)
      doDraw(canvas, Array(32, 32, 64, 32), Array(32, 32, 64, 48))
      canvas.restore()
      canvas.save()
      canvas.translate(10, 110)
      doDraw(canvas, Array(0, 0, 64, 0, 0, 64), Array(0, 0, 96, 0, 24, 64))
      canvas.restore()
      canvas.save()
      canvas.translate(160, 110)
      doDraw(canvas, Array(0, 0, 64, 0, 64, 64, 0, 64), Array(0, 0, 96, 0, 64, 96, 0, 64))
      canvas.restore()
    }
  }
}

class PolyToPoly extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
