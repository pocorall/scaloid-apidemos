package com.example.android.apis.graphics

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Xfermode
import android.os.Bundle
import android.view.View
import Xfermodes._
//remove if not needed
import scala.collection.JavaConversions._

object Xfermodes {

  def makeDst(w: Int, h: Int): Bitmap = {
    val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    val c = new Canvas(bm)
    val p = new Paint(Paint.ANTI_ALIAS_FLAG)
    p.setColor(0xFFFFCC44)
    c.drawOval(new RectF(0, 0, w * 3 / 4, h * 3 / 4), p)
    bm
  }

  def makeSrc(w: Int, h: Int): Bitmap = {
    val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    val c = new Canvas(bm)
    val p = new Paint(Paint.ANTI_ALIAS_FLAG)
    p.setColor(0xFF66AAFF)
    c.drawRect(w / 3, h / 3, w * 19 / 20, h * 19 / 20, p)
    bm
  }


  private val W = 64

  private val H = 64

  private val ROW_MAX = 4

  private val sModes = Array(new PorterDuffXfermode(PorterDuff.Mode.CLEAR), new PorterDuffXfermode(PorterDuff.Mode.SRC), new PorterDuffXfermode(PorterDuff.Mode.DST), new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER), new PorterDuffXfermode(PorterDuff.Mode.DST_OVER), new PorterDuffXfermode(PorterDuff.Mode.SRC_IN), new PorterDuffXfermode(PorterDuff.Mode.DST_IN), new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT), new PorterDuffXfermode(PorterDuff.Mode.DST_OUT), new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP), new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP), new PorterDuffXfermode(PorterDuff.Mode.XOR), new PorterDuffXfermode(PorterDuff.Mode.DARKEN), new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN), new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY), new PorterDuffXfermode(PorterDuff.Mode.SCREEN))

  private val sLabels = Array("Clear", "Src", "Dst", "SrcOver", "DstOver", "SrcIn", "DstIn", "SrcOut", "DstOut", "SrcATop", "DstATop", "Xor", "Darken", "Lighten", "Multiply", "Screen")


  private class SampleView(context: Context) extends View(context) {

    val bm = Bitmap.createBitmap(Array(0xFFFFFFFF, 0xFFCCCCCC, 0xFFCCCCCC, 0xFFFFFFFF), 2, 2, Bitmap.Config.RGB_565)

    private var mSrcB: Bitmap = makeSrc(W, H)

    private var mDstB: Bitmap = makeDst(W, H)

    private var mBG: Shader = new BitmapShader(bm, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)

    val m = new Matrix()

    m.setScale(6, 6)

    mBG.setLocalMatrix(m)

    protected override def onDraw(canvas: Canvas) {
      canvas.drawColor(Color.WHITE)
      val labelP = new Paint(Paint.ANTI_ALIAS_FLAG)
      labelP.setTextAlign(Paint.Align.CENTER)
      val paint = new Paint()
      paint.setFilterBitmap(false)
      canvas.translate(15, 35)
      var x = 0
      var y = 0
      for (i <- 0 until sModes.length) {
        paint.setStyle(Paint.Style.STROKE)
        paint.setShader(null)
        canvas.drawRect(x - 0.5f, y - 0.5f, x + W + 0.5f, y + H + 0.5f, paint)
        paint.setStyle(Paint.Style.FILL)
        paint.setShader(mBG)
        canvas.drawRect(x, y, x + W, y + H, paint)
        val sc = canvas.saveLayer(x, y, x + W, y + H, null, Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
          Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
          Canvas.CLIP_TO_LAYER_SAVE_FLAG)
        canvas.translate(x, y)
        canvas.drawBitmap(mDstB, 0, 0, paint)
        paint.setXfermode(sModes(i))
        canvas.drawBitmap(mSrcB, 0, 0, paint)
        paint.setXfermode(null)
        canvas.restoreToCount(sc)
        canvas.drawText(sLabels(i), x + W / 2, y - labelP.getTextSize / 2, labelP)
        x += W + 10
        if ((i % ROW_MAX) == ROW_MAX - 1) {
          x = 0
          y += H + 30
        }
      }
    }
  }
}

class Xfermodes extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
