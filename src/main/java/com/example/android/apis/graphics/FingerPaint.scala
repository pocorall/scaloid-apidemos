package com.example.android.apis.graphics

import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import FingerPaint._
//remove if not needed
import scala.collection.JavaConversions._

object FingerPaint {

  private val MINP = 0.25f

  private val MAXP = 0.75f

  private val TOUCH_TOLERANCE = 4

  private val COLOR_MENU_ID = Menu.FIRST

  private val EMBOSS_MENU_ID = Menu.FIRST + 1

  private val BLUR_MENU_ID = Menu.FIRST + 2

  private val ERASE_MENU_ID = Menu.FIRST + 3

  private val SRCATOP_MENU_ID = Menu.FIRST + 4
}

class FingerPaint extends GraphicsActivity with ColorPickerDialog.OnColorChangedListener {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new MyView(this))
    mPaint = new Paint()
    mPaint.setAntiAlias(true)
    mPaint.setDither(true)
    mPaint.setColor(0xFFFF0000)
    mPaint.setStyle(Paint.Style.STROKE)
    mPaint.setStrokeJoin(Paint.Join.ROUND)
    mPaint.setStrokeCap(Paint.Cap.ROUND)
    mPaint.setStrokeWidth(12)
    mEmboss = new EmbossMaskFilter(Array(1, 1, 1), 0.4f, 6, 3.5f)
    mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL)
  }

  private var mPaint: Paint = _

  private var mEmboss: MaskFilter = _

  private var mBlur: MaskFilter = _

  def colorChanged(color: Int) {
    mPaint.setColor(color)
  }

  class MyView(c: Context) extends View(c) {

    private var mBitmap: Bitmap = _

    private var mCanvas: Canvas = _

    private var mPath: Path = new Path()

    private var mBitmapPaint: Paint = new Paint(Paint.DITHER_FLAG)

    protected override def onSizeChanged(w: Int,
                                         h: Int,
                                         oldw: Int,
                                         oldh: Int) {
      super.onSizeChanged(w, h, oldw, oldh)
      mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
      mCanvas = new Canvas(mBitmap)
    }

    protected override def onDraw(canvas: Canvas) {
      canvas.drawColor(0xFFAAAAAA)
      canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint)
      canvas.drawPath(mPath, mPaint)
    }

    private var mX: Float = _

    private var mY: Float = _

    private def touch_start(x: Float, y: Float) {
      mPath.reset()
      mPath.moveTo(x, y)
      mX = x
      mY = y
    }

    private def touch_move(x: Float, y: Float) {
      val dx = Math.abs(x - mX)
      val dy = Math.abs(y - mY)
      if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
        mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
        mX = x
        mY = y
      }
    }

    private def touch_up() {
      mPath.lineTo(mX, mY)
      mCanvas.drawPath(mPath, mPaint)
      mPath.reset()
    }

    override def onTouchEvent(event: MotionEvent): Boolean = {
      val x = event.getX
      val y = event.getY
      event.getAction match {
        case MotionEvent.ACTION_DOWN =>
          touch_start(x, y)
          invalidate()

        case MotionEvent.ACTION_MOVE =>
          touch_move(x, y)
          invalidate()

        case MotionEvent.ACTION_UP =>
          touch_up()
          invalidate()

      }
      true
    }
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    super.onCreateOptionsMenu(menu)
    menu.add(0, COLOR_MENU_ID, 0, "Color").setShortcut('3', 'c')
    menu.add(0, EMBOSS_MENU_ID, 0, "Emboss").setShortcut('4', 's')
    menu.add(0, BLUR_MENU_ID, 0, "Blur").setShortcut('5', 'z')
    menu.add(0, ERASE_MENU_ID, 0, "Erase").setShortcut('5', 'z')
    menu.add(0, SRCATOP_MENU_ID, 0, "SrcATop").setShortcut('5', 'z')
    true
  }

  override def onPrepareOptionsMenu(menu: Menu): Boolean = {
    super.onPrepareOptionsMenu(menu)
    true
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    mPaint.setXfermode(null)
    mPaint.setAlpha(0xFF)
    item.getItemId match {
      case COLOR_MENU_ID =>
        new ColorPickerDialog(this, this, mPaint.getColor).show()
        return true

      case EMBOSS_MENU_ID =>
        if (mPaint.getMaskFilter != mEmboss) {
          mPaint.setMaskFilter(mEmboss)
        } else {
          mPaint.setMaskFilter(null)
        }
        return true

      case BLUR_MENU_ID =>
        if (mPaint.getMaskFilter != mBlur) {
          mPaint.setMaskFilter(mBlur)
        } else {
          mPaint.setMaskFilter(null)
        }
        return true

      case ERASE_MENU_ID =>
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR))
        return true

      case SRCATOP_MENU_ID =>
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP))
        mPaint.setAlpha(0x80)
        return true

    }
    super.onOptionsItemSelected(item)
  }
}
