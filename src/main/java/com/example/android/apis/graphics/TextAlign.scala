package com.example.android.apis.graphics

import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view._
import TextAlign._
//remove if not needed
import scala.collection.JavaConversions._

object TextAlign {

  object SampleView {

    private val DY = 30

    private val TEXT_L = "Left"

    private val TEXT_C = "Center"

    private val TEXT_R = "Right"

    private val POSTEXT = "Positioned"

    private val TEXTONPATH = "Along a path"

    private def makePath(p: Path) {
      p.moveTo(10, 0)
      p.cubicTo(100, -50, 200, 50, 300, 0)
    }
  }

  private class SampleView(context: Context) extends View(context) {

    private var mPaint: Paint = new Paint()

    private var mX: Float = _

    private var mPos: Array[Float] = buildTextPositions(SampleView.POSTEXT, 0, mPaint)

    private var mPath: Path = new Path()

    private var mPathPaint: Paint = new Paint()

    private def buildTextPositions(text: String, y: Float, paint: Paint): Array[Float] = {
      val widths: Array[Float] = new Array[Float](text.length)
      val n: Int = paint.getTextWidths(text, widths)
      val pos: Array[Float] = new Array[Float](n * 2)
      var accumulatedX: Float = 0
      var i: Int = 0
      while (i < n) {
        {
          pos(i * 2 + 0) = accumulatedX
          pos(i * 2 + 1) = y
          accumulatedX += widths(i)
        }
        ({
          i += 1; i - 1
        })
      }
      return pos
    }

    setFocusable(true)

    mPaint.setAntiAlias(true)

    mPaint.setTextSize(30)

    mPaint.setTypeface(Typeface.SERIF)

    SampleView.makePath(mPath)

    mPathPaint.setAntiAlias(true)

    mPathPaint.setColor(0x800000FF)

    mPathPaint.setStyle(Paint.Style.STROKE)

    protected override def onDraw(canvas: Canvas) {
      canvas.drawColor(Color.WHITE)
      val p = mPaint
      val x = mX
      val y = 0
      val pos = mPos
      p.setColor(0x80FF0000)
      canvas.drawLine(x, y, x, y + SampleView.DY * 3, p)
      p.setColor(Color.BLACK)
      canvas.translate(0, SampleView.DY)
      p.setTextAlign(Paint.Align.LEFT)
      canvas.drawText(SampleView.TEXT_L, x, y, p)
      canvas.translate(0, SampleView.DY)
      p.setTextAlign(Paint.Align.CENTER)
      canvas.drawText(SampleView.TEXT_C, x, y, p)
      canvas.translate(0, SampleView.DY)
      p.setTextAlign(Paint.Align.RIGHT)
      canvas.drawText(SampleView.TEXT_R, x, y, p)
      canvas.translate(100, SampleView.DY * 2)
      p.setColor(0xBB00FF00)
      for (i <- 0 until pos.length / 2) {
        canvas.drawLine(pos(i * 2 + 0), pos(i * 2 + 1) - SampleView.DY, pos(i * 2 + 0), pos(i * 2 + 1) + SampleView.DY * 2,
          p)
      }
      p.setColor(Color.BLACK)
      p.setTextAlign(Paint.Align.LEFT)
      canvas.drawPosText(SampleView.POSTEXT, pos, p)
      canvas.translate(0, SampleView.DY)
      p.setTextAlign(Paint.Align.CENTER)
      canvas.drawPosText(SampleView.POSTEXT, pos, p)
      canvas.translate(0, SampleView.DY)
      p.setTextAlign(Paint.Align.RIGHT)
      canvas.drawPosText(SampleView.POSTEXT, pos, p)
      canvas.translate(-100, SampleView.DY * 2)
      canvas.drawPath(mPath, mPathPaint)
      p.setTextAlign(Paint.Align.LEFT)
      canvas.drawTextOnPath(SampleView.TEXTONPATH, mPath, 0, 0, p)
      canvas.translate(0, SampleView.DY * 1.5f)
      canvas.drawPath(mPath, mPathPaint)
      p.setTextAlign(Paint.Align.CENTER)
      canvas.drawTextOnPath(SampleView.TEXTONPATH, mPath, 0, 0, p)
      canvas.translate(0, SampleView.DY * 1.5f)
      canvas.drawPath(mPath, mPathPaint)
      p.setTextAlign(Paint.Align.RIGHT)
      canvas.drawTextOnPath(SampleView.TEXTONPATH, mPath, 0, 0, p)
    }

    protected override def onSizeChanged(w: Int,
                                         h: Int,
                                         ow: Int,
                                         oh: Int) {
      super.onSizeChanged(w, h, ow, oh)
      mX = w * 0.5f
    }
  }
}

class TextAlign extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
