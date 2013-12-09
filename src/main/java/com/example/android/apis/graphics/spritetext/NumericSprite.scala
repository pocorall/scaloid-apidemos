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
package com.example.android.apis.graphics.spritetext

import javax.microedition.khronos.opengles.GL10
import android.graphics.Paint

class NumericSprite {
  private final val sStrike: String = "0123456789"

  private var mLabelMaker: LabelMaker = null
  private var mText: String = null
  private var mWidth: Array[Int] = new Array[Int](10)
  private var mLabelId: Array[Int] = new Array[Int](10)

  mText = ""
  mLabelMaker = null

  def initialize(gl: GL10, paint: Paint) {
    val height: Int = roundUpPower2(paint.getFontSpacing.asInstanceOf[Int])
    val interDigitGaps: Float = 9 * 1.0f
    val width: Int = roundUpPower2((interDigitGaps + paint.measureText(sStrike)).asInstanceOf[Int])
    mLabelMaker = new LabelMaker(true, width, height)
    mLabelMaker.initialize(gl)
    mLabelMaker.beginAdding(gl)

    var i: Int = 0
    while (i < 10) {{
        val digit: String = sStrike.substring(i, i + 1)
        mLabelId(i) = mLabelMaker.add(gl, digit, paint)
        mWidth(i) = Math.ceil(mLabelMaker.getWidth(i)).asInstanceOf[Int]
      }
      ({
        i += 1; i - 1
      })
    }
    mLabelMaker.endAdding(gl)
  }

  def shutdown(gl: GL10) {
    mLabelMaker.shutdown(gl)
    mLabelMaker = null
  }

  /**
   * Find the smallest power of two >= the input value.
   * (Doesn't work for negative numbers.)
   */
  private def roundUpPower2(x: Int): Int = {
    var xVar = x
    xVar = xVar - 1
    xVar = xVar | (xVar >> 1)
    xVar = xVar | (xVar >> 2)
    xVar = xVar | (xVar >> 4)
    xVar = xVar | (xVar >> 8)
    xVar = xVar | (xVar >> 16)
    return x + 1
  }

  def setValue(value: Int) {
    mText = format(value)
  }

  def draw(gl: GL10, x: Float, y: Float, viewWidth: Float, viewHeight: Float) {
    val length: Int = mText.length
    mLabelMaker.beginDrawing(gl, viewWidth, viewHeight)

    var xVar: Float = x
    var i: Int = 0
    while (i < length) {
      {
        val c: Char = mText.charAt(i)
        val digit: Int = c - '0'
        mLabelMaker.draw(gl, xVar, y, mLabelId(digit))
        xVar += mWidth(digit)
      }
      ({
        i += 1; i - 1
      })
    }
    mLabelMaker.endDrawing(gl)
  }

  def width: Float = {
    var width: Float = 0.0f
    val length: Int = mText.length
    var i: Int = 0
    while (i < length) {
      {
        val c: Char = mText.charAt(i)
        width += mWidth(c - '0')
      }
      ({
        i += 1; i - 1
      })
    }
    return width
  }

  private def format(value: Int): String = {
    return Integer.toString(value)
  }
}