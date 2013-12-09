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

import android.content.Context
import android.graphics._
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes._
import android.os.Bundle
import android.view._

object ShapeDrawable1 {

  private var mDrawables: Array[ShapeDrawable] = null

  private def makeSweep: Shader = {
    return new SweepGradient(150, 25, Array[Int](0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFF0000), null)
  }

  private def makeLinear: Shader = {
    return new LinearGradient(0, 0, 50, 50, Array[Int](0xFFFF0000, 0xFF00FF00, 0xFF0000FF), null, Shader.TileMode.MIRROR)
  }

  private def makeTiling: Shader = {
    val pixels: Array[Int] = Array[Int](0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0)
    val bm: Bitmap = Bitmap.createBitmap(pixels, 2, 2, Bitmap.Config.ARGB_8888)
    return new BitmapShader(bm, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
  }

  private class MyShapeDrawable(s: Shape) extends ShapeDrawable(s) {
    private var mStrokePaint: Paint = new Paint(Paint.ANTI_ALIAS_FLAG)
    mStrokePaint.setStyle(Paint.Style.STROKE)

    def getStrokePaint: Paint = {
      return mStrokePaint
    }

    protected override def onDraw(s: Shape, c: Canvas, p: Paint) {
      s.draw(c, p)
      s.draw(c, mStrokePaint)
    }
  }


  private class SampleView(context: Context) extends View(context) {
    setFocusable(true)
    val outerR: Array[Float] = Array[Float](12, 12, 12, 12, 0, 0, 0, 0)
    val inset: RectF = new RectF(6, 6, 6, 6)
    val innerR: Array[Float] = Array[Float](12, 12, 0, 0, 12, 12, 0, 0)
    val path: Path = new Path
    path.moveTo(50, 0)
    path.lineTo(0, 50)
    path.lineTo(50, 100)
    path.lineTo(100, 50)
    path.close
    mDrawables = new Array[ShapeDrawable](7)
    mDrawables(0) = new ShapeDrawable(new RectShape)
    mDrawables(1) = new ShapeDrawable(new OvalShape)
    mDrawables(2) = new ShapeDrawable(new RoundRectShape(outerR, null, null))
    mDrawables(3) = new ShapeDrawable(new RoundRectShape(outerR, inset, null))
    mDrawables(4) = new ShapeDrawable(new RoundRectShape(outerR, inset, innerR))
    mDrawables(5) = new ShapeDrawable(new PathShape(path, 100, 100))
    mDrawables(6) = new ShapeDrawable1.MyShapeDrawable(new ArcShape(45, -270))
    mDrawables(0).getPaint.setColor(0xFFFF0000)
    mDrawables(1).getPaint.setColor(0xFF00FF00)
    mDrawables(2).getPaint.setColor(0xFF0000FF)
    mDrawables(3).getPaint.setShader(makeSweep)
    mDrawables(4).getPaint.setShader(makeLinear)
    mDrawables(5).getPaint.setShader(makeTiling)
    mDrawables(6).getPaint.setColor(0x88FF8844)
    val pe: PathEffect = new DiscretePathEffect(10, 4)
    val pe2: PathEffect = new CornerPathEffect(4)
    mDrawables(3).getPaint.setPathEffect(new ComposePathEffect(pe2, pe))
    val msd: ShapeDrawable1.MyShapeDrawable = mDrawables(6).asInstanceOf[ShapeDrawable1.MyShapeDrawable]
    msd.getStrokePaint.setStrokeWidth(4)


    protected override def onDraw(canvas: Canvas) {
      val x: Int = 10
      var y: Int = 10
      val width: Int = 300
      val height: Int = 50
      for (dr <- mDrawables) {
        dr.setBounds(x, y, x + width, y + height)
        dr.draw(canvas)
        y += height + 5
      }
    }
  }

}

class ShapeDrawable1 extends GraphicsActivity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new ShapeDrawable1.SampleView(this))
  }
}