package com.example.android.apis.graphics

import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view.View
import Clipping._
//remove if not needed
import scala.collection.JavaConversions._

object Clipping {

  private class SampleView(context: Context) extends View(context) {

    private var mPaint: Paint = new Paint()

    private var mPath: Path = new Path()

    setFocusable(true)

    mPaint.setAntiAlias(true)

    mPaint.setStrokeWidth(6)

    mPaint.setTextSize(16)

    mPaint.setTextAlign(Paint.Align.RIGHT)

    private def drawScene(canvas: Canvas) {
      canvas.clipRect(0, 0, 100, 100)
      canvas.drawColor(Color.WHITE)
      mPaint.setColor(Color.RED)
      canvas.drawLine(0, 0, 100, 100, mPaint)
      mPaint.setColor(Color.GREEN)
      canvas.drawCircle(30, 70, 30, mPaint)
      mPaint.setColor(Color.BLUE)
      canvas.drawText("Clipping", 100, 30, mPaint)
    }

    protected override def onDraw(canvas: Canvas) {
      canvas.drawColor(Color.GRAY)
      canvas.save()
      canvas.translate(10, 10)
      drawScene(canvas)
      canvas.restore()
      canvas.save()
      canvas.translate(160, 10)
      canvas.clipRect(10, 10, 90, 90)
      canvas.clipRect(30, 30, 70, 70, Region.Op.DIFFERENCE)
      drawScene(canvas)
      canvas.restore()
      canvas.save()
      canvas.translate(10, 160)
      mPath.reset()
      canvas.clipPath(mPath)
      mPath.addCircle(50, 50, 50, Path.Direction.CCW)
      canvas.clipPath(mPath, Region.Op.REPLACE)
      drawScene(canvas)
      canvas.restore()
      canvas.save()
      canvas.translate(160, 160)
      canvas.clipRect(0, 0, 60, 60)
      canvas.clipRect(40, 40, 100, 100, Region.Op.UNION)
      drawScene(canvas)
      canvas.restore()
      canvas.save()
      canvas.translate(10, 310)
      canvas.clipRect(0, 0, 60, 60)
      canvas.clipRect(40, 40, 100, 100, Region.Op.XOR)
      drawScene(canvas)
      canvas.restore()
      canvas.save()
      canvas.translate(160, 310)
      canvas.clipRect(0, 0, 60, 60)
      canvas.clipRect(40, 40, 100, 100, Region.Op.REVERSE_DIFFERENCE)
      drawScene(canvas)
      canvas.restore()
    }
  }
}

class Clipping extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
