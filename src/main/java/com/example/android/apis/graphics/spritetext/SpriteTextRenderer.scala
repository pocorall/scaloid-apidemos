package com.example.android.apis.graphics.spritetext

import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.opengl.GLSurfaceView
import android.opengl.GLU
import android.opengl.GLUtils
import android.os.SystemClock
import android.util.Log
import com.example.android.apis.R
import Triangle._
//remove if not needed
import scala.collection.JavaConversions._

class SpriteTextRenderer(private var mContext: Context) extends GLSurfaceView.Renderer {
  private val SAMPLE_PERIOD_FRAMES = 12

  private val SAMPLE_FACTOR = 1.0f / SAMPLE_PERIOD_FRAMES

  private var mWidth: Int = _

  private var mHeight: Int = _

  private var mTriangle: Triangle = new Triangle()

  private var mTextureID: Int = _

  private var mFrames: Int = _

  private var mMsPerFrame: Int = _

  private var mStartTime: Long = _

  private var mLabels: LabelMaker = _

  private var mLabelPaint: Paint = new Paint()

  private var mLabelA: Int = _

  private var mLabelB: Int = _

  private var mLabelC: Int = _

  private var mLabelMsPF: Int = _

  private var mProjector: Projector = new Projector()

  private var mNumericSprite: NumericSprite = _

  private var mScratch: Array[Float] = new Array[Float](8)

  private var mLastTime: Long = _

  mLabelPaint.setTextSize(32)

  mLabelPaint.setAntiAlias(true)

  mLabelPaint.setARGB(0xff, 0x00, 0x00, 0x00)

  def onSurfaceCreated(gl: GL10, config: EGLConfig) {
    gl.glDisable(GL10.GL_DITHER)
    gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST)
    gl.glClearColor(.5f, .5f, .5f, 1)
    gl.glShadeModel(GL10.GL_SMOOTH)
    gl.glEnable(GL10.GL_DEPTH_TEST)
    gl.glEnable(GL10.GL_TEXTURE_2D)
    val textures = Array.ofDim[Int](1)
    gl.glGenTextures(1, textures, 0)
    mTextureID = textures(0)
    gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID)
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST)
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR)
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE)
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE)
    gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE)
    val is = mContext.getResources.openRawResource(R.raw.robot)
    var bitmap: Bitmap = null
    try {
      bitmap = BitmapFactory.decodeStream(is)
    } finally {
      try {
        is.close()
      } catch {
        case e: IOException =>
      }
    }
    GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0)
    bitmap.recycle()
    if (mLabels != null) {
      mLabels.shutdown(gl)
    } else {
      mLabels = new LabelMaker(true, 256, 64)
    }
    mLabels.initialize(gl)
    mLabels.beginAdding(gl)
    mLabelA = mLabels.add(gl, "A", mLabelPaint)
    mLabelB = mLabels.add(gl, "B", mLabelPaint)
    mLabelC = mLabels.add(gl, "C", mLabelPaint)
    mLabelMsPF = mLabels.add(gl, "ms/f", mLabelPaint)
    mLabels.endAdding(gl)
    if (mNumericSprite != null) {
      mNumericSprite.shutdown(gl)
    } else {
      mNumericSprite = new NumericSprite()
    }
    mNumericSprite.initialize(gl, mLabelPaint)
  }

  def onDrawFrame(gl: GL10) {
    gl.glDisable(GL10.GL_DITHER)
    gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE)
    gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT)
    gl.glMatrixMode(GL10.GL_MODELVIEW)
    gl.glLoadIdentity()
    GLU.gluLookAt(gl, 0.0f, 0.0f, -2.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f)
    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
    gl.glActiveTexture(GL10.GL_TEXTURE0)
    gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID)
    gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT)
    gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT)
    if (false) {
      val time = SystemClock.uptimeMillis()
      if (mLastTime != 0) {
        val delta = time - mLastTime
        Log.w("time", java.lang.Long.toString(delta))
      }
      mLastTime = time
    }
    val time = SystemClock.uptimeMillis() % 4000L
    val angle = 0.090f * time.toInt
    gl.glRotatef(angle, 0, 0, 1.0f)
    gl.glScalef(2.0f, 2.0f, 2.0f)
    mTriangle.draw(gl)
    mProjector.getCurrentModelView(gl)
    mLabels.beginDrawing(gl, mWidth, mHeight)
    drawLabel(gl, 0, mLabelA)
    drawLabel(gl, 1, mLabelB)
    drawLabel(gl, 2, mLabelC)
    val msPFX = mWidth - mLabels.getWidth(mLabelMsPF) - 1
    mLabels.draw(gl, msPFX, 0, mLabelMsPF)
    mLabels.endDrawing(gl)
    drawMsPF(gl, msPFX)
  }

  private def drawMsPF(gl: GL10, rightMargin: Float) {
    val time = SystemClock.uptimeMillis()
    if (mStartTime == 0) {
      mStartTime = time
    }

    mFrames += 1
    if ( mFrames == SAMPLE_PERIOD_FRAMES) {
      mFrames = 0
      val delta = time - mStartTime
      mStartTime = time
      mMsPerFrame = (delta * SAMPLE_FACTOR).toInt
    }
    if (mMsPerFrame > 0) {
      mNumericSprite.setValue(mMsPerFrame)
      val numWidth = mNumericSprite.width()
      val x = rightMargin - numWidth
      mNumericSprite.draw(gl, x, 0, mWidth, mHeight)
    }
  }

  private def drawLabel(gl: GL10, triangleVertex: Int, labelId: Int) {
    val x = mTriangle.getX(triangleVertex)
    val y = mTriangle.getY(triangleVertex)
    mScratch(0) = x
    mScratch(1) = y
    mScratch(2) = 0.0f
    mScratch(3) = 1.0f
    mProjector.project(mScratch, 0, mScratch, 4)
    val sx = mScratch(4)
    val sy = mScratch(5)
    val height = mLabels.getHeight(labelId)
    val width = mLabels.getWidth(labelId)
    val tx = sx - width * 0.5f
    val ty = sy - height * 0.5f
    mLabels.draw(gl, tx, ty, labelId)
  }

  def onSurfaceChanged(gl: GL10, w: Int, h: Int) {
    mWidth = w
    mHeight = h
    gl.glViewport(0, 0, w, h)
    mProjector.setCurrentView(0, 0, w, h)
    val ratio = w.toFloat / h
    gl.glMatrixMode(GL10.GL_PROJECTION)
    gl.glLoadIdentity()
    gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10)
    mProjector.getCurrentProjection(gl)
  }

}

