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
package com.example.android.apis.graphics.spritetext

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Paint.Style
import android.graphics.drawable.Drawable
import android.opengl.GLUtils
import java.util.ArrayList
import javax.microedition.khronos.opengles.GL10
import javax.microedition.khronos.opengles.GL11
import javax.microedition.khronos.opengles.GL11Ext

/**
 * An OpenGL text label maker.
 *
 *
 * OpenGL labels are implemented by creating a Bitmap, drawing all the labels
 * into the Bitmap, converting the Bitmap into an Alpha texture, and drawing
 * portions of the texture using glDrawTexiOES.
 *
 * The benefits of this approach are that the labels are drawn using the high
 * quality anti-aliased font rasterizer, full character set support, and all the
 * text labels are stored on a single texture, which makes it faster to use.
 *
 * The drawbacks are that you can only have as many labels as will fit onto one
 * texture, and you have to recreate the whole texture if any label text
 * changes.
 *
 */
object LabelMaker {


  private class Label {
    def this(width: Float, height: Float, baseLine: Float, cropU: Int, cropV: Int, cropW: Int, cropH: Int) {
      this()
      this.width = width
      this.height = height
      this.baseline = baseLine
      val crop: Array[Int] = new Array[Int](4)
      crop(0) = cropU
      crop(1) = cropV
      crop(2) = cropW
      crop(3) = cropH
      mCrop = crop
    }

    var width: Float = .0f
    var height: Float = .0f
    var baseline: Float = .0f
    var mCrop: Array[Int] = null
  }

}

class LabelMaker {
  private final val STATE_NEW: Int = 0
  private final val STATE_INITIALIZED: Int = 1
  private final val STATE_ADDING: Int = 2
  private final val STATE_DRAWING: Int = 3

  private var mStrikeWidth: Int = 0
  private var mStrikeHeight: Int = 0
  private var mFullColor: Boolean = false
  private var mBitmap: Bitmap = null
  private var mCanvas: Canvas = null
  private var mClearPaint: Paint = null
  private var mTextureID: Int = 0
  private var mTexelWidth: Float = .0f
  private var mTexelHeight: Float = .0f
  private var mU: Int = 0
  private var mV: Int = 0
  private var mLineHeight: Int = 0
  private var mLabels: ArrayList[LabelMaker.Label] = new ArrayList[LabelMaker.Label]
  private var mState: Int = 0

  /**
   * Create a label maker
   * or maximum compatibility with various OpenGL ES implementations,
   * the strike width and height must be powers of two,
   * We want the strike width to be at least as wide as the widest window.
   *
   * @param fullColor true if we want a full color backing store (4444),
   *                  otherwise we generate a grey L8 backing store.
   * @param strikeWidth width of strike
   * @param strikeHeight height of strike
   */
  def this(fullColor: Boolean, strikeWidth: Int, strikeHeight: Int) {
    this()
    mFullColor = fullColor
    mStrikeWidth = strikeWidth
    mStrikeHeight = strikeHeight
    mTexelWidth = (1.0 / mStrikeWidth).asInstanceOf[Float]
    mTexelHeight = (1.0 / mStrikeHeight).asInstanceOf[Float]
    mClearPaint = new Paint
    mClearPaint.setARGB(0, 0, 0, 0)
    mClearPaint.setStyle(Style.FILL)
    mState = STATE_NEW
  }

