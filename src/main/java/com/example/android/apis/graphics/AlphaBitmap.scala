/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.apis.graphics

import com.example.android.apis.R
import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view._
import java.io.InputStream

object AlphaBitmap {

  private var mBitmap: Bitmap = null
  private var mBitmap2: Bitmap = null
  private var mBitmap3: Bitmap = null
  private var mShader: Shader = null

  private object SampleView {
    private def drawIntoBitmap(bm: Bitmap) {
      val x: Float = bm.getWidth
      val y: Float = bm.getHeight
      val c: Canvas = new Canvas(bm)
      val p: Paint = new Paint
      p.setAntiAlias(true)
      p.setAlpha(0x80)
      c.drawCircle(x / 2, y / 2, x / 2, p)
      p.setAlpha(0x30)
      p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC))
      p.setTextSize(60)
      p.setTextAlign(Paint.Align.CENTER)
      val fm: Paint.FontMetrics = p.getFontMetrics
      c.drawText("Alpha", x / 2, (y - fm.ascent) / 2, p)
    }
  }

  private class SampleView(context: Context) extends View(context) {
    setFocusable(true)
    val is: InputStream = context.getResources.openRawResource(R.drawable.app_sample_code)
    mBitmap = BitmapFactory.decodeStream(is)
    mBitmap2 = mBitmap.extractAlpha
    mBitmap3 = Bitmap.createBitmap(200, 200, Bitmap.Config.ALPHA_8)
    SampleView.drawIntoBitmap(mBitmap3)
    mShader = new LinearGradient(0, 0, 100, 70, Array[Int](Color.RED, Color.GREEN, Color.BLUE), null, Shader.TileMode.MIRROR)

    protected override def onDraw(canvas: Canvas) {
      canvas.drawColor(Color.WHITE)
      val p: Paint = new Paint
      var y: Float = 10
      p.setColor(Color.RED)
      canvas.drawBitmap(mBitmap, 10, y, p)
      y += mBitmap.getHeight + 10
      canvas.drawBitmap(mBitmap2, 10, y, p)
      y += mBitmap2.getHeight + 10
      p.setShader(mShader)
      canvas.drawBitmap(mBitmap3, 10, y, p)
    }
  }

}

class AlphaBitmap extends GraphicsActivity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new AlphaBitmap.SampleView(this))
  }
}