package com.example.android.apis.graphics

import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view._
import java.nio.IntBuffer
import java.nio.ShortBuffer
import BitmapPixels._
//remove if not needed
import scala.collection.JavaConversions._

object BitmapPixels {

  object SampleView {

    private def getR32(c: Int): Int = (c >> 0) & 0xFF

    private def getG32(c: Int): Int = (c >> 8) & 0xFF

    private def getB32(c: Int): Int = (c >> 16) & 0xFF

    private def getA32(c: Int): Int = (c >> 24) & 0xFF

    private def pack8888(r: Int,
                         g: Int,
                         b: Int,
                         a: Int): Int = {
      (r << 0) | (g << 8) | (b << 16) | (a << 24)
    }

    private def pack565(r: Int, g: Int, b: Int): Short = {
      ((r << 11) | (g << 5) | (b << 0)).toShort
    }

    private def pack4444(r: Int,
                         g: Int,
                         b: Int,
                         a: Int): Short = {
      ((a << 0) | (b << 4) | (g << 8) | (r << 12)).toShort
    }

    private def mul255(c: Int, a: Int): Int = {
      val prod = c * a + 128
      (prod + (prod >> 8)) >> 8
    }

    private def premultiplyColor(c: Int): Int = {
      var r = Color.red(c)
      var g = Color.green(c)
      var b = Color.blue(c)
      val a = Color.alpha(c)
      r = mul255(r, a)
      g = mul255(g, a)
      b = mul255(b, a)
      pack8888(r, g, b, a)
    }

    private def makeRamp(from: Int,
                         to: Int,
                         n: Int,
                         ramp8888: Array[Int],
                         ramp565: Array[Short],
                         ramp4444: Array[Short]) {
      var r = getR32(from) << 23
      var g = getG32(from) << 23
      var b = getB32(from) << 23
      var a = getA32(from) << 23
      val dr = ((getR32(to) << 23) - r) / (n - 1)
      val dg = ((getG32(to) << 23) - g) / (n - 1)
      val db = ((getB32(to) << 23) - b) / (n - 1)
      val da = ((getA32(to) << 23) - a) / (n - 1)
      for (i <- 0 until n) {
        ramp8888(i) = pack8888(r >> 23, g >> 23, b >> 23, a >> 23)
        ramp565(i) = pack565(r >> (23 + 3), g >> (23 + 2), b >> (23 + 3))
        ramp4444(i) = pack4444(r >> (23 + 4), g >> (23 + 4), b >> (23 + 4), a >> (23 + 4))
        r += dr
        g += dg
        b += db
        a += da
      }
    }

    private def makeBuffer(src: Array[Int], n: Int): IntBuffer = {
      val dst = IntBuffer.allocate(n * n)
      for (i <- 0 until n) {
        dst.put(src)
      }
      dst.rewind()
      dst
    }

    private def makeBuffer(src: Array[Short], n: Int): ShortBuffer = {
      val dst = ShortBuffer.allocate(n * n)
      for (i <- 0 until n) {
        dst.put(src)
      }
      dst.rewind()
      dst
    }
  }

  private class SampleView(context: Context) extends View(context) {
    val N = 100

    private var mBitmap1: Bitmap = Bitmap.createBitmap(N, N, Bitmap.Config.ARGB_8888)

    private var mBitmap2: Bitmap = Bitmap.createBitmap(N, N, Bitmap.Config.RGB_565)

    private var mBitmap3: Bitmap = Bitmap.createBitmap(N, N, Bitmap.Config.ARGB_4444)

    setFocusable(true)

    val data8888 = Array.ofDim[Int](N)

    val data565 = Array.ofDim[Short](N)

    val data4444 = Array.ofDim[Short](N)

    SampleView.makeRamp(SampleView.premultiplyColor(Color.RED), SampleView.premultiplyColor(Color.GREEN), N, data8888, data565, data4444)

    mBitmap1.copyPixelsFromBuffer(SampleView.makeBuffer(data8888, N))

    mBitmap2.copyPixelsFromBuffer(SampleView.makeBuffer(data565, N))

    mBitmap3.copyPixelsFromBuffer(SampleView.makeBuffer(data4444, N))

    protected override def onDraw(canvas: Canvas) {
      canvas.drawColor(0xFFCCCCCC)
      var y = 10
      canvas.drawBitmap(mBitmap1, 10, y, null)
      y += mBitmap1.getHeight + 10
      canvas.drawBitmap(mBitmap2, 10, y, null)
      y += mBitmap2.getHeight + 10
      canvas.drawBitmap(mBitmap3, 10, y, null)
    }
  }
}

class BitmapPixels extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
