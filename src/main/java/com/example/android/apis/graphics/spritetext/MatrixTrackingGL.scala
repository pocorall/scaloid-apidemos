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

import android.util.Log
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.opengles.GL
import javax.microedition.khronos.opengles.GL10
import javax.microedition.khronos.opengles.GL10Ext
import javax.microedition.khronos.opengles.GL11
import javax.microedition.khronos.opengles.GL11Ext

/**
 * Allows retrieving the current matrix even if the current OpenGL ES
 * driver does not support retrieving the current matrix.
 *
 * Note: the actual matrix may differ from the retrieved matrix, due
 * to differences in the way the math is implemented by GLMatrixWrapper
 * as compared to the way the math is implemented by the OpenGL ES
 * driver.
 */
class MatrixTrackingGL extends GL with GL10 with GL10Ext with GL11 with GL11Ext {
  private final val _check: Boolean = false

  private var mgl: GL10 = null
  private var mgl10Ext: GL10Ext = null
  private var mgl11: GL11 = null
  private var mgl11Ext: GL11Ext = null
  private var mMatrixMode: Int = 0
  private var mCurrent: MatrixStack = null
  private var mModelView: MatrixStack = null
  private var mTexture: MatrixStack = null
  private var mProjection: MatrixStack = null
  private[spritetext] var mByteBuffer: ByteBuffer = null
  private[spritetext] var mFloatBuffer: FloatBuffer = null
  private[spritetext] var mCheckA: Array[Float] = null
  private[spritetext] var mCheckB: Array[Float] = null

  def this(gl: GL) {
    this()
    mgl = gl.asInstanceOf[GL10]
    if (gl.isInstanceOf[GL10Ext]) {
      mgl10Ext = gl.asInstanceOf[GL10Ext]
    }
    if (gl.isInstanceOf[GL11]) {
      mgl11 = gl.asInstanceOf[GL11]
    }
    if (gl.isInstanceOf[GL11Ext]) {
      mgl11Ext = gl.asInstanceOf[GL11Ext]
    }
    mModelView = new MatrixStack
    mProjection = new MatrixStack
    mTexture = new MatrixStack
    mCurrent = mModelView
    mMatrixMode = GL10.GL_MODELVIEW
  }

  def glActiveTexture(texture: Int) {
    mgl.glActiveTexture(texture)
  }

  def glAlphaFunc(func: Int, ref: Float) {
    mgl.glAlphaFunc(func, ref)
  }

  def glAlphaFuncx(func: Int, ref: Int) {
    mgl.glAlphaFuncx(func, ref)
  }

  def glBindTexture(target: Int, texture: Int) {
    mgl.glBindTexture(target, texture)
  }

  def glBlendFunc(sfactor: Int, dfactor: Int) {
    mgl.glBlendFunc(sfactor, dfactor)
  }

  def glClear(mask: Int) {
    mgl.glClear(mask)
  }

  def glClearColor(red: Float, green: Float, blue: Float, alpha: Float) {
    mgl.glClearColor(red, green, blue, alpha)
  }

  def glClearColorx(red: Int, green: Int, blue: Int, alpha: Int) {
    mgl.glClearColorx(red, green, blue, alpha)
  }

  def glClearDepthf(depth: Float) {
    mgl.glClearDepthf(depth)
  }

  def glClearDepthx(depth: Int) {
    mgl.glClearDepthx(depth)
  }

  def glClearStencil(s: Int) {
    mgl.glClearStencil(s)
  }

  def glClientActiveTexture(texture: Int) {
    mgl.glClientActiveTexture(texture)
  }

  def glColor4f(red: Float, green: Float, blue: Float, alpha: Float) {
    mgl.glColor4f(red, green, blue, alpha)
  }

  def glColor4x(red: Int, green: Int, blue: Int, alpha: Int) {
    mgl.glColor4x(red, green, blue, alpha)
  }

