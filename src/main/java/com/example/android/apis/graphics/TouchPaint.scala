package com.example.android.apis.graphics

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import java.util.Random
import TouchPaint._
import com.example.android.apis.graphics.TouchPaint.PaintMode.PaintMode
import com.example.android.apis.graphics.TouchPaint.PaintMode

//remove if not needed
import scala.collection.JavaConversions._

object TouchPaint {

  private val MSG_FADE = 1

  private val CLEAR_ID = Menu.FIRST

  private val FADE_ID = Menu.FIRST + 1

  private val FADE_DELAY = 100

  val COLORS = Array(Color.WHITE, Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA)

  val BACKGROUND_COLOR = Color.BLACK

  object PaintMode extends Enumeration {

    val Draw = new PaintMode()

    val Splat = new PaintMode()

    val Erase = new PaintMode()

    class PaintMode extends Val

    implicit def convertValue(v: Value): PaintMode = v.asInstanceOf[PaintMode]
  }

  private val FADE_ALPHA = 0x06

  private val MAX_FADE_STEPS = 256 / FADE_ALPHA + 4

  private val TRACKBALL_SCALE = 10

  private val SPLAT_VECTORS = 40
}

class TouchPaint extends GraphicsActivity {

  var mView: PaintView = _

  var mFading: Boolean = _

