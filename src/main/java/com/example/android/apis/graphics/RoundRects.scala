package com.example.android.apis.graphics

import android.content.Context
import android.graphics._
import android.graphics.drawable._
import android.os.Bundle
import android.view._
import RoundRects._
//remove if not needed
import scala.collection.JavaConversions._

object RoundRects {

  object SampleView {

    def setCornerRadii(drawable: GradientDrawable,
                       r0: Float,
                       r1: Float,
                       r2: Float,
                       r3: Float) {
      drawable.setCornerRadii(Array(r0, r0, r1, r1, r2, r2, r3, r3))
    }
  }

  private class SampleView(context: Context) extends View(context) {

    private var mPath: Path = new Path()

    private var mPaint: Paint = new Paint(Paint.ANTI_ALIAS_FLAG)

    private var mRect: Rect = new Rect(0, 0, 120, 120)

    private var mDrawable: GradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
      Array(0xFFFF0000, 0xFF00FF00, 0xFF0000FF))

    setFocusable(true)

    mDrawable.setShape(GradientDrawable.RECTANGLE)

    mDrawable.setGradientRadius((Math.sqrt(2) * 60).toFloat)

    protected override def onDraw(canvas: Canvas) {
      mDrawable.setBounds(mRect)
      val r = 16
      canvas.save()
      canvas.translate(10, 10)
      mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT)
      SampleView.setCornerRadii(mDrawable, r, r, 0, 0)
      mDrawable.draw(canvas)
      canvas.restore()
      canvas.save()
      canvas.translate(10 + mRect.width() + 10, 10)
      mDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT)
      SampleView.setCornerRadii(mDrawable, 0, 0, r, r)
      mDrawable.draw(canvas)
      canvas.restore()
      canvas.translate(0, mRect.height() + 10)
      canvas.save()
      canvas.translate(10, 10)
      mDrawable.setGradientType(GradientDrawable.SWEEP_GRADIENT)
      SampleView.setCornerRadii(mDrawable, 0, r, r, 0)
      mDrawable.draw(canvas)
      canvas.restore()
      canvas.save()
      canvas.translate(10 + mRect.width() + 10, 10)
      mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT)
      SampleView.setCornerRadii(mDrawable, r, 0, 0, r)
      mDrawable.draw(canvas)
      canvas.restore()
      canvas.translate(0, mRect.height() + 10)
      canvas.save()
      canvas.translate(10, 10)
      mDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT)
      SampleView.setCornerRadii(mDrawable, r, 0, r, 0)
      mDrawable.draw(canvas)
      canvas.restore()
      canvas.save()
      canvas.translate(10 + mRect.width() + 10, 10)
      mDrawable.setGradientType(GradientDrawable.SWEEP_GRADIENT)
      SampleView.setCornerRadii(mDrawable, 0, r, 0, r)
      mDrawable.draw(canvas)
      canvas.restore()
    }
  }
}

class RoundRects extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
