package com.example.android.apis.graphics

import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import PathEffects._
//remove if not needed
import scala.collection.JavaConversions._

object PathEffects {

  object SampleView {

    private def makeDash(phase: Float): PathEffect = {
      new DashPathEffect(Array(15, 5, 8, 5), phase)
    }

    private def makeEffects(e: Array[PathEffect], phase: Float) {
      e(0) = null
      e(1) = new CornerPathEffect(10)
      e(2) = new DashPathEffect(Array(10, 5, 5, 5), phase)
      e(3) = new PathDashPathEffect(makePathDash(), 12, phase, PathDashPathEffect.Style.ROTATE)
      e(4) = new ComposePathEffect(e(2), e(1))
      e(5) = new ComposePathEffect(e(3), e(1))
    }

    private def makeFollowPath(): Path = {
      val p = new Path()
      p.moveTo(0, 0)
      var i = 1
      while (i <= 15) {
        p.lineTo(i * 20, Math.random().toFloat * 35)
        i += 1
      }
      p
    }

    private def makePathDash(): Path = {
      val p = new Path()
      p.moveTo(4, 0)
      p.lineTo(0, -4)
      p.lineTo(8, -4)
      p.lineTo(12, 0)
      p.lineTo(8, 4)
      p.lineTo(0, 4)
      p
    }
  }

  private class SampleView(context: Context) extends View(context) {

    private var mPaint: Paint = new Paint(Paint.ANTI_ALIAS_FLAG)

    private var mPath: Path = SampleView.makeFollowPath()

    private var mEffects: Array[PathEffect] = new Array[PathEffect](6)

    private var mColors: Array[Int] = Array(Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.BLACK)

    private var mPhase: Float = _

    setFocusable(true)

    setFocusableInTouchMode(true)

    mPaint.setStyle(Paint.Style.STROKE)

    mPaint.setStrokeWidth(6)

    protected override def onDraw(canvas: Canvas) {
      canvas.drawColor(Color.WHITE)
      val bounds = new RectF()
      mPath.computeBounds(bounds, false)
      canvas.translate(10 - bounds.left, 10 - bounds.top)
      SampleView.makeEffects(mEffects, mPhase)
      mPhase += 1
      invalidate()
      for (i <- 0 until mEffects.length) {
        mPaint.setPathEffect(mEffects(i))
        mPaint.setColor(mColors(i))
        canvas.drawPath(mPath, mPaint)
        canvas.translate(0, 28)
      }
    }

    override def onKeyDown(keyCode: Int, event: KeyEvent): Boolean = {
      keyCode match {
        case KeyEvent.KEYCODE_DPAD_CENTER =>
          mPath = SampleView.makeFollowPath
          return true
        case _ =>
          return super.onKeyDown(keyCode, event)
      }
      return super.onKeyDown(keyCode, event)
    }
  }
}

class PathEffects extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
