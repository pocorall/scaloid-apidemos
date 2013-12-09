package com.example.android.apis.graphics

import com.example.android.apis.R
import android.app.Activity
import android.content.Context
import android.graphics._
import android.graphics.drawable._
import android.os.Bundle
import android.view._
import ColorFilters._
//remove if not needed
import scala.collection.JavaConversions._

object ColorFilters {

  object SampleView {

    private def addToTheRight(curr: Drawable, prev: Drawable) {
      val r = prev.getBounds
      val x = r.right + 12
      val center = (r.top + r.bottom) >> 1
      val h = curr.getIntrinsicHeight
      val y = center - (h >> 1)
      curr.setBounds(x, y, x + curr.getIntrinsicWidth, y + h)
    }
  }

  private class SampleView(private var activity: Activity) extends View(activity) {
    val context = activity

    setFocusable(true)

    val resIDs = Array(R.drawable.btn_circle_normal, R.drawable.btn_check_off, R.drawable.btn_check_on)

    private var mPaint: Paint = new Paint()

    private var mPaint2: Paint = new Paint(mPaint)

    val fm = mPaint.getFontMetrics

    mPaint.setAntiAlias(true)

    mPaint.setTextSize(16)

    mPaint.setTextAlign(Paint.Align.CENTER)

    mPaint2.setAlpha(64)

    private var mDrawable: Drawable = context.getResources.getDrawable(R.drawable.btn_default_normal)

    private var mDrawables: Array[Drawable] = new Array[Drawable](resIDs.length)

    private var mPaintTextOffset: Float = (fm.descent + fm.ascent) * 0.5f

    private var mColors: Array[Int] = Array(0, 0xCC0000FF, 0x880000FF, 0x440000FF, 0xFFCCCCFF, 0xFF8888FF, 0xFF4444FF)

    private var mModes: Array[PorterDuff.Mode] = Array(PorterDuff.Mode.SRC_ATOP, PorterDuff.Mode.MULTIPLY)

    private var mModeIndex: Int = 0

    mDrawable.setBounds(0, 0, 150, 48)

    mDrawable.setDither(true)

    var prev = mDrawable

    for (i <- 0 until resIDs.length) {
      mDrawables(i) = context.getResources.getDrawable(resIDs(i))
      mDrawables(i).setDither(true)
      SampleView.addToTheRight(mDrawables(i), prev)
      prev = mDrawables(i)
    }

    updateTitle()

    private def swapPaintColors() {
      if (mPaint.getColor == 0xFF000000) {
        mPaint.setColor(0xFFFFFFFF)
        mPaint2.setColor(0xFF000000)
      } else {
        mPaint.setColor(0xFF000000)
        mPaint2.setColor(0xFFFFFFFF)
      }
      mPaint2.setAlpha(64)
    }

    private def updateTitle() {
      activity.setTitle(mModes(mModeIndex).toString)
    }

    private def drawSample(canvas: Canvas, filter: ColorFilter) {
      val r = mDrawable.getBounds
      val x = (r.left + r.right) * 0.5f
      val y = (r.top + r.bottom) * 0.5f - mPaintTextOffset
      mDrawable.setColorFilter(filter)
      mDrawable.draw(canvas)
      canvas.drawText("Label", x + 1, y + 1, mPaint2)
      canvas.drawText("Label", x, y, mPaint)
      for (dr <- mDrawables) {
        dr.setColorFilter(filter)
        dr.draw(canvas)
      }
    }

    protected override def onDraw(canvas: Canvas) {
      canvas.drawColor(0xFFCCCCCC)
      canvas.translate(8, 12)
      for (color <- mColors) {
        var filter: ColorFilter = null
        filter = if (color == 0) null else new PorterDuffColorFilter(color, mModes(mModeIndex))
        drawSample(canvas, filter)
        canvas.translate(0, 55)
      }
    }

    override def onTouchEvent(event: MotionEvent): Boolean = {
      event.getAction match {
        case MotionEvent.ACTION_DOWN =>
        case MotionEvent.ACTION_MOVE =>
        case MotionEvent.ACTION_UP =>
          if (mPaint.getColor == 0xFFFFFFFF) {
            mModeIndex = (mModeIndex + 1) % mModes.length
            updateTitle
          }
          swapPaintColors
          invalidate
      }
      return true
    }
  }
}

class ColorFilters extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
