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

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.CharBuffer
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10

/**
 * A 2D rectangular mesh. Can be drawn textured or untextured.
 *
 */
class Grid {
  private var mVertexBuffer: FloatBuffer = null
  private var mTexCoordBuffer: FloatBuffer = null
  private var mIndexBuffer: CharBuffer = null
  private var mW: Int = 0
  private var mH: Int = 0
  private var mIndexCount: Int = 0

  def this(w: Int, h: Int) {
    this()
    if (w < 0 || w >= 65536) {
      throw new IllegalArgumentException("w")
    }
    if (h < 0 || h >= 65536) {
      throw new IllegalArgumentException("h")
    }
    if (w * h >= 65536) {
      throw new IllegalArgumentException("w * h >= 65536")
    }
    mW = w
    mH = h
    val size: Int = w * h
    val FLOAT_SIZE: Int = 4
    val CHAR_SIZE: Int = 2
    mVertexBuffer = ByteBuffer.allocateDirect(FLOAT_SIZE * size * 3).order(ByteOrder.nativeOrder).asFloatBuffer
    mTexCoordBuffer = ByteBuffer.allocateDirect(FLOAT_SIZE * size * 2).order(ByteOrder.nativeOrder).asFloatBuffer
    val quadW: Int = mW - 1
    val quadH: Int = mH - 1
    val quadCount: Int = quadW * quadH
    val indexCount: Int = quadCount * 6
    mIndexCount = indexCount
    mIndexBuffer = ByteBuffer.allocateDirect(CHAR_SIZE * indexCount).order(ByteOrder.nativeOrder).asCharBuffer
      var i: Int = 0
        var y: Int = 0
        while (y < quadH) {
          {
            {
              var x: Int = 0
              while (x < quadW) {
                {
                  val a: Char = (y * mW + x).asInstanceOf[Char]
                  val b: Char = (y * mW + x + 1).asInstanceOf[Char]
                  val c: Char = ((y + 1) * mW + x).asInstanceOf[Char]
                  val d: Char = ((y + 1) * mW + x + 1).asInstanceOf[Char]
                  mIndexBuffer.put(({
                    i += 1; i - 1
                  }), a)
                  mIndexBuffer.put(({
                    i += 1; i - 1
                  }), b)
                  mIndexBuffer.put(({
                    i += 1; i - 1
                  }), c)
                  mIndexBuffer.put(({
                    i += 1; i - 1
                  }), b)
                  mIndexBuffer.put(({
                    i += 1; i - 1
                  }), c)
                  mIndexBuffer.put(({
                    i += 1; i - 1
                  }), d)
                }
                ({
                  x += 1; x - 1
                })
              }
            }
          }
          ({
            y += 1; y - 1
          })
        }
  }

  private[spritetext] def set(i: Int, j: Int, x: Float, y: Float, z: Float, u: Float, v: Float) {
    if (i < 0 || i >= mW) {
      throw new IllegalArgumentException("i")
    }
    if (j < 0 || j >= mH) {
      throw new IllegalArgumentException("j")
    }
    val index: Int = mW * j + i
    val posIndex: Int = index * 3
    mVertexBuffer.put(posIndex, x)
    mVertexBuffer.put(posIndex + 1, y)
    mVertexBuffer.put(posIndex + 2, z)
    val texIndex: Int = index * 2
    mTexCoordBuffer.put(texIndex, u)
    mTexCoordBuffer.put(texIndex + 1, v)
  }

  def draw(gl: GL10, useTexture: Boolean) {
    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer)
    if (useTexture) {
      gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
      gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoordBuffer)
      gl.glEnable(GL10.GL_TEXTURE_2D)
    }
    else {
      gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
      gl.glDisable(GL10.GL_TEXTURE_2D)
    }
    gl.glDrawElements(GL10.GL_TRIANGLES, mIndexCount, GL10.GL_UNSIGNED_SHORT, mIndexBuffer)
    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)
  }
}