object Triangle {

  private val VERTS = 3

  private val sCoords = Array(-0.5f, -0.25f, 0, 0.5f, -0.25f, 0, 0.0f, 0.559016994f, 0)
}

class Triangle {

  val vbb = ByteBuffer.allocateDirect(VERTS * 3 * 4)

  vbb.order(ByteOrder.nativeOrder())

  val tbb = ByteBuffer.allocateDirect(VERTS * 2 * 4)

  tbb.order(ByteOrder.nativeOrder())

  val ibb = ByteBuffer.allocateDirect(VERTS * 2)

  ibb.order(ByteOrder.nativeOrder())

  private var mFVertexBuffer: FloatBuffer = vbb.asFloatBuffer()

  private var mTexBuffer: FloatBuffer = tbb.asFloatBuffer()

  private var mIndexBuffer: ShortBuffer = ibb.asShortBuffer()

  for (i <- 0 until VERTS; j <- 0 until 3) {
    mFVertexBuffer.put(sCoords(i * 3 + j))
  }

  for (i <- 0 until VERTS; j <- 0 until 2) {
    mTexBuffer.put(sCoords(i * 3 + j) * 2.0f + 0.5f)
  }

  for (i <- 0 until VERTS) {
    mIndexBuffer.put(i.toShort)
  }

  mFVertexBuffer.position(0)

  mTexBuffer.position(0)

  mIndexBuffer.position(0)

  def draw(gl: GL10) {
    gl.glFrontFace(GL10.GL_CCW)
    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer)
    gl.glEnable(GL10.GL_TEXTURE_2D)
    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer)
    gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, VERTS, GL10.GL_UNSIGNED_SHORT, mIndexBuffer)
  }

  def getX(vertex: Int): Float = sCoords(3 * vertex)

  def getY(vertex: Int): Float = sCoords(3 * vertex + 1)


}