  /**
   * Call to initialize the class.
   * Call whenever the surface has been created.
   *
   * @param gl
   */
  def initialize(gl: GL10) {
    mState = STATE_INITIALIZED
    val textures: Array[Int] = new Array[Int](1)
    gl.glGenTextures(1, textures, 0)
    mTextureID = textures(0)
    gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID)
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST)
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST)
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE)
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE)
    gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE)
  }

  /**
   * Call when the surface has been destroyed
   */
  def shutdown(gl: GL10) {
    if (gl != null) {
      if (mState > STATE_NEW) {
        val textures: Array[Int] = new Array[Int](1)
        textures(0) = mTextureID
        gl.glDeleteTextures(1, textures, 0)
        mState = STATE_NEW
      }
    }
  }

  /**
   * Call before adding labels. Clears out any existing labels.
   *
   * @param gl
   */
  def beginAdding(gl: GL10) {
    checkState(STATE_INITIALIZED, STATE_ADDING)
    mLabels.clear
    mU = 0
    mV = 0
    mLineHeight = 0
    val config: Bitmap.Config = if (mFullColor) Bitmap.Config.ARGB_4444 else Bitmap.Config.ALPHA_8
    mBitmap = Bitmap.createBitmap(mStrikeWidth, mStrikeHeight, config)
    mCanvas = new Canvas(mBitmap)
    mBitmap.eraseColor(0)
  }

  /**
   * Call to add a label
   *
   * @param gl
   * @param text the text of the label
   * @param textPaint the paint of the label
   * @return the id of the label, used to measure and draw the label
   */
  def add(gl: GL10, text: String, textPaint: Paint): Int = {
    return add(gl, null, text, textPaint)
  }

  /**
   * Call to add a label
   *
   * @param gl
   * @param text the text of the label
   * @param textPaint the paint of the label
   * @return the id of the label, used to measure and draw the label
   */
  def add(gl: GL10, background: Drawable, text: String, textPaint: Paint): Int = {
    return add(gl, background, text, textPaint, 0, 0)
  }

  /**
   * Call to add a label
   * @return the id of the label, used to measure and draw the label
   */
  def add(gl: GL10, drawable: Drawable, minWidth: Int, minHeight: Int): Int = {
    return add(gl, drawable, null, null, minWidth, minHeight)
  }

  /**
   * Call to add a label
   *
   * @param gl
   * @param text the text of the label
   * @param textPaint the paint of the label
   * @return the id of the label, used to measure and draw the label
   */
  def add(gl: GL10, background: Drawable, text: String, textPaint: Paint, minWidth: Int, minHeight: Int): Int = {
    checkState(STATE_ADDING, STATE_ADDING)

    var minWidthVar = minWidth
    var minHeightVar =  minHeight

    val drawBackground: Boolean = background != null
    val drawText: Boolean = (text != null) && (textPaint != null)
    val padding: Rect = new Rect
    if (drawBackground) {
      background.getPadding(padding)
      minWidthVar = Math.max(minWidthVar, background.getMinimumWidth)
      minHeightVar = Math.max(minHeightVar, background.getMinimumHeight)
    }
    var ascent: Int = 0
    var descent: Int = 0
    var measuredTextWidth: Int = 0
    if (drawText) {
      ascent = Math.ceil(-textPaint.ascent).asInstanceOf[Int]
      descent = Math.ceil(textPaint.descent).asInstanceOf[Int]
      measuredTextWidth = Math.ceil(textPaint.measureText(text)).asInstanceOf[Int]
    }
    val textHeight: Int = ascent + descent
    val textWidth: Int = Math.min(mStrikeWidth, measuredTextWidth)
    val padHeight: Int = padding.top + padding.bottom
    val padWidth: Int = padding.left + padding.right
    val height: Int = Math.max(minHeightVar, textHeight + padHeight)
    var width: Int = Math.max(minWidthVar, textWidth + padWidth)
    val effectiveTextHeight: Int = height - padHeight
    val effectiveTextWidth: Int = width - padWidth
    val centerOffsetHeight: Int = (effectiveTextHeight - textHeight) / 2
    val centerOffsetWidth: Int = (effectiveTextWidth - textWidth) / 2
    var u: Int = mU
    var v: Int = mV
    var lineHeight: Int = mLineHeight
    if (width > mStrikeWidth) {
      width = mStrikeWidth
    }
    if (u + width > mStrikeWidth) {
      u = 0
      v += lineHeight
      lineHeight = 0
    }
    lineHeight = Math.max(lineHeight, height)
    if (v + lineHeight > mStrikeHeight) {
      throw new IllegalArgumentException("Out of texture space.")
    }
    val u2: Int = u + width
    val vBase: Int = v + ascent
    val v2: Int = v + height
    if (drawBackground) {
      background.setBounds(u, v, u + width, v + height)
      background.draw(mCanvas)
    }
    if (drawText) {
      mCanvas.drawText(text, u + padding.left + centerOffsetWidth, vBase + padding.top + centerOffsetHeight, textPaint)
    }
    mU = u + width
    mV = v
    mLineHeight = lineHeight
    mLabels.add(new LabelMaker.Label(width, height, ascent, u, v + height, width, -height))
    return mLabels.size - 1
  }

  /**
   * Call to end adding labels. Must be called before drawing starts.
   *
   * @param gl
   */
  def endAdding(gl: GL10) {
    checkState(STATE_ADDING, STATE_INITIALIZED)
    gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID)
    GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0)
    mBitmap.recycle
    mBitmap = null
    mCanvas = null
  }

  /**
   * Get the width in pixels of a given label.
   *
   * @param labelID
   * @return the width in pixels
   */
  def getWidth(labelID: Int): Float = {
    return mLabels.get(labelID).width
  }

  /**
   * Get the height in pixels of a given label.
   *
   * @param labelID
   * @return the height in pixels
   */
  def getHeight(labelID: Int): Float = {
    return mLabels.get(labelID).height
  }

  /**
   * Get the baseline of a given label. That's how many pixels from the top of
   * the label to the text baseline. (This is equivalent to the negative of
   * the label's paint's ascent.)
   *
   * @param labelID
   * @return the baseline in pixels.
   */
  def getBaseline(labelID: Int): Float = {
    return mLabels.get(labelID).baseline
  }

  /**
   * Begin drawing labels. Sets the OpenGL state for rapid drawing.
   *
   * @param gl
   * @param viewWidth
   * @param viewHeight
   */
  def beginDrawing(gl: GL10, viewWidth: Float, viewHeight: Float) {
    checkState(STATE_INITIALIZED, STATE_DRAWING)
    gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID)
    gl.glShadeModel(GL10.GL_FLAT)
    gl.glEnable(GL10.GL_BLEND)
    gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA)
    gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000)
    gl.glMatrixMode(GL10.GL_PROJECTION)
    gl.glPushMatrix
    gl.glLoadIdentity
    gl.glOrthof(0.0f, viewWidth, 0.0f, viewHeight, 0.0f, 1.0f)
    gl.glMatrixMode(GL10.GL_MODELVIEW)
    gl.glPushMatrix
    gl.glLoadIdentity
    gl.glTranslatef(0.375f, 0.375f, 0.0f)
  }

  /**
   * Draw a given label at a given x,y position, expressed in pixels, with the
   * lower-left-hand-corner of the view being (0,0).
   *
   * @param gl
   * @param x
   * @param y
   * @param labelID
   */
  def draw(gl: GL10, x: Float, y: Float, labelID: Int) {
    checkState(STATE_DRAWING, STATE_DRAWING)
    val label: LabelMaker.Label = mLabels.get(labelID)
    gl.glEnable(GL10.GL_TEXTURE_2D)
    (gl.asInstanceOf[GL11]).glTexParameteriv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, label.mCrop, 0)
    (gl.asInstanceOf[GL11Ext]).glDrawTexiOES(x.asInstanceOf[Int], y.asInstanceOf[Int], 0, label.width.asInstanceOf[Int], label.height.asInstanceOf[Int])
  }

  /**
   * Ends the drawing and restores the OpenGL state.
   *
   * @param gl
   */
  def endDrawing(gl: GL10) {
    checkState(STATE_DRAWING, STATE_INITIALIZED)
    gl.glDisable(GL10.GL_BLEND)
    gl.glMatrixMode(GL10.GL_PROJECTION)
    gl.glPopMatrix
    gl.glMatrixMode(GL10.GL_MODELVIEW)
    gl.glPopMatrix
  }

  private def checkState(oldState: Int, newState: Int) {
    if (mState != oldState) {
      throw new IllegalArgumentException("Can't call this method now.")
    }
    mState = newState
  }


}