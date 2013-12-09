package com.example.android.apis.graphics

import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view._
import java.io.ByteArrayOutputStream
import CreateBitmap._
//remove if not needed
import scala.collection.JavaConversions._

object CreateBitmap {

  private val WIDTH = 50

  private val HEIGHT = 50

  private val STRIDE = 64

  private def createColors(): Array[Int] = {
    val colors = Array.ofDim[Int](STRIDE * HEIGHT)
    for (y <- 0 until HEIGHT; x <- 0 until WIDTH) {
      val r = x * 255 / (WIDTH - 1)
      val g = y * 255 / (HEIGHT - 1)
      val b = 255 - Math.min(r, g)
      val a = Math.max(r, g)
      colors(y * STRIDE + x) = (a << 24) | (r << 16) | (g << 8) | b
    }
    colors
  }

  object SampleView {

    private def codec(src: Bitmap, format: Bitmap.CompressFormat, quality: Int): Bitmap = {
      val os = new ByteArrayOutputStream()
      src.compress(format, quality, os)
      val array = os.toByteArray()
      BitmapFactory.decodeByteArray(array, 0, array.length)
    }
  }

  private class SampleView(context: Context) extends View(context) {

    private var mBitmaps: Array[Bitmap] = new Array[Bitmap](6)

    private var mJPEG: Array[Bitmap] = new Array[Bitmap](mBitmaps.length)

    private var mPNG: Array[Bitmap] = new Array[Bitmap](mBitmaps.length)

    private var mColors: Array[Int] = createColors()

    private var mPaint: Paint = new Paint()

    setFocusable(true)

    val colors = mColors

    mBitmaps(0) = Bitmap.createBitmap(colors, 0, STRIDE, WIDTH, HEIGHT, Bitmap.Config.ARGB_8888)

    mBitmaps(1) = Bitmap.createBitmap(colors, 0, STRIDE, WIDTH, HEIGHT, Bitmap.Config.RGB_565)

    mBitmaps(2) = Bitmap.createBitmap(colors, 0, STRIDE, WIDTH, HEIGHT, Bitmap.Config.ARGB_4444)

    mBitmaps(3) = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888)

    mBitmaps(4) = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.RGB_565)

    mBitmaps(5) = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_4444)

    var i = 3
    while (i <= 5) {
      mBitmaps(i).setPixels(colors, 0, STRIDE, 0, 0, WIDTH, HEIGHT)
      i += 1
    }

    mPaint.setDither(true)

    for (i <- 0 until mBitmaps.length) {
      mJPEG(i) = SampleView.codec(mBitmaps(i), Bitmap.CompressFormat.JPEG, 80)
      mPNG(i) = SampleView.codec(mBitmaps(i), Bitmap.CompressFormat.PNG, 0)
    }

    protected override def onDraw(canvas: Canvas) {
      canvas.drawColor(Color.WHITE)
      for (i <- 0 until mBitmaps.length) {
        canvas.drawBitmap(mBitmaps(i), 0, 0, null)
        canvas.drawBitmap(mJPEG(i), 80, 0, null)
        canvas.drawBitmap(mPNG(i), 160, 0, null)
        canvas.translate(0, mBitmaps(i).getHeight)
      }
      canvas.drawBitmap(mColors, 0, STRIDE, 0, 0, WIDTH, HEIGHT, true, null)
      canvas.translate(0, HEIGHT)
      canvas.drawBitmap(mColors, 0, STRIDE, 0, 0, WIDTH, HEIGHT, false, mPaint)
    }
  }
}

class CreateBitmap extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
