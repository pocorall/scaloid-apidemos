package com.example.android.apis.graphics

import com.example.android.apis.R
import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view.View
import ColorMatrixSample._
//remove if not needed
import scala.collection.JavaConversions._

object ColorMatrixSample {

  object SampleView {

    private def setTranslate(cm: ColorMatrix,
                             dr: Float,
                             dg: Float,
                             db: Float,
                             da: Float) {
      cm.set(Array(2, 0, 0, 0, dr, 0, 2, 0, 0, dg, 0, 0, 2, 0, db, 0, 0, 0, 1, da))
    }

    private def setContrast(cm: ColorMatrix, contrast: Float) {
      val scale = contrast + 1.0f
      val translate = (-.5f * scale + .5f) * 255.0f
      cm.set(Array(scale, 0, 0, 0, translate, 0, scale, 0, 0, translate, 0, 0, scale, 0, translate, 0, 0, 0, 1, 0))
    }

    private def setContrastTranslateOnly(cm: ColorMatrix, contrast: Float) {
      val scale = contrast + 1.0f
      val translate = (-.5f * scale + .5f) * 255.0f
      cm.set(Array(1, 0, 0, 0, translate, 0, 1, 0, 0, translate, 0, 0, 1, 0, translate, 0, 0, 0, 1, 0))
    }

    private def setContrastScaleOnly(cm: ColorMatrix, contrast: Float) {
      val scale = contrast + 1.0f
      val translate = (-.5f * scale + .5f) * 255.0f
      cm.set(Array(scale, 0, 0, 0, 0, 0, scale, 0, 0, 0, 0, 0, scale, 0, 0, 0, 0, 0, 1, 0))
    }
  }

  private class SampleView(context: Context) extends View(context) {

    private var mPaint: Paint = new Paint(Paint.ANTI_ALIAS_FLAG)

    private var mBitmap: Bitmap = BitmapFactory.decodeResource(context.getResources, R.drawable.balloons)

    private var mAngle: Float = _

    protected override def onDraw(canvas: Canvas) {
      val paint = mPaint
      val x = 20
      val y = 20
      canvas.drawColor(Color.WHITE)
      paint.setColorFilter(null)
      canvas.drawBitmap(mBitmap, x, y, paint)
      val cm = new ColorMatrix()
      mAngle += 2
      if (mAngle > 180) {
        mAngle = 0
      }
      val contrast = mAngle / 180.0f
      SampleView.setContrast(cm, contrast)
      paint.setColorFilter(new ColorMatrixColorFilter(cm))
      canvas.drawBitmap(mBitmap, x + mBitmap.getWidth + 10, y, paint)
      SampleView.setContrastScaleOnly(cm, contrast)
      paint.setColorFilter(new ColorMatrixColorFilter(cm))
      canvas.drawBitmap(mBitmap, x, y + mBitmap.getHeight + 10, paint)
      SampleView.setContrastTranslateOnly(cm, contrast)
      paint.setColorFilter(new ColorMatrixColorFilter(cm))
      canvas.drawBitmap(mBitmap, x, y + 2 * (mBitmap.getHeight + 10), paint)
      invalidate()
    }
  }
}

class ColorMatrixSample extends GraphicsActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new SampleView(this))
  }
}
