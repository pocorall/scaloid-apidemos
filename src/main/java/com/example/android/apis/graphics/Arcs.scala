package com.example.android.apis.graphics

import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view.View
import Arcs._
//remove if not needed
import scala.collection.JavaConversions._

object Arcs {

  object SampleView {

    private val SWEEP_INC = 2

    private val START_INC = 15
  }

  private class SampleView(context: Context) extends View(context) {

    private var mPaints: Array[Paint] = new Array[Paint](4)

    private var mFramePaint: Paint = new Paint()

    private var mUseCenters: Array[Boolean] = new Array[Boolean](4)

    private var mOvals: Array[RectF] = new Array[RectF](4)

    private var mBigOval: RectF = new RectF(40, 10, 280, 250)

    private var mStart: Float = _

    private var mSweep: Float = _

    private var mBigIndex: Int = _

    mPaints(0) = new Paint()

    mPaints(0).setAntiAlias(true)

    mPaints(0).setStyle(Paint.Style.FILL)

    mPaints(0).setColor(0x88FF0000)

    mUseCenters(0) = false

    mPaints(1) = new Paint(mPaints(0))

    mPaints(1).setColor(0x8800FF00)

    mUseCenters(1) = true

    mPaints(2) = new Paint(mPaints(0))

    mPaints(2).setStyle(Paint.Style.STROKE)

    mPaints(2).setStrokeWidth(4)

    mPaints(2).setColor(0x880000FF)

    mUseCenters(2) = false

    mPaints(3) = new Paint(mPaints(2))

    mPaints(3).setColor(0x88888888)

    mUseCenters(3) = true

    mOvals(0) = new RectF(10, 270, 70, 330)

    mOvals(1) = new RectF(90, 270, 150, 330)

    mOvals(2) = new RectF(170, 270, 230, 330)

    mOvals(3) = new RectF(250, 270, 310, 330)

    mFramePaint.setAntiAlias(true)

    mFramePaint.setStyle(Paint.Style.STROKE)

    mFramePaint.setStrokeWidth(0)

    private def drawArcs(canvas: Canvas,
                         oval: RectF,
                         useCenter: Boolean,
                         paint: Paint) {
      canvas.drawRect(oval, mFramePaint)
      canvas.drawArc(oval, mStart, mSweep, useCenter, paint)
    }

    protected override def onDraw(canvas: Canvas) {
      canvas.drawColor(Color.WHITE)
      drawArcs(canvas, mBigOval, mUseCenters(mBigIndex), mPaints(mBigIndex))
      for (i <- 0 until 4) {
        drawArcs(canvas, mOvals(i), mUseCenters(i), mPaints(i))
      }
      mSweep += SampleView.SWEEP_INC
      if (mSweep > 360) {
        mSweep -= 360
        mStart += SampleView.START_INC
        if (mStart >= 360) {
          mStart -= 360
        }
        mBigIndex = (mBigIndex + 1) % mOvals.length
      }
      invalidate()
    }
  }
}

class Arcs extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
