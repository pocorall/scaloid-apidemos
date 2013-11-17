package com.example.android.apis.graphics

import com.example.android.apis.R
import android.content.Context
import android.graphics._
import android.graphics.drawable._
import android.os.Bundle
import android.view._
import java.io.InputStream
import java.io.ByteArrayOutputStream
import BitmapDecode._
//remove if not needed
import scala.collection.JavaConversions._

object BitmapDecode {

  object SampleView {

    private val DECODE_STREAM = true

    private def streamToBytes(is: InputStream): Array[Byte] = {
      val os = new ByteArrayOutputStream(1024)
      val buffer = Array.ofDim[Byte](1024)
      var len: Int = 0
      try {
        while ((({
          len = is.read(buffer); len
        })) >= 0) {
          os.write(buffer, 0, len)
        }
      } catch {
        case e: java.io.IOException =>
      }
      os.toByteArray()
    }
  }

  private class SampleView(context: Context) extends View(context) {



    private var mMovie: Movie = _

    private var mMovieStart: Long = _

    setFocusable(true)

    var is: java.io.InputStream = null

    is = context.getResources.openRawResource(R.drawable.beach)

    val opts = new BitmapFactory.Options()

    var bm: Bitmap = null

    opts.inJustDecodeBounds = true

    bm = BitmapFactory.decodeStream(is, null, opts)

    opts.inJustDecodeBounds = false

    opts.inSampleSize = 4

    bm = BitmapFactory.decodeStream(is, null, opts)

    is = context.getResources.openRawResource(R.drawable.frog)

    private var mBitmap: Bitmap = bm

    private var mBitmap2: Bitmap = BitmapFactory.decodeStream(is)

    private var mDrawable: Drawable = context.getResources.getDrawable(R.drawable.button)

    val w = mBitmap2.getWidth

    val h = mBitmap2.getHeight

    val pixels = Array.ofDim[Int](w * h)

    mBitmap2.getPixels(pixels, 0, w, 0, 0, w, h)

    mDrawable.setBounds(150, 20, 300, 100)

    private var mBitmap3: Bitmap = Bitmap.createBitmap(pixels, 0, w, w, h, Bitmap.Config.ARGB_8888)

    private var mBitmap4: Bitmap = Bitmap.createBitmap(pixels, 0, w, w, h, Bitmap.Config.ARGB_4444)

    is = context.getResources.openRawResource(R.drawable.animated_gif)

    if (SampleView.DECODE_STREAM) {
      mMovie = Movie.decodeStream(is)
    } else {
      val array = SampleView.streamToBytes(is)
      mMovie = Movie.decodeByteArray(array, 0, array.length)
    }

    protected override def onDraw(canvas: Canvas) {
      canvas.drawColor(0xFFCCCCCC)
      val p = new Paint()
      p.setAntiAlias(true)
      canvas.drawBitmap(mBitmap, 10, 10, null)
      canvas.drawBitmap(mBitmap2, 10, 170, null)
      canvas.drawBitmap(mBitmap3, 110, 170, null)
      canvas.drawBitmap(mBitmap4, 210, 170, null)
      mDrawable.draw(canvas)
      val now = android.os.SystemClock.uptimeMillis()
      if (mMovieStart == 0) {
        mMovieStart = now
      }
      if (mMovie != null) {
        var dur = mMovie.duration()
        if (dur == 0) {
          dur = 1000
        }
        val relTime = ((now - mMovieStart) % dur).toInt
        mMovie.setTime(relTime)
        mMovie.draw(canvas, getWidth - mMovie.width(), getHeight - mMovie.height())
        invalidate()
      }
    }
  }
}

class BitmapDecode extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
