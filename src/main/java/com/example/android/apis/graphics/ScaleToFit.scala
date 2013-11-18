package com.example.android.apis.graphics

import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view.View
import ScaleToFit._
//remove if not needed
import scala.collection.JavaConversions._

object ScaleToFit {


  private val sFits = Array(Matrix.ScaleToFit.FILL, Matrix.ScaleToFit.START, Matrix.ScaleToFit.CENTER, Matrix.ScaleToFit.END)

  private val sFitLabels = Array("FILL", "START", "CENTER", "END")

  private val sSrcData = Array(80, 40, Color.RED, 40, 80, Color.GREEN, 30, 30, Color.BLUE, 80, 80, Color.BLACK)

  private val N = 4

  private val WIDTH = 52

  private val HEIGHT = 52

  private class SampleView(context: Context) extends View(context) {

    private val mPaint = new Paint(Paint.ANTI_ALIAS_FLAG)

    private val mHairPaint = new Paint(Paint.ANTI_ALIAS_FLAG)

    private val mLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG)

    private val mMatrix = new Matrix()

    private val mSrcR = new RectF()

    private val mDstR = new RectF(0, 0, WIDTH, HEIGHT)

    mHairPaint.setStyle(Paint.Style.STROKE)

    mLabelPaint.setTextSize(16)

    private def setSrcR(index: Int) {
      val w = sSrcData(index * 3 + 0)
      val h = sSrcData(index * 3 + 1)
      mSrcR.set(0, 0, w, h)
    }

    private def drawSrcR(canvas: Canvas, index: Int) {
      mPaint.setColor(sSrcData(index * 3 + 2))
      canvas.drawOval(mSrcR, mPaint)
    }

    private def drawFit(canvas: Canvas, index: Int, stf: Matrix.ScaleToFit) {
      canvas.save()
      setSrcR(index)
      mMatrix.setRectToRect(mSrcR, mDstR, stf)
      canvas.concat(mMatrix)
      drawSrcR(canvas, index)
      canvas.restore()
      canvas.drawRect(mDstR, mHairPaint)
    }

    protected override def onDraw(canvas: Canvas) {
      canvas.drawColor(Color.WHITE)
      canvas.translate(10, 10)
      canvas.save()
      for (i <- 0 until N) {
        setSrcR(i)
        drawSrcR(canvas, i)
        canvas.translate(mSrcR.width() + 15, 0)
      }
      canvas.restore()
      canvas.translate(0, 100)
      for (j <- 0 until sFits.length) {
        canvas.save()
        for (i <- 0 until N) {
          drawFit(canvas, i, sFits(j))
          canvas.translate(mDstR.width() + 8, 0)
        }
        canvas.drawText(sFitLabels(j), 0, HEIGHT * 2 / 3, mLabelPaint)
        canvas.restore()
        canvas.translate(0, 80)
      }
    }
  }
}

class ScaleToFit extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