  def glColorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean) {
    mgl.glColorMask(red, green, blue, alpha)
  }

  def glColorPointer(size: Int, `type`: Int, stride: Int, pointer: Buffer) {
    mgl.glColorPointer(size, `type`, stride, pointer)
  }

  def glCompressedTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, imageSize: Int, data: Buffer) {
    mgl.glCompressedTexImage2D(target, level, internalformat, width, height, border, imageSize, data)
  }

  def glCompressedTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, width: Int, height: Int, format: Int, imageSize: Int, data: Buffer) {
    mgl.glCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, imageSize, data)
  }

  def glCopyTexImage2D(target: Int, level: Int, internalformat: Int, x: Int, y: Int, width: Int, height: Int, border: Int) {
    mgl.glCopyTexImage2D(target, level, internalformat, x, y, width, height, border)
  }

  def glCopyTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, x: Int, y: Int, width: Int, height: Int) {
    mgl.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height)
  }

  def glCullFace(mode: Int) {
    mgl.glCullFace(mode)
  }

  def glDeleteTextures(n: Int, textures: Array[Int], offset: Int) {
    mgl.glDeleteTextures(n, textures, offset)
  }

  def glDeleteTextures(n: Int, textures: IntBuffer) {
    mgl.glDeleteTextures(n, textures)
  }

  def glDepthFunc(func: Int) {
    mgl.glDepthFunc(func)
  }

  def glDepthMask(flag: Boolean) {
    mgl.glDepthMask(flag)
  }

  def glDepthRangef(near: Float, far: Float) {
    mgl.glDepthRangef(near, far)
  }

  def glDepthRangex(near: Int, far: Int) {
    mgl.glDepthRangex(near, far)
  }

  def glDisable(cap: Int) {
    mgl.glDisable(cap)
  }

  def glDisableClientState(array: Int) {
    mgl.glDisableClientState(array)
  }

  def glDrawArrays(mode: Int, first: Int, count: Int) {
    mgl.glDrawArrays(mode, first, count)
  }

  def glDrawElements(mode: Int, count: Int, `type`: Int, indices: Buffer) {
    mgl.glDrawElements(mode, count, `type`, indices)
  }

  def glEnable(cap: Int) {
    mgl.glEnable(cap)
  }

  def glEnableClientState(array: Int) {
    mgl.glEnableClientState(array)
  }

  def glFinish {
    mgl.glFinish
  }

  def glFlush {
    mgl.glFlush
  }

  def glFogf(pname: Int, param: Float) {
    mgl.glFogf(pname, param)
  }

  def glFogfv(pname: Int, params: Array[Float], offset: Int) {
    mgl.glFogfv(pname, params, offset)
  }

  def glFogfv(pname: Int, params: FloatBuffer) {
    mgl.glFogfv(pname, params)
  }

  def glFogx(pname: Int, param: Int) {
    mgl.glFogx(pname, param)
  }

  def glFogxv(pname: Int, params: Array[Int], offset: Int) {
    mgl.glFogxv(pname, params, offset)
  }

  def glFogxv(pname: Int, params: IntBuffer) {
    mgl.glFogxv(pname, params)
  }

  def glFrontFace(mode: Int) {
    mgl.glFrontFace(mode)
  }

  def glFrustumf(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float) {
    mCurrent.glFrustumf(left, right, bottom, top, near, far)
    mgl.glFrustumf(left, right, bottom, top, near, far)
    if (_check) check
  }

  def glFrustumx(left: Int, right: Int, bottom: Int, top: Int, near: Int, far: Int) {
    mCurrent.glFrustumx(left, right, bottom, top, near, far)
    mgl.glFrustumx(left, right, bottom, top, near, far)
    if (_check) check
  }

  def glGenTextures(n: Int, textures: Array[Int], offset: Int) {
    mgl.glGenTextures(n, textures, offset)
  }

  def glGenTextures(n: Int, textures: IntBuffer) {
    mgl.glGenTextures(n, textures)
  }

  def glGetError: Int = {
    val result: Int = mgl.glGetError
    return result
  }

  def glGetIntegerv(pname: Int, params: Array[Int], offset: Int) {
    mgl.glGetIntegerv(pname, params, offset)
  }

  def glGetIntegerv(pname: Int, params: IntBuffer) {
    mgl.glGetIntegerv(pname, params)
  }

  def glGetString(name: Int): String = {
    val result: String = mgl.glGetString(name)
    return result
  }

  def glHint(target: Int, mode: Int) {
    mgl.glHint(target, mode)
  }

  def glLightModelf(pname: Int, param: Float) {
    mgl.glLightModelf(pname, param)
  }

  def glLightModelfv(pname: Int, params: Array[Float], offset: Int) {
    mgl.glLightModelfv(pname, params, offset)
  }

  def glLightModelfv(pname: Int, params: FloatBuffer) {
    mgl.glLightModelfv(pname, params)
  }

  def glLightModelx(pname: Int, param: Int) {
    mgl.glLightModelx(pname, param)
  }

  def glLightModelxv(pname: Int, params: Array[Int], offset: Int) {
    mgl.glLightModelxv(pname, params, offset)
  }

  def glLightModelxv(pname: Int, params: IntBuffer) {
    mgl.glLightModelxv(pname, params)
  }

  def glLightf(light: Int, pname: Int, param: Float) {
    mgl.glLightf(light, pname, param)
  }

  def glLightfv(light: Int, pname: Int, params: Array[Float], offset: Int) {
    mgl.glLightfv(light, pname, params, offset)
  }

  def glLightfv(light: Int, pname: Int, params: FloatBuffer) {
    mgl.glLightfv(light, pname, params)
  }

  def glLightx(light: Int, pname: Int, param: Int) {
    mgl.glLightx(light, pname, param)
  }

  def glLightxv(light: Int, pname: Int, params: Array[Int], offset: Int) {
    mgl.glLightxv(light, pname, params, offset)
  }

  def glLightxv(light: Int, pname: Int, params: IntBuffer) {
    mgl.glLightxv(light, pname, params)
  }

  def glLineWidth(width: Float) {
    mgl.glLineWidth(width)
  }

  def glLineWidthx(width: Int) {
    mgl.glLineWidthx(width)
  }

  def glLoadIdentity {
    mCurrent.glLoadIdentity
    mgl.glLoadIdentity
    if (_check) check
  }

  def glLoadMatrixf(m: Array[Float], offset: Int) {
    mCurrent.glLoadMatrixf(m, offset)
    mgl.glLoadMatrixf(m, offset)
    if (_check) check
  }

  def glLoadMatrixf(m: FloatBuffer) {
    val position: Int = m.position
    mCurrent.glLoadMatrixf(m)
    m.position(position)
    mgl.glLoadMatrixf(m)
    if (_check) check
  }

  def glLoadMatrixx(m: Array[Int], offset: Int) {
    mCurrent.glLoadMatrixx(m, offset)
    mgl.glLoadMatrixx(m, offset)
    if (_check) check
  }

  def glLoadMatrixx(m: IntBuffer) {
    val position: Int = m.position
    mCurrent.glLoadMatrixx(m)
    m.position(position)
    mgl.glLoadMatrixx(m)
    if (_check) check
  }

  def glLogicOp(opcode: Int) {
    mgl.glLogicOp(opcode)
  }

  def glMaterialf(face: Int, pname: Int, param: Float) {
    mgl.glMaterialf(face, pname, param)
  }

  def glMaterialfv(face: Int, pname: Int, params: Array[Float], offset: Int) {
    mgl.glMaterialfv(face, pname, params, offset)
  }

  def glMaterialfv(face: Int, pname: Int, params: FloatBuffer) {
    mgl.glMaterialfv(face, pname, params)
  }

  def glMaterialx(face: Int, pname: Int, param: Int) {
    mgl.glMaterialx(face, pname, param)
  }

  def glMaterialxv(face: Int, pname: Int, params: Array[Int], offset: Int) {
    mgl.glMaterialxv(face, pname, params, offset)
  }

  def glMaterialxv(face: Int, pname: Int, params: IntBuffer) {
    mgl.glMaterialxv(face, pname, params)
  }

  def glMatrixMode(mode: Int) {
    mode match {
      case GL10.GL_MODELVIEW =>
        mCurrent = mModelView
      case GL10.GL_TEXTURE =>
        mCurrent = mTexture
      case GL10.GL_PROJECTION =>
        mCurrent = mProjection
      case _ =>
        throw new IllegalArgumentException("Unknown matrix mode: " + mode)
    }
    mgl.glMatrixMode(mode)
    mMatrixMode = mode
    if (_check) check
  }

  def glMultMatrixf(m: Array[Float], offset: Int) {
    mCurrent.glMultMatrixf(m, offset)
    mgl.glMultMatrixf(m, offset)
    if (_check) check
  }

  def glMultMatrixf(m: FloatBuffer) {
    val position: Int = m.position
    mCurrent.glMultMatrixf(m)
    m.position(position)
    mgl.glMultMatrixf(m)
    if (_check) check
  }

  def glMultMatrixx(m: Array[Int], offset: Int) {
    mCurrent.glMultMatrixx(m, offset)
    mgl.glMultMatrixx(m, offset)
    if (_check) check
  }

  def glMultMatrixx(m: IntBuffer) {
    val position: Int = m.position
    mCurrent.glMultMatrixx(m)
    m.position(position)
    mgl.glMultMatrixx(m)
    if (_check) check
  }

  def glMultiTexCoord4f(target: Int, s: Float, t: Float, r: Float, q: Float) {
    mgl.glMultiTexCoord4f(target, s, t, r, q)
  }

  def glMultiTexCoord4x(target: Int, s: Int, t: Int, r: Int, q: Int) {
    mgl.glMultiTexCoord4x(target, s, t, r, q)
  }

  def glNormal3f(nx: Float, ny: Float, nz: Float) {
    mgl.glNormal3f(nx, ny, nz)
  }

  def glNormal3x(nx: Int, ny: Int, nz: Int) {
    mgl.glNormal3x(nx, ny, nz)
  }

  def glNormalPointer(`type`: Int, stride: Int, pointer: Buffer) {
    mgl.glNormalPointer(`type`, stride, pointer)
  }

  def glOrthof(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float) {
    mCurrent.glOrthof(left, right, bottom, top, near, far)
    mgl.glOrthof(left, right, bottom, top, near, far)
    if (_check) check
  }

  def glOrthox(left: Int, right: Int, bottom: Int, top: Int, near: Int, far: Int) {
    mCurrent.glOrthox(left, right, bottom, top, near, far)
    mgl.glOrthox(left, right, bottom, top, near, far)
    if (_check) check
  }

  def glPixelStorei(pname: Int, param: Int) {
    mgl.glPixelStorei(pname, param)
  }

  def glPointSize(size: Float) {
    mgl.glPointSize(size)
  }

  def glPointSizex(size: Int) {
    mgl.glPointSizex(size)
  }

  def glPolygonOffset(factor: Float, units: Float) {
    mgl.glPolygonOffset(factor, units)
  }

  def glPolygonOffsetx(factor: Int, units: Int) {
    mgl.glPolygonOffsetx(factor, units)
  }

  def glPopMatrix {
    mCurrent.glPopMatrix
    mgl.glPopMatrix
    if (_check) check
  }

  def glPushMatrix {
    mCurrent.glPushMatrix
    mgl.glPushMatrix
    if (_check) check
  }

  def glReadPixels(x: Int, y: Int, width: Int, height: Int, format: Int, `type`: Int, pixels: Buffer) {
    mgl.glReadPixels(x, y, width, height, format, `type`, pixels)
  }

  def glRotatef(angle: Float, x: Float, y: Float, z: Float) {
    mCurrent.glRotatef(angle, x, y, z)
    mgl.glRotatef(angle, x, y, z)
    if (_check) check
  }

  def glRotatex(angle: Int, x: Int, y: Int, z: Int) {
    mCurrent.glRotatex(angle, x, y, z)
    mgl.glRotatex(angle, x, y, z)
    if (_check) check
  }

  def glSampleCoverage(value: Float, invert: Boolean) {
    mgl.glSampleCoverage(value, invert)
  }

  def glSampleCoveragex(value: Int, invert: Boolean) {
    mgl.glSampleCoveragex(value, invert)
  }

  def glScalef(x: Float, y: Float, z: Float) {
    mCurrent.glScalef(x, y, z)
    mgl.glScalef(x, y, z)
    if (_check) check
  }

  def glScalex(x: Int, y: Int, z: Int) {
    mCurrent.glScalex(x, y, z)
    mgl.glScalex(x, y, z)
    if (_check) check
  }

  def glScissor(x: Int, y: Int, width: Int, height: Int) {
    mgl.glScissor(x, y, width, height)
  }

  def glShadeModel(mode: Int) {
    mgl.glShadeModel(mode)
  }

  def glStencilFunc(func: Int, ref: Int, mask: Int) {
    mgl.glStencilFunc(func, ref, mask)
  }

  def glStencilMask(mask: Int) {
    mgl.glStencilMask(mask)
  }

  def glStencilOp(fail: Int, zfail: Int, zpass: Int) {
    mgl.glStencilOp(fail, zfail, zpass)
  }

  def glTexCoordPointer(size: Int, `type`: Int, stride: Int, pointer: Buffer) {
    mgl.glTexCoordPointer(size, `type`, stride, pointer)
  }

  def glTexEnvf(target: Int, pname: Int, param: Float) {
    mgl.glTexEnvf(target, pname, param)
  }

  def glTexEnvfv(target: Int, pname: Int, params: Array[Float], offset: Int) {
    mgl.glTexEnvfv(target, pname, params, offset)
  }

  def glTexEnvfv(target: Int, pname: Int, params: FloatBuffer) {
    mgl.glTexEnvfv(target, pname, params)
  }

  def glTexEnvx(target: Int, pname: Int, param: Int) {
    mgl.glTexEnvx(target, pname, param)
  }

  def glTexEnvxv(target: Int, pname: Int, params: Array[Int], offset: Int) {
    mgl.glTexEnvxv(target, pname, params, offset)
  }

  def glTexEnvxv(target: Int, pname: Int, params: IntBuffer) {
    mgl.glTexEnvxv(target, pname, params)
  }

  def glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, `type`: Int, pixels: Buffer) {
    mgl.glTexImage2D(target, level, internalformat, width, height, border, format, `type`, pixels)
  }

  def glTexParameterf(target: Int, pname: Int, param: Float) {
    mgl.glTexParameterf(target, pname, param)
  }

  def glTexParameterx(target: Int, pname: Int, param: Int) {
    mgl.glTexParameterx(target, pname, param)
  }

  def glTexParameteriv(target: Int, pname: Int, params: Array[Int], offset: Int) {
    mgl11.glTexParameteriv(target, pname, params, offset)
  }

  def glTexParameteriv(target: Int, pname: Int, params: IntBuffer) {
    mgl11.glTexParameteriv(target, pname, params)
  }

  def glTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, width: Int, height: Int, format: Int, `type`: Int, pixels: Buffer) {
    mgl.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, `type`, pixels)
  }

  def glTranslatef(x: Float, y: Float, z: Float) {
    mCurrent.glTranslatef(x, y, z)
    mgl.glTranslatef(x, y, z)
    if (_check) check
  }

  def glTranslatex(x: Int, y: Int, z: Int) {
    mCurrent.glTranslatex(x, y, z)
    mgl.glTranslatex(x, y, z)
    if (_check) check
  }

  def glVertexPointer(size: Int, `type`: Int, stride: Int, pointer: Buffer) {
    mgl.glVertexPointer(size, `type`, stride, pointer)
  }

  def glViewport(x: Int, y: Int, width: Int, height: Int) {
    mgl.glViewport(x, y, width, height)
  }

  def glClipPlanef(plane: Int, equation: Array[Float], offset: Int) {
    mgl11.glClipPlanef(plane, equation, offset)
  }

  def glClipPlanef(plane: Int, equation: FloatBuffer) {
    mgl11.glClipPlanef(plane, equation)
  }

  def glClipPlanex(plane: Int, equation: Array[Int], offset: Int) {
    mgl11.glClipPlanex(plane, equation, offset)
  }

  def glClipPlanex(plane: Int, equation: IntBuffer) {
    mgl11.glClipPlanex(plane, equation)
  }

  def glDrawTexfOES(x: Float, y: Float, z: Float, width: Float, height: Float) {
    mgl11Ext.glDrawTexfOES(x, y, z, width, height)
  }

  def glDrawTexfvOES(coords: Array[Float], offset: Int) {
    mgl11Ext.glDrawTexfvOES(coords, offset)
  }

  def glDrawTexfvOES(coords: FloatBuffer) {
    mgl11Ext.glDrawTexfvOES(coords)
  }

  def glDrawTexiOES(x: Int, y: Int, z: Int, width: Int, height: Int) {
    mgl11Ext.glDrawTexiOES(x, y, z, width, height)
  }

  def glDrawTexivOES(coords: Array[Int], offset: Int) {
    mgl11Ext.glDrawTexivOES(coords, offset)
  }

  def glDrawTexivOES(coords: IntBuffer) {
    mgl11Ext.glDrawTexivOES(coords)
  }

  def glDrawTexsOES(x: Short, y: Short, z: Short, width: Short, height: Short) {
    mgl11Ext.glDrawTexsOES(x, y, z, width, height)
  }

  def glDrawTexsvOES(coords: Array[Short], offset: Int) {
    mgl11Ext.glDrawTexsvOES(coords, offset)
  }

  def glDrawTexsvOES(coords: ShortBuffer) {
    mgl11Ext.glDrawTexsvOES(coords)
  }

  def glDrawTexxOES(x: Int, y: Int, z: Int, width: Int, height: Int) {
    mgl11Ext.glDrawTexxOES(x, y, z, width, height)
  }

  def glDrawTexxvOES(coords: Array[Int], offset: Int) {
    mgl11Ext.glDrawTexxvOES(coords, offset)
  }

  def glDrawTexxvOES(coords: IntBuffer) {
    mgl11Ext.glDrawTexxvOES(coords)
  }

  def glQueryMatrixxOES(mantissa: Array[Int], mantissaOffset: Int, exponent: Array[Int], exponentOffset: Int): Int = {
    return mgl10Ext.glQueryMatrixxOES(mantissa, mantissaOffset, exponent, exponentOffset)
  }

  def glQueryMatrixxOES(mantissa: IntBuffer, exponent: IntBuffer): Int = {
    return mgl10Ext.glQueryMatrixxOES(mantissa, exponent)
  }

  def glBindBuffer(target: Int, buffer: Int) {
    throw new UnsupportedOperationException
  }

  def glBufferData(target: Int, size: Int, data: Buffer, usage: Int) {
    throw new UnsupportedOperationException
  }

  def glBufferSubData(target: Int, offset: Int, size: Int, data: Buffer) {
    throw new UnsupportedOperationException
  }

  def glColor4ub(red: Byte, green: Byte, blue: Byte, alpha: Byte) {
    throw new UnsupportedOperationException
  }

  def glDeleteBuffers(n: Int, buffers: Array[Int], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glDeleteBuffers(n: Int, buffers: IntBuffer) {
    throw new UnsupportedOperationException
  }

  def glGenBuffers(n: Int, buffers: Array[Int], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glGenBuffers(n: Int, buffers: IntBuffer) {
    throw new UnsupportedOperationException
  }

  def glGetBooleanv(pname: Int, params: Array[Boolean], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glGetBooleanv(pname: Int, params: IntBuffer) {
    throw new UnsupportedOperationException
  }

  def glGetBufferParameteriv(target: Int, pname: Int, params: Array[Int], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glGetBufferParameteriv(target: Int, pname: Int, params: IntBuffer) {
    throw new UnsupportedOperationException
  }

  def glGetClipPlanef(pname: Int, eqn: Array[Float], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glGetClipPlanef(pname: Int, eqn: FloatBuffer) {
    throw new UnsupportedOperationException
  }

  def glGetClipPlanex(pname: Int, eqn: Array[Int], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glGetClipPlanex(pname: Int, eqn: IntBuffer) {
    throw new UnsupportedOperationException
  }

  def glGetFixedv(pname: Int, params: Array[Int], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glGetFixedv(pname: Int, params: IntBuffer) {
    throw new UnsupportedOperationException
  }

  def glGetFloatv(pname: Int, params: Array[Float], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glGetFloatv(pname: Int, params: FloatBuffer) {
    throw new UnsupportedOperationException
  }

  def glGetLightfv(light: Int, pname: Int, params: Array[Float], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glGetLightfv(light: Int, pname: Int, params: FloatBuffer) {
    throw new UnsupportedOperationException
  }

  def glGetLightxv(light: Int, pname: Int, params: Array[Int], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glGetLightxv(light: Int, pname: Int, params: IntBuffer) {
    throw new UnsupportedOperationException
  }

  def glGetMaterialfv(face: Int, pname: Int, params: Array[Float], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glGetMaterialfv(face: Int, pname: Int, params: FloatBuffer) {
    throw new UnsupportedOperationException
  }

  def glGetMaterialxv(face: Int, pname: Int, params: Array[Int], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glGetMaterialxv(face: Int, pname: Int, params: IntBuffer) {
    throw new UnsupportedOperationException
  }

  def glGetTexEnviv(env: Int, pname: Int, params: Array[Int], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glGetTexEnviv(env: Int, pname: Int, params: IntBuffer) {
    throw new UnsupportedOperationException
  }

  def glGetTexEnvxv(env: Int, pname: Int, params: Array[Int], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glGetTexEnvxv(env: Int, pname: Int, params: IntBuffer) {
    throw new UnsupportedOperationException
  }

  def glGetTexParameterfv(target: Int, pname: Int, params: Array[Float], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glGetTexParameterfv(target: Int, pname: Int, params: FloatBuffer) {
    throw new UnsupportedOperationException
  }

  def glGetTexParameteriv(target: Int, pname: Int, params: Array[Int], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glGetTexParameteriv(target: Int, pname: Int, params: IntBuffer) {
    throw new UnsupportedOperationException
  }

  def glGetTexParameterxv(target: Int, pname: Int, params: Array[Int], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glGetTexParameterxv(target: Int, pname: Int, params: IntBuffer) {
    throw new UnsupportedOperationException
  }

  def glIsBuffer(buffer: Int): Boolean = {
    throw new UnsupportedOperationException
  }

  def glIsEnabled(cap: Int): Boolean = {
    throw new UnsupportedOperationException
  }

  def glIsTexture(texture: Int): Boolean = {
    throw new UnsupportedOperationException
  }

  def glPointParameterf(pname: Int, param: Float) {
    throw new UnsupportedOperationException
  }

  def glPointParameterfv(pname: Int, params: Array[Float], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glPointParameterfv(pname: Int, params: FloatBuffer) {
    throw new UnsupportedOperationException
  }

  def glPointParameterx(pname: Int, param: Int) {
    throw new UnsupportedOperationException
  }

  def glPointParameterxv(pname: Int, params: Array[Int], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glPointParameterxv(pname: Int, params: IntBuffer) {
    throw new UnsupportedOperationException
  }

  def glPointSizePointerOES(`type`: Int, stride: Int, pointer: Buffer) {
    throw new UnsupportedOperationException
  }

  def glTexEnvi(target: Int, pname: Int, param: Int) {
    throw new UnsupportedOperationException
  }

  def glTexEnviv(target: Int, pname: Int, params: Array[Int], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glTexEnviv(target: Int, pname: Int, params: IntBuffer) {
    throw new UnsupportedOperationException
  }

  def glTexParameterfv(target: Int, pname: Int, params: Array[Float], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glTexParameterfv(target: Int, pname: Int, params: FloatBuffer) {
    throw new UnsupportedOperationException
  }

  def glTexParameteri(target: Int, pname: Int, param: Int) {
    throw new UnsupportedOperationException
  }

  def glTexParameterxv(target: Int, pname: Int, params: Array[Int], offset: Int) {
    throw new UnsupportedOperationException
  }

  def glTexParameterxv(target: Int, pname: Int, params: IntBuffer) {
    throw new UnsupportedOperationException
  }

  def glColorPointer(size: Int, `type`: Int, stride: Int, offset: Int) {
    throw new UnsupportedOperationException
  }

  def glDrawElements(mode: Int, count: Int, `type`: Int, offset: Int) {
    throw new UnsupportedOperationException
  }

  def glGetPointerv(pname: Int, params: Array[Buffer]) {
    throw new UnsupportedOperationException
  }

  def glNormalPointer(`type`: Int, stride: Int, offset: Int) {
    throw new UnsupportedOperationException
  }

  def glTexCoordPointer(size: Int, `type`: Int, stride: Int, offset: Int) {
    throw new UnsupportedOperationException
  }

  def glVertexPointer(size: Int, `type`: Int, stride: Int, offset: Int) {
    throw new UnsupportedOperationException
  }

  def glCurrentPaletteMatrixOES(matrixpaletteindex: Int) {
    throw new UnsupportedOperationException
  }

  def glLoadPaletteFromModelViewMatrixOES {
    throw new UnsupportedOperationException
  }

  def glMatrixIndexPointerOES(size: Int, `type`: Int, stride: Int, pointer: Buffer) {
    throw new UnsupportedOperationException
  }

  def glMatrixIndexPointerOES(size: Int, `type`: Int, stride: Int, offset: Int) {
    throw new UnsupportedOperationException
  }

  def glWeightPointerOES(size: Int, `type`: Int, stride: Int, pointer: Buffer) {
    throw new UnsupportedOperationException
  }

  def glWeightPointerOES(size: Int, `type`: Int, stride: Int, offset: Int) {
    throw new UnsupportedOperationException
  }

  /**
   * Get the current matrix
   */
  def getMatrix(m: Array[Float], offset: Int) {
    mCurrent.getMatrix(m, offset)
  }

  /**
   * Get the current matrix mode
   */
  def getMatrixMode: Int = {
    return mMatrixMode
  }

  private def check {
    var oesMode: Int = 0
    mMatrixMode match {
      case javax.microedition.khronos.opengles.GL10.GL_MODELVIEW =>
        oesMode = GL11.GL_MODELVIEW_MATRIX_FLOAT_AS_INT_BITS_OES
      case javax.microedition.khronos.opengles.GL10.GL_PROJECTION =>
        oesMode = GL11.GL_PROJECTION_MATRIX_FLOAT_AS_INT_BITS_OES
      case javax.microedition.khronos.opengles.GL10.GL_TEXTURE =>
        oesMode = GL11.GL_TEXTURE_MATRIX_FLOAT_AS_INT_BITS_OES
      case _ =>
        throw new IllegalArgumentException("Unknown matrix mode")
    }
    if (mByteBuffer == null) {
      mCheckA = new Array[Float](16)
      mCheckB = new Array[Float](16)
      mByteBuffer = ByteBuffer.allocateDirect(64)
      mByteBuffer.order(ByteOrder.nativeOrder)
      mFloatBuffer = mByteBuffer.asFloatBuffer
    }
    mgl.glGetIntegerv(oesMode, mByteBuffer.asIntBuffer)
    var i: Int = 0
    while (i < 16) {
      {
        mCheckB(i) = mFloatBuffer.get(i)
      }
      ({
        i += 1; i - 1
      })
    }

    mCurrent.getMatrix(mCheckA, 0)
    var fail: Boolean = false

    i = 0
    while (i < 16) {
      {
        if (mCheckA(i) != mCheckB(i)) {
          Log.d("GLMatWrap", "i:" + i + " a:" + mCheckA(i) + " a:" + mCheckB(i))
          fail = true
        }
      }
      ({
        i += 1; i - 1
      })
    }

    if (fail) {
      throw new IllegalArgumentException("Matrix math difference.")
    }
  }

}