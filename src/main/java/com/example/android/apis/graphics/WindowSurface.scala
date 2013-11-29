package com.example.android.apis.graphics

import android.app.Activity
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.SurfaceHolder
import WindowSurface._
//remove if not needed
import scala.collection.JavaConversions._

object WindowSurface {

  class MovingPoint {

    var x: Float = _

    var y: Float = _

    var dx: Float = _

    var dy: Float = _

    def init(width: Int, height: Int, minStep: Float) {
      x = ((width - 1) * Math.random()).toFloat
      y = ((height - 1) * Math.random()).toFloat
      dx = (Math.random() * minStep * 2).toFloat + 1
      dy = (Math.random() * minStep * 2).toFloat + 1
    }

    def adjDelta(cur: Float, minStep: Float, maxStep: Float): Float = {
      var curVar = cur
      curVar += ((Math.random() * minStep) - (minStep / 2)).toFloat
      if (curVar < 0 && curVar > -minStep) curVar = -minStep
      if (curVar >= 0 && curVar < minStep) curVar = minStep
      if (curVar > maxStep) curVar = maxStep
      if (curVar < -maxStep) curVar = -maxStep
      curVar
    }

    def step(width: Int,
             height: Int,
             minStep: Float,
             maxStep: Float) {
      x += dx
      if (x <= 0 || x >= (width - 1)) {
        if (x <= 0) x = 0 else if (x >= (width - 1)) x = width - 1
        dx = adjDelta(-dx, minStep, maxStep)
      }
      y += dy
      if (y <= 0 || y >= (height - 1)) {
        if (y <= 0) y = 0 else if (y >= (height - 1)) y = height - 1
        dy = adjDelta(-dy, minStep, maxStep)
      }
    }
  }
}

class WindowSurface extends Activity with SurfaceHolder.Callback2 {
  val NUM_OLD = 100

  var mDrawingThread: DrawingThread = _

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    getWindow.takeSurface(this)
    mDrawingThread = new DrawingThread()
    mDrawingThread.start()
  }

  protected override def onPause() {
    super.onPause()
    synchronized (mDrawingThread)
    mDrawingThread.mRunning = false
    mDrawingThread.notify()

  }

  protected override def onResume() {
    super.onResume()
    synchronized (mDrawingThread)
    mDrawingThread.mRunning = true
    mDrawingThread.notify()

  }

  protected override def onDestroy() {
    super.onDestroy()
    synchronized (mDrawingThread)
    mDrawingThread.mQuit = true
    mDrawingThread.notify()

  }

  def surfaceCreated(holder: SurfaceHolder) {
    synchronized (mDrawingThread)
    mDrawingThread.mSurface = holder
    mDrawingThread.notify()

  }

  def surfaceChanged(holder: SurfaceHolder,
                     format: Int,
                     width: Int,
                     height: Int) {
  }

  def surfaceRedrawNeeded(holder: SurfaceHolder) {
  }

  def surfaceDestroyed(holder: SurfaceHolder) {
    synchronized (mDrawingThread)
    mDrawingThread.mSurface = holder
    mDrawingThread.notify()
    while (mDrawingThread.mActive) {
      try {
        mDrawingThread.wait()
      } catch {
        case e: java.lang.InterruptedException => e.printStackTrace()
      }
    }
  }

  class DrawingThread extends Thread {

    var mSurface: SurfaceHolder = _

    var mRunning: Boolean = _

    var mActive: Boolean = _

    var mQuit: Boolean = _

    var mLineWidth: Int = _

    var mMinStep: Float = _

    var mMaxStep: Float = _

    var mInitialized: Boolean = _

    val mPoint1 = new MovingPoint()

    val mPoint2 = new MovingPoint()

    var mNumOld: Int = 0

    val mOld = new Array[Float](NUM_OLD * 4)

    val mOldColor = new Array[Int](NUM_OLD)

    var mBrightLine: Int = 0

    val mColor = new MovingPoint()

    val mBackground = new Paint()

    val mForeground = new Paint()

    def makeGreen(index: Int): Int = {
      val dist = Math.abs(mBrightLine - index)
      if (dist > 10) return 0
      (255 - (dist * (255 / 10))) << 8
    }

    override def run() {
      mLineWidth = (getResources.getDisplayMetrics.density * 1.5).toInt
      if (mLineWidth < 1) mLineWidth = 1
      mMinStep = mLineWidth * 2
      mMaxStep = mMinStep * 3
      mBackground.setColor(0xff000000)
      mForeground.setColor(0xff00ffff)
      mForeground.setAntiAlias(false)
      mForeground.setStrokeWidth(mLineWidth)
      while (true) {
        synchronized (this)
        while (mSurface == null || !mRunning) {
          if (mActive) {
            mActive = false
            notify()
          }
          if (mQuit) {
            return
          }
          try {
            wait()
          } catch {
            case e: java.lang.InterruptedException =>
          }
        }
        if (!mActive) {
          mActive = true
          notify()
        }
        val canvas = mSurface.lockCanvas()
        if (canvas == null) {
          Log.i("WindowSurface", "Failure locking canvas")
          //continue
        }
        if (!mInitialized) {
          mInitialized = true
          mPoint1.init(canvas.getWidth, canvas.getHeight, mMinStep)
          mPoint2.init(canvas.getWidth, canvas.getHeight, mMinStep)
          mColor.init(127, 127, 1)
        } else {
          mPoint1.step(canvas.getWidth, canvas.getHeight, mMinStep, mMaxStep)
          mPoint2.step(canvas.getWidth, canvas.getHeight, mMinStep, mMaxStep)
          mColor.step(127, 127, 1, 3)
        }
        mBrightLine += 2
        if (mBrightLine > (NUM_OLD * 2)) {
          mBrightLine = -2
        }
        canvas.drawColor(mBackground.getColor)
        var i = mNumOld - 1
        while (i >= 0) {
          mForeground.setColor(mOldColor(i) | makeGreen(i))
          mForeground.setAlpha(((NUM_OLD - i) * 255) / NUM_OLD)
          val p = i * 4
          canvas.drawLine(mOld(p), mOld(p + 1), mOld(p + 2), mOld(p + 3), mForeground)
          i -= 1
        }
        var red = mColor.x.toInt + 128
        if (red > 255) red = 255
        var blue = mColor.y.toInt + 128
        if (blue > 255) blue = 255
        val color = 0xff000000 | (red << 16) | blue
        mForeground.setColor(color | makeGreen(-2))
        canvas.drawLine(mPoint1.x, mPoint1.y, mPoint2.x, mPoint2.y, mForeground)
        if (mNumOld > 1) {
          System.arraycopy(mOld, 0, mOld, 4, (mNumOld - 1) * 4)
          System.arraycopy(mOldColor, 0, mOldColor, 1, mNumOld - 1)
        }
        if (mNumOld < NUM_OLD) mNumOld += 1
        mOld(0) = mPoint1.x
        mOld(1) = mPoint1.y
        mOld(2) = mPoint2.x
        mOld(3) = mPoint2.y
        mOldColor(0) = color
        mSurface.unlockCanvasAndPost(canvas)

      }
    }
  }
}
