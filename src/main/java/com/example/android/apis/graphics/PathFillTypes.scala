package com.example.android.apis.graphics

import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view.View
import PathFillTypes._
//remove if not needed
import scala.collection.JavaConversions._

object PathFillTypes {

  private class SampleView(context: Context) extends View(context) {

    private var mPaint: Paint = new Paint(Paint.ANTI_ALIAS_FLAG)

    private var mPath: Path = new Path()

    setFocusable(true)

    setFocusableInTouchMode(true)

    mPath.addCircle(40, 40, 45, Path.Direction.CCW)

    mPath.addCircle(80, 80, 45, Path.Direction.CCW)

    private def showPath(canvas: Canvas,
                         x: Int,
                         y: Int,
                         ft: Path.FillType,
                         paint: Paint) {
      canvas.save()
      canvas.translate(x, y)
      canvas.clipRect(0, 0, 120, 120)
      canvas.drawColor(Color.WHITE)
      mPath.setFillType(ft)
      canvas.drawPath(mPath, paint)
      canvas.restore()
    }

    protected override def onDraw(canvas: Canvas) {
      val paint = mPaint
      canvas.drawColor(0xFFCCCCCC)
      canvas.translate(20, 20)
      paint.setAntiAlias(true)
      showPath(canvas, 0, 0, Path.FillType.WINDING, paint)
      showPath(canvas, 160, 0, Path.FillType.EVEN_ODD, paint)
      showPath(canvas, 0, 160, Path.FillType.INVERSE_WINDING, paint)
      showPath(canvas, 160, 160, Path.FillType.INVERSE_EVEN_ODD, paint)
    }
  }
}

class PathFillTypes extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
