/*
 * Copyright (C) 2008 The Android Open Source Project
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

import android.content.Context
import android.graphics._
import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.view.View
import java.io._

object Pictures {

  private class SampleView(context: Context) extends View(context) {

    private var mPicture: Picture = null
    private var mDrawable: Drawable = null

    setFocusable(true)
    setFocusableInTouchMode(true)
    mPicture = new Picture
    drawSomething(mPicture.beginRecording(200, 100))
    mPicture.endRecording
    mDrawable = new PictureDrawable(mPicture)

    private[graphics] def drawSomething(canvas: Canvas) {
      val p: Paint = new Paint(Paint.ANTI_ALIAS_FLAG)
      p.setColor(0x88FF0000)
      canvas.drawCircle(50, 50, 40, p)
      p.setColor(Color.GREEN)
      p.setTextSize(30)
      canvas.drawText("Pictures", 60, 60, p)
    }

    protected override def onDraw(canvas: Canvas) {
      canvas.drawColor(Color.WHITE)
      canvas.drawPicture(mPicture)
      canvas.drawPicture(mPicture, new RectF(0, 100, getWidth, 200))
      mDrawable.setBounds(0, 200, getWidth, 300)
      mDrawable.draw(canvas)
      val os: ByteArrayOutputStream = new ByteArrayOutputStream
      mPicture.writeToStream(os)
      val is: InputStream = new ByteArrayInputStream(os.toByteArray)
      canvas.translate(0, 300)
      canvas.drawPicture(Picture.createFromStream(is))
    }

  }

}

class Pictures extends GraphicsActivity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new Pictures.SampleView(this))
  }
}