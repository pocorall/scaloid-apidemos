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

import android.opengl.Matrix
import java.nio.FloatBuffer
import java.nio.IntBuffer

/**
 * A matrix stack, similar to OpenGL ES's internal matrix stack.
 */
class MatrixStack {
  val DEFAULT_MAX_DEPTH: Int = 32
  val MATRIX_SIZE: Int = 16

  var mMatrix: Array[Float] = null
  var mTop: Int = 0
  var mTemp: Array[Float] = null

  mMatrix = new Array[Float](DEFAULT_MAX_DEPTH * MATRIX_SIZE)
  mTemp = new Array[Float](MATRIX_SIZE * 2)
  glLoadIdentity


  def glFrustumf(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float) {
    Matrix.frustumM(mMatrix, mTop, left, right, bottom, top, near, far)
  }

  def glFrustumx(left: Int, right: Int, bottom: Int, top: Int, near: Int, far: Int) {
    glFrustumf(fixedToFloat(left), fixedToFloat(right), fixedToFloat(bottom), fixedToFloat(top), fixedToFloat(near), fixedToFloat(far))
  }

  def glLoadIdentity {
    Matrix.setIdentityM(mMatrix, mTop)
  }

  def glLoadMatrixf(m: Array[Float], offset: Int) {
    System.arraycopy(m, offset, mMatrix, mTop, MATRIX_SIZE)
  }

  def glLoadMatrixf(m: FloatBuffer) {
    m.get(mMatrix, mTop, MATRIX_SIZE)
  }

  def glLoadMatrixx(m: Array[Int], offset: Int) {
    {
      var i: Int = 0
      while (i < MATRIX_SIZE) {
        {
          mMatrix(mTop + i) = fixedToFloat(m(offset + i))
        }
        ({
          i += 1; i - 1
        })
      }
    }
  }

  def glLoadMatrixx(m: IntBuffer) {
    {
      var i: Int = 0
      while (i < MATRIX_SIZE) {
        {
          mMatrix(mTop + i) = fixedToFloat(m.get)
        }
        ({
          i += 1; i - 1
        })
      }
    }
  }

  def glMultMatrixf(m: Array[Float], offset: Int) {
    System.arraycopy(mMatrix, mTop, mTemp, 0, MATRIX_SIZE)
    Matrix.multiplyMM(mMatrix, mTop, mTemp, 0, m, offset)
  }

  def glMultMatrixf(m: FloatBuffer) {
    m.get(mTemp, MATRIX_SIZE, MATRIX_SIZE)
    glMultMatrixf(mTemp, MATRIX_SIZE)
  }

  def glMultMatrixx(m: Array[Int], offset: Int) {
    {
      var i: Int = 0
      while (i < MATRIX_SIZE) {
        {
          mTemp(MATRIX_SIZE + i) = fixedToFloat(m(offset + i))
        }
        ({
          i += 1; i - 1
        })
      }
    }
    glMultMatrixf(mTemp, MATRIX_SIZE)
  }

  def glMultMatrixx(m: IntBuffer) {
    {
      var i: Int = 0
      while (i < MATRIX_SIZE) {
        {
          mTemp(MATRIX_SIZE + i) = fixedToFloat(m.get)
        }
        ({
          i += 1; i - 1
        })
      }
    }
    glMultMatrixf(mTemp, MATRIX_SIZE)
  }

  def glOrthof(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float) {
    Matrix.orthoM(mMatrix, mTop, left, right, bottom, top, near, far)
  }

  def glOrthox(left: Int, right: Int, bottom: Int, top: Int, near: Int, far: Int) {
    glOrthof(fixedToFloat(left), fixedToFloat(right), fixedToFloat(bottom), fixedToFloat(top), fixedToFloat(near), fixedToFloat(far))
  }

  def glPopMatrix {
    preflight_adjust(-1)
    adjust(-1)
  }

  def glPushMatrix {
    preflight_adjust(1)
    System.arraycopy(mMatrix, mTop, mMatrix, mTop + MATRIX_SIZE, MATRIX_SIZE)
    adjust(1)
  }

  def glRotatef(angle: Float, x: Float, y: Float, z: Float) {
    Matrix.setRotateM(mTemp, 0, angle, x, y, z)
    System.arraycopy(mMatrix, mTop, mTemp, MATRIX_SIZE, MATRIX_SIZE)
    Matrix.multiplyMM(mMatrix, mTop, mTemp, MATRIX_SIZE, mTemp, 0)
  }

  def glRotatex(angle: Int, x: Int, y: Int, z: Int) {
    glRotatef(angle, fixedToFloat(x), fixedToFloat(y), fixedToFloat(z))
  }

  def glScalef(x: Float, y: Float, z: Float) {
    Matrix.scaleM(mMatrix, mTop, x, y, z)
  }

  def glScalex(x: Int, y: Int, z: Int) {
    glScalef(fixedToFloat(x), fixedToFloat(y), fixedToFloat(z))
  }

  def glTranslatef(x: Float, y: Float, z: Float) {
    Matrix.translateM(mMatrix, mTop, x, y, z)
  }

  def glTranslatex(x: Int, y: Int, z: Int) {
    glTranslatef(fixedToFloat(x), fixedToFloat(y), fixedToFloat(z))
  }

  def getMatrix(dest: Array[Float], offset: Int) {
    System.arraycopy(mMatrix, mTop, dest, offset, MATRIX_SIZE)
  }

  private def fixedToFloat(x: Int): Float = {
    return x * (1.0f / 65536.0f)
  }

  private def preflight_adjust(dir: Int) {
    val newTop: Int = mTop + dir * MATRIX_SIZE
    if (newTop < 0) {
      throw new IllegalArgumentException("stack underflow")
    }
    if (newTop + MATRIX_SIZE > mMatrix.length) {
      throw new IllegalArgumentException("stack overflow")
    }
  }

  private def adjust(dir: Int) {
    mTop += dir * MATRIX_SIZE
  }
}