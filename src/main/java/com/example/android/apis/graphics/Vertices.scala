package com.example.android.apis.graphics

import com.example.android.apis.R
import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view._
import Vertices._
//remove if not needed
import scala.collection.JavaConversions._

object Vertices {

  private def setXY(array: Array[Float],
                    index: Int,
                    x: Float,
                    y: Float) {
    array(index * 2 + 0) = x
    array(index * 2 + 1) = y
  }


  private class SampleView(context: Context) extends View(context) {

    private val mPaint: Paint = new Paint
    private val mVerts: Array[Float] = new Array[Float](10)
    private val mTexs: Array[Float] = new Array[Float](10)
    private val mIndices: Array[Short] = Array(0, 1, 2, 3, 4, 1)
    private val mMatrix: Matrix = new Matrix
    private val mInverse: Matrix = new Matrix

    setFocusable(true)

    val bm = BitmapFactory.decodeResource(getResources, R.drawable.beach)

    val s = new BitmapShader(bm, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

    mPaint.setShader(s)

    val w = bm.getWidth

    val h = bm.getHeight

    setXY(mTexs, 0, w / 2, h / 2)

    setXY(mTexs, 1, 0, 0)

    setXY(mTexs, 2, w, 0)

    setXY(mTexs, 3, w, h)

    setXY(mTexs, 4, 0, h)

    setXY(mVerts, 0, w / 2, h / 2)

    setXY(mVerts, 1, 0, 0)

    setXY(mVerts, 2, w, 0)

    setXY(mVerts, 3, w, h)

    setXY(mVerts, 4, 0, h)

    mMatrix.setScale(0.8f, 0.8f)

    mMatrix.preTranslate(20, 20)

    mMatrix.invert(mInverse)

    protected override def onDraw(canvas: Canvas) {
      canvas.drawColor(0xFFCCCCCC)
      canvas.save()
      canvas.concat(mMatrix)
      canvas.drawVertices(Canvas.VertexMode.TRIANGLE_FAN, 10, mVerts, 0, mTexs, 0, null, 0, null, 0,
        0, mPaint)
      canvas.translate(0, 240)
      canvas.drawVertices(Canvas.VertexMode.TRIANGLE_FAN, 10, mVerts, 0, mTexs, 0, null, 0, mIndices,
        0, 6, mPaint)
      canvas.restore()
    }

    override def onTouchEvent(event: MotionEvent): Boolean = {
      val pt = Array(event.getX, event.getY)
      mInverse.mapPoints(pt)
      setXY(mVerts, 0, pt(0), pt(1))
      invalidate()
      true
    }
  }
}

class Vertices extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
