package com.example.android.apis.graphics

import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.Window
import UnicodeChart._
//remove if not needed
import scala.collection.JavaConversions._

object UnicodeChart {
  private val XMUL = 20

  private val YMUL = 28

  private val YBASE = 18

  private class SampleView(context: Context) extends View(context) {

    private var mBigCharPaint: Paint = new Paint(Paint.ANTI_ALIAS_FLAG)

    private var mLabelPaint: Paint = new Paint(Paint.ANTI_ALIAS_FLAG)

    private val mChars = new Array[Char](256)

    private val mPos = new Array[Float](512)

    private var mBase: Int = _

    setFocusable(true)

    setFocusableInTouchMode(true)

    mBigCharPaint.setTextSize(15)

    mBigCharPaint.setTextAlign(Paint.Align.CENTER)

    mLabelPaint.setTextSize(8)

    mLabelPaint.setTextAlign(Paint.Align.CENTER)

    val pos = mPos

    var index: Int = 0
    var indexTmp: Int = 0
    for (col <- 0 until 16) {
      val x = col * XMUL + 10
      for (row <- 0 until 16) {

        pos(index) = x
        index += 1

        pos(index) = row * YMUL + YBASE
        index += 1
      }
    }

    private def computeX(index: Int): Float = (index >> 4) * XMUL + 10

    private def computeY(index: Int): Float = (index & 0xF) * YMUL + YMUL

    private def drawChart(canvas: Canvas, base: Int) {
      val chars = mChars
      for (i <- 0 until 256) {
        val unichar = base + i
        chars(i) = unichar.toChar
        canvas.drawText(java.lang.Integer.toHexString(unichar), computeX(i), computeY(i), mLabelPaint)
      }
      canvas.drawPosText(chars, 0, 256, mPos, mBigCharPaint)
    }

    protected override def onDraw(canvas: Canvas) {
      canvas.drawColor(Color.WHITE)
      canvas.translate(0, 1)
      drawChart(canvas, mBase * 256)
    }

    override def onKeyDown(keyCode: Int, event: KeyEvent): Boolean = keyCode match {
      case KeyEvent.KEYCODE_DPAD_LEFT =>
        if (mBase > 0) {
          mBase -= 1
          invalidate()
        }
        true

      case KeyEvent.KEYCODE_DPAD_RIGHT =>
        mBase += 1
        invalidate()
        true

      case _ => //break
        return super.onKeyDown(keyCode, event)
    }
  }
}

class UnicodeChart extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    setContentView(new SampleView(this))
  }
}
