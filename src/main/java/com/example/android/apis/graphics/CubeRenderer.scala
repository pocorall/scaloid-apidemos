package com.example.android.apis.graphics

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLSurfaceView
//remove if not needed
import scala.collection.JavaConversions._

class CubeRenderer(private var mTranslucentBackground: Boolean) extends GLSurfaceView.Renderer {
  private var mCube: Cube = new Cube()

  private var mAngle: Float = _

  def onDrawFrame(gl: GL10) {
    gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT)
    gl.glMatrixMode(GL10.GL_MODELVIEW)
    gl.glLoadIdentity()
    gl.glTranslatef(0, 0, -3.0f)
    gl.glRotatef(mAngle, 0, 1, 0)
    gl.glRotatef(mAngle * 0.25f, 1, 0, 0)
    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
    gl.glEnableClientState(GL10.GL_COLOR_ARRAY)
    mCube.draw(gl)
    gl.glRotatef(mAngle * 2.0f, 0, 1, 1)
    gl.glTranslatef(0.5f, 0.5f, 0.5f)
    mCube.draw(gl)
    mAngle += 1.2f
  }

  def onSurfaceChanged(gl: GL10, width: Int, height: Int) {
    gl.glViewport(0, 0, width, height)
    val ratio = width.toFloat / height
    gl.glMatrixMode(GL10.GL_PROJECTION)
    gl.glLoadIdentity()
    gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10)
  }

  def onSurfaceCreated(gl: GL10, config: EGLConfig) {
    gl.glDisable(GL10.GL_DITHER)
    gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST)
    if (mTranslucentBackground) {
      gl.glClearColor(0, 0, 0, 0)
    } else {
      gl.glClearColor(1, 1, 1, 1)
    }
    gl.glEnable(GL10.GL_CULL_FACE)
    gl.glShadeModel(GL10.GL_SMOOTH)
    gl.glEnable(GL10.GL_DEPTH_TEST)
  }

}