  var mColorIndex: Int = _

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    mView = new PaintView(this)
    setContentView(mView)
    mView.requestFocus()
    if (savedInstanceState != null) {
      mFading = savedInstanceState.getBoolean("fading", true)
      mColorIndex = savedInstanceState.getInt("color", 0)
    } else {
      mFading = true
      mColorIndex = 0
    }
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    menu.add(0, CLEAR_ID, 0, "Clear")
    menu.add(0, FADE_ID, 0, "Fade").setCheckable(true)
    super.onCreateOptionsMenu(menu)
  }

  override def onPrepareOptionsMenu(menu: Menu): Boolean = {
    menu.findItem(FADE_ID).setChecked(mFading)
    super.onPrepareOptionsMenu(menu)
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = item.getItemId match {
    case CLEAR_ID =>
      mView.clear()
      true

    case FADE_ID =>
      mFading = !mFading
      if (mFading) {
        startFading()
      } else {
        stopFading()
      }
      true

    case _ => super.onOptionsItemSelected(item)
  }

  protected override def onResume() {
    super.onResume()
    if (mFading) {
      startFading()
    }
  }

  protected override def onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putBoolean("fading", mFading)
    outState.putInt("color", mColorIndex)
  }

  protected override def onPause() {
    super.onPause()
    stopFading()
  }

  def startFading() {
    mHandler.removeMessages(MSG_FADE)
    scheduleFade()
  }

  def stopFading() {
    mHandler.removeMessages(MSG_FADE)
  }

  def scheduleFade() {
    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_FADE), FADE_DELAY)
  }

  private var mHandler: Handler = new Handler() {

    override def handleMessage(msg: Message) {
      msg.what match {
        case MSG_FADE => {
          mView.fade()
          scheduleFade()
          //break
        }
        case _ => super.handleMessage(msg)
      }
    }
  }

  class PaintView(c: Context) extends View(c) {

    private val mRandom = new Random()

    private var mBitmap: Bitmap = _

    private var mCanvas: Canvas = _

    private val mPaint = new Paint()

    private val mFadePaint = new Paint()

    private var mCurX: Float = _

    private var mCurY: Float = _

    private var mOldButtonState: Int = _

    private var mFadeSteps: Int = MAX_FADE_STEPS

    setFocusable(true)

    mPaint.setAntiAlias(true)

    mFadePaint.setColor(BACKGROUND_COLOR)

    mFadePaint.setAlpha(FADE_ALPHA)

    def clear() {
      if (mCanvas != null) {
        mPaint.setColor(BACKGROUND_COLOR)
        mCanvas.drawPaint(mPaint)
        invalidate()
        mFadeSteps = MAX_FADE_STEPS
      }
    }

    def fade() {
      if (mCanvas != null && mFadeSteps < MAX_FADE_STEPS) {
        mCanvas.drawPaint(mFadePaint)
        invalidate()
        mFadeSteps += 1
      }
    }

    protected override def onSizeChanged(w: Int,
                                         h: Int,
                                         oldw: Int,
                                         oldh: Int) {
      var curW = if (mBitmap != null) mBitmap.getWidth else 0
      var curH = if (mBitmap != null) mBitmap.getHeight else 0
      if (curW >= w && curH >= h) {
        return
      }
      if (curW < w) curW = w
      if (curH < h) curH = h
      val newBitmap = Bitmap.createBitmap(curW, curH, Bitmap.Config.ARGB_8888)
      val newCanvas = new Canvas()
      newCanvas.setBitmap(newBitmap)
      if (mBitmap != null) {
        newCanvas.drawBitmap(mBitmap, 0, 0, null)
      }
      mBitmap = newBitmap
      mCanvas = newCanvas
      mFadeSteps = MAX_FADE_STEPS
    }

    protected override def onDraw(canvas: Canvas) {
      if (mBitmap != null) {
        canvas.drawBitmap(mBitmap, 0, 0, null)
      }
    }

    override def onTrackballEvent(event: MotionEvent): Boolean = {
      val action = event.getActionMasked
      if (action == MotionEvent.ACTION_DOWN) {
        advanceColor()
      }
      if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
        val N = event.getHistorySize
        val scaleX = event.getXPrecision * TRACKBALL_SCALE
        val scaleY = event.getYPrecision * TRACKBALL_SCALE
        for (i <- 0 until N) {
          moveTrackball(event.getHistoricalX(i) * scaleX, event.getHistoricalY(i) * scaleY)
        }
        moveTrackball(event.getX * scaleX, event.getY * scaleY)
      }
      true
    }

    private def moveTrackball(deltaX: Float, deltaY: Float) {
      val curW = if (mBitmap != null) mBitmap.getWidth else 0
      val curH = if (mBitmap != null) mBitmap.getHeight else 0
      mCurX = Math.max(Math.min(mCurX + deltaX, curW - 1), 0)
      mCurY = Math.max(Math.min(mCurY + deltaY, curH - 1), 0)
      paint(PaintMode.Draw, mCurX, mCurY)
    }

    override def onTouchEvent(event: MotionEvent): Boolean = onTouchOrHoverEvent(event, true)

    override def onHoverEvent(event: MotionEvent): Boolean = onTouchOrHoverEvent(event, false)

    private def onTouchOrHoverEvent(event: MotionEvent, isTouch: Boolean): Boolean = {
      val buttonState = event.getButtonState
      val pressedButtons = buttonState & ~mOldButtonState
      mOldButtonState = buttonState
      if ((pressedButtons & MotionEvent.BUTTON_SECONDARY) != 0) {
        advanceColor()
      }
      var mode: PaintMode = null
      if ((buttonState & MotionEvent.BUTTON_TERTIARY) != 0) {
        mode = PaintMode.Splat
      } else if (isTouch || (buttonState & MotionEvent.BUTTON_PRIMARY) != 0) {
        mode = PaintMode.Draw
      } else {
        return false
      }
      val action = event.getActionMasked
      if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE ||
        action == MotionEvent.ACTION_HOVER_MOVE) {
        val N = event.getHistorySize
        val P = event.getPointerCount
        for (i <- 0 until N; j <- 0 until P) {
          paint(getPaintModeForTool(event.getToolType(j), mode), event.getHistoricalX(j, i), event.getHistoricalY(j,
            i), event.getHistoricalPressure(j, i), event.getHistoricalTouchMajor(j, i), event.getHistoricalTouchMinor(j,
            i), event.getHistoricalOrientation(j, i), event.getHistoricalAxisValue(MotionEvent.AXIS_DISTANCE,
            j, i), event.getHistoricalAxisValue(MotionEvent.AXIS_TILT, j, i))
        }
        for (j <- 0 until P) {
          paint(getPaintModeForTool(event.getToolType(j), mode), event.getX(j), event.getY(j), event.getPressure(j),
            event.getTouchMajor(j), event.getTouchMinor(j), event.getOrientation(j), event.getAxisValue(MotionEvent.AXIS_DISTANCE,
              j), event.getAxisValue(MotionEvent.AXIS_TILT, j))
        }
        mCurX = event.getX
        mCurY = event.getY
      }
      true
    }

    private def getPaintModeForTool(toolType: Int, defaultMode: PaintMode): PaintMode = {
      if (toolType == MotionEvent.TOOL_TYPE_ERASER) {
        return PaintMode.Erase
      }
      defaultMode
    }

    private def advanceColor() {
      mColorIndex = (mColorIndex + 1) % COLORS.length
    }

    private def paint(mode: PaintMode, x: Float, y: Float) {
      paint(mode, x, y, 1.0f, 0, 0, 0, 0, 0)
    }

    private def paint(mode: PaintMode,
                      x: Float,
                      y: Float,
                      pressure: Float,
                      major: Float,
                      minor: Float,
                      orientation: Float,
                      distance: Float,
                      tilt: Float) {

      var majorVar: Float = major
      var minorVar: Float = minor
      if (mBitmap != null) {
        if (majorVar <= 0 || minorVar <= 0) {
          majorVar = ({
            minorVar = 16; minorVar
          })
        }
        mode match {
          case PaintMode.Draw =>
            mPaint.setColor(COLORS(mColorIndex))
            mPaint.setAlpha(Math.min((pressure * 128).toInt, 255))
            drawOval(mCanvas, x, y, majorVar, minorVar, orientation, mPaint)

          case PaintMode.Erase =>
            mPaint.setColor(BACKGROUND_COLOR)
            mPaint.setAlpha(Math.min((pressure * 128).toInt, 255))
            drawOval(mCanvas, x, y, majorVar, minorVar, orientation, mPaint)

          case PaintMode.Splat =>
            mPaint.setColor(COLORS(mColorIndex))
            mPaint.setAlpha(64)
            drawSplat(mCanvas, x, y, orientation, distance, tilt, mPaint)

        }
      }
      mFadeSteps = 0
      invalidate()
    }

    private val mReusableOvalRect = new RectF()

    private def drawOval(canvas: Canvas,
                         x: Float,
                         y: Float,
                         major: Float,
                         minor: Float,
                         orientation: Float,
                         paint: Paint) {
      canvas.save(Canvas.MATRIX_SAVE_FLAG)
      canvas.rotate((orientation * 180 / Math.PI).toFloat, x, y)
      mReusableOvalRect.left = x - minor / 2
      mReusableOvalRect.right = x + minor / 2
      mReusableOvalRect.top = y - major / 2
      mReusableOvalRect.bottom = y + major / 2
      canvas.drawOval(mReusableOvalRect, paint)
      canvas.restore()
    }

    private def drawSplat(canvas: Canvas,
                          x: Float,
                          y: Float,
                          orientation: Float,
                          distance: Float,
                          tilt: Float,
                          paint: Paint) {
      val z: Float = distance * 2 + 10
      val nx: Float = (Math.sin(orientation) * Math.sin(tilt)).asInstanceOf[Float]
      val ny: Float = (-Math.cos(orientation) * Math.sin(tilt)).asInstanceOf[Float]
      val nz: Float = Math.cos(tilt).asInstanceOf[Float]
      if (nz < 0.05) {
        return
      }
      val cd: Float = z / nz
      val cx: Float = nx * cd
      val cy: Float = ny * cd
      for (i <- 0 until SPLAT_VECTORS) {
        val direction: Double = mRandom.nextDouble * Math.PI * 2
        val dispersion: Double = mRandom.nextGaussian * 0.2
        var vx: Double = Math.cos(direction) * dispersion
        var vy: Double = Math.sin(direction) * dispersion
        var vz: Double = 1
        var temp: Double = vy
        vy = temp * Math.cos(tilt) - vz * Math.sin(tilt)
        vz = temp * Math.sin(tilt) + vz * Math.cos(tilt)
        temp = vx
        vx = temp * Math.cos(orientation) - vy * Math.sin(orientation)
        vy = temp * Math.sin(orientation) + vy * Math.cos(orientation)
        if (vz < 0.05) {
          //continue
        }
        val pd = (z / vz).toFloat
        val px = (vx * pd).toFloat
        val py = (vy * pd).toFloat
        mCanvas.drawCircle(x + px - cx, y + py - cy, 1.0f, paint)
      }
    }
  }
}
