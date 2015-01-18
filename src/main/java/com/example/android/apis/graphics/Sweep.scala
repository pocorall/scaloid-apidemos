package com.example.android.apis.graphics

import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import Sweep._
//remove if not needed
import scala.collection.JavaConversions._

object Sweep {

  private class SampleView(context: Context) extends View(context) {

    private var mPaint: Paint = new Paint(Paint.ANTI_ALIAS_FLAG)

    private var mRotate: Float = _

    private var mMatrix: Matrix = new Matrix()

    private var mShader: Shader = new SweepGradient(x, y, Array(Color.GREEN, Color.RED, Color.BLUE, Color.GREEN),
      null)

    private var mDoTiming: Boolean = _

    setFocusable(true)

    setFocusableInTouchMode(true)

    val x = 160

    val y = 100

    mPaint.setShader(mShader)

    protected override def onDraw(canvas: Canvas) {
      val paint = mPaint
      val x = 160
      val y = 100
      canvas.drawColor(Color.WHITE)
      mMatrix.setRotate(mRotate, x, y)
      mShader.setLocalMatrix(mMatrix)
      mRotate += 3
      if (mRotate >= 360) {
        mRotate = 0
      }
      invalidate()
      if (mDoTiming) {
        var now = System.currentTimeMillis()
        for (i <- 0 until 20) {
          canvas.drawCircle(x, y, 80, paint)
        }
        now = System.currentTimeMillis() - now
        android.util.Log.d("skia", "sweep ms = " + (now / 20.0))
      } else {
        canvas.drawCircle(x, y, 80, paint)
      }
    }

    override def onKeyDown(keyCode: Int, event: KeyEvent): Boolean = keyCode match {
      case KeyEvent.KEYCODE_D =>
        mPaint.setDither(!mPaint.isDither)
        invalidate()
        true

      case KeyEvent.KEYCODE_T =>
        mDoTiming = !mDoTiming
        invalidate()
        true
      case _ =>
        return super.onKeyDown(keyCode, event)
    }
  }
}

class Sweep extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
