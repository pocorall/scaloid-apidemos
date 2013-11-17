package com.example.android.apis.graphics

import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view.MotionEvent
import android.view._
import Patterns._
//remove if not needed
import scala.collection.JavaConversions._

object Patterns {

  private def makeBitmap1(): Bitmap = {
    val bm = Bitmap.createBitmap(40, 40, Bitmap.Config.RGB_565)
    val c = new Canvas(bm)
    c.drawColor(Color.RED)
    val p = new Paint()
    p.setColor(Color.BLUE)
    c.drawRect(5, 5, 35, 35, p)
    bm
  }

  private def makeBitmap2(): Bitmap = {
    val bm = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)
    val c = new Canvas(bm)
    val p = new Paint(Paint.ANTI_ALIAS_FLAG)
    p.setColor(Color.GREEN)
    p.setAlpha(0xCC)
    c.drawCircle(32, 32, 27, p)
    bm
  }

  private class SampleView(context: Context) extends View(context) {

    private val mShader1 = new BitmapShader(makeBitmap1(), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)

    private val mShader2 = new BitmapShader(makeBitmap2(), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)

    private val mPaint = new Paint(Paint.FILTER_BITMAP_FLAG)

    private val mFastDF = new PaintFlagsDrawFilter(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG, 0)

    private var mTouchStartX: Float = _

    private var mTouchStartY: Float = _

    private var mTouchCurrX: Float = _

    private var mTouchCurrY: Float = _

    private var mDF: DrawFilter = _

    setFocusable(true)

    setFocusableInTouchMode(true)

    val m = new Matrix()

    m.setRotate(30)

    mShader2.setLocalMatrix(m)

    protected override def onDraw(canvas: Canvas) {
      canvas.setDrawFilter(mDF)
      mPaint.setShader(mShader1)
      canvas.drawPaint(mPaint)
      canvas.translate(mTouchCurrX - mTouchStartX, mTouchCurrY - mTouchStartY)
      mPaint.setShader(mShader2)
      canvas.drawPaint(mPaint)
    }

    override def onTouchEvent(event: MotionEvent): Boolean = {
      val x: Float = event.getX
      val y: Float = event.getY
      event.getAction match {
        case MotionEvent.ACTION_DOWN =>
          mTouchStartX = ({
            mTouchCurrX = x; mTouchCurrX
          })
          mTouchStartY = ({
            mTouchCurrY = y; mTouchCurrY
          })
          mDF = mFastDF
          invalidate

        case MotionEvent.ACTION_MOVE =>
          mTouchCurrX = x
          mTouchCurrY = y
          invalidate()

        case MotionEvent.ACTION_UP =>
          mDF = null
          invalidate()

      }
      true
    }
  }
}

class Patterns extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
