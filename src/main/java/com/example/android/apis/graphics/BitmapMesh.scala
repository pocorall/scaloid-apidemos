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

import com.example.android.apis.R
import android.content.Context
import android.graphics._
import android.os.Bundle
import android.view._
import android.util.FloatMath

object BitmapMesh {

  private final val WIDTH: Int = 20
  private final val HEIGHT: Int = 20
  private final val COUNT: Int = (WIDTH + 1) * (HEIGHT + 1)

  private final var mBitmap: Bitmap = null
  private final val mVerts: Array[Float] = new Array[Float](COUNT * 2)
  private final val mOrig: Array[Float] = new Array[Float](COUNT * 2)
  private final val mMatrix: Matrix = new Matrix
  private final val mInverse: Matrix = new Matrix
  private var mLastWarpX: Int = -9999
  private var mLastWarpY: Int = 0

  private def setXY(array: Array[Float], index: Int, x: Float, y: Float) {
    array(index * 2 + 0) = x
    array(index * 2 + 1) = y
  }



  private class SampleView(context: Context)  extends View(context) {
      setFocusable(true)
      mBitmap = BitmapFactory.decodeResource(getResources, R.drawable.beach)
      val w: Float = mBitmap.getWidth
      val h: Float = mBitmap.getHeight
      var index: Int = 0

      var y: Int = 0
      while (y <= HEIGHT) {
        {
          val fy: Float = h * y / HEIGHT

          var x: Int = 0
          while (x <= WIDTH) {
            {
              val fx: Float = w * x / WIDTH
              setXY(mVerts, index, fx, fy)
              setXY(mOrig, index, fx, fy)
              index += 1
            }
            ({
              x += 1; x - 1
            })
          }

        }
        ({
          y += 1; y - 1
        })
      }

      mMatrix.setTranslate(10, 10)
      mMatrix.invert(mInverse)


    protected override def onDraw(canvas: Canvas) {
      canvas.drawColor(0xFFCCCCCC)
      canvas.concat(mMatrix)
      canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT, mVerts, 0, null, 0, null)
    }

    private def warp(cx: Float, cy: Float) {
      val K: Float = 10000
      val src: Array[Float] = mOrig
      val dst: Array[Float] = mVerts
      var i: Int = 0
      while (i < COUNT * 2) {
        val x: Float = src(i + 0)
        val y: Float = src(i + 1)
        val dx: Float = cx - x
        val dy: Float = cy - y
        val dd: Float = dx * dx + dy * dy
        val d: Float = FloatMath.sqrt(dd)
        var pull: Float = K / (dd + 0.000001f)
        pull /= (d + 0.000001f)
        if (pull >= 1) {
          dst(i + 0) = cx
          dst(i + 1) = cy
        }
        else {
          dst(i + 0) = x + dx * pull
          dst(i + 1) = y + dy * pull
        }
        i += 2
      }
    }

    override def onTouchEvent(event: MotionEvent): Boolean = {
      val pt: Array[Float] = Array(event.getX, event.getY)
      mInverse.mapPoints(pt)
      val x: Int = pt(0).asInstanceOf[Int]
      val y: Int = pt(1).asInstanceOf[Int]
      if (mLastWarpX != x || mLastWarpY != y) {
        mLastWarpX = x
        mLastWarpY = y
        warp(pt(0), pt(1))
        invalidate
      }
      return true
    }


  }

}

class BitmapMesh extends GraphicsActivity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(new BitmapMesh.SampleView(this))
  }
}