package com.example.android.apis.graphics

import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view._
import MeasureText._
//remove if not needed
import scala.collection.JavaConversions._

object MeasureText {

  private val WIDTH = 50

  private val HEIGHT = 50

  private val STRIDE = 64

  private def createColors(): Array[Int] = {
    val colors = Array.ofDim[Int](STRIDE * HEIGHT)
    for (y <- 0 until HEIGHT; x <- 0 until WIDTH) {
      val r = x * 255 / (WIDTH - 1)
      val g = y * 255 / (HEIGHT - 1)
      val b = 255 - Math.min(r, g)
      val a = Math.max(r, g)
      colors(y * STRIDE + x) = (a << 24) | (r << 16) | (g << 8) | b
    }
    colors
  }

  private class SampleView(context: Context) extends View(context) {

    private var mPaint: Paint = new Paint()

    private var mOriginX: Float = 10

    private var mOriginY: Float = 80

    setFocusable(true)

    mPaint.setAntiAlias(true)

    mPaint.setStrokeWidth(5)

    mPaint.setStrokeCap(Paint.Cap.ROUND)

    mPaint.setTextSize(64)

    mPaint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC))

    private def showText(canvas: Canvas, text: String, align: Paint.Align) {
      val bounds = new Rect()
      val widths: Array[Float] = new Array[Float](text.length)
      val count = mPaint.getTextWidths(text, 0, text.length, widths)
      val w = mPaint.measureText(text, 0, text.length)
      mPaint.getTextBounds(text, 0, text.length, bounds)
      mPaint.setColor(0xFF88FF88)
      canvas.drawRect(bounds, mPaint)
      mPaint.setColor(Color.BLACK)
      canvas.drawText(text, 0, 0, mPaint)
      val pts = Array.ofDim[Float](2 + count * 2)
      var x: Float = 0
      val y: Float = 0
      pts(0) = x
      pts(1) = y
      for (i <- 0 until count) {
        x += widths(i)
        pts(2 + i * 2) = x
        pts(2 + i * 2 + 1) = y
      }
      mPaint.setColor(Color.RED)
      mPaint.setStrokeWidth(0)
      canvas.drawLine(0, 0, w, 0, mPaint)
      mPaint.setStrokeWidth(5)
      canvas.drawPoints(pts, 0, (count + 1) << 1, mPaint)
    }

    protected override def onDraw(canvas: Canvas) {
      canvas.drawColor(Color.WHITE)
      canvas.translate(mOriginX, mOriginY)
      showText(canvas, "Measure", Paint.Align.LEFT)
      canvas.translate(0, 80)
      showText(canvas, "wiggy!", Paint.Align.CENTER)
      canvas.translate(0, 80)
      showText(canvas, "Text", Paint.Align.RIGHT)
    }
  }
}

class MeasureText extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
