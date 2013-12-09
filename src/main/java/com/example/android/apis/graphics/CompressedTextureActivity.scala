package com.example.android.apis.graphics

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.opengles.GL10
import android.app.Activity
import android.opengl.ETC1Util
import android.opengl.GLES10
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import com.example.android.apis.R
//remove if not needed
import scala.collection.JavaConversions._

class CompressedTextureActivity extends Activity {
  private val TAG = "CompressedTextureActivity"

  private val TEST_CREATE_TEXTURE = false

  private val USE_STREAM_IO = false

  private var mGLView: GLSurfaceView = _

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    mGLView = new GLSurfaceView(this)
    mGLView.setEGLConfigChooser(false)
    var loader: StaticTriangleRenderer.TextureLoader = null
    loader = if (TEST_CREATE_TEXTURE) new SyntheticCompressedTextureLoader() else new CompressedTextureLoader()
    mGLView.setRenderer(new StaticTriangleRenderer(this, loader))
    setContentView(mGLView)
  }

  protected override def onPause() {
    super.onPause()
    mGLView.onPause()
  }

  protected override def onResume() {
    super.onResume()
    mGLView.onResume()
  }

  private class CompressedTextureLoader extends StaticTriangleRenderer.TextureLoader {

    def load(gl: GL10) {
      Log.w(TAG, "ETC1 texture support: " + ETC1Util.isETC1Supported)
      val input = getResources.openRawResource(R.raw.androids)
      try {
        ETC1Util.loadTexture(GLES10.GL_TEXTURE_2D, 0, 0, GLES10.GL_RGB, GLES10.GL_UNSIGNED_SHORT_5_6_5,
          input)
      } catch {
        case e: IOException => Log.w(TAG, "Could not load texture: " + e)
      } finally {
        try {
          input.close()
        } catch {
          case e: IOException =>
        }
      }
    }
  }

  private class SyntheticCompressedTextureLoader extends StaticTriangleRenderer.TextureLoader {

    def load(gl: GL10) {
      val width = 128
      val height = 128
      val image = createImage(width, height)
      val etc1Texture = ETC1Util.compressTexture(image, width, height, 3, 3 * width)
      if (USE_STREAM_IO) {
        try {
          val bos = new ByteArrayOutputStream()
          ETC1Util.writeTexture(etc1Texture, bos)
          val bis = new ByteArrayInputStream(bos.toByteArray())
          ETC1Util.loadTexture(GLES10.GL_TEXTURE_2D, 0, 0, GLES10.GL_RGB, GLES10.GL_UNSIGNED_SHORT_5_6_5,
            bis)
        } catch {
          case e: IOException => Log.w(TAG, "Could not load texture: " + e)
        }
      } else {
        ETC1Util.loadTexture(GLES10.GL_TEXTURE_2D, 0, 0, GLES10.GL_RGB, GLES10.GL_UNSIGNED_SHORT_5_6_5,
          etc1Texture)
      }
    }

    private def createImage(width: Int, height: Int): Buffer = {
      val stride = 3 * width
      val image = ByteBuffer.allocateDirect(height * stride).order(ByteOrder.nativeOrder())
      for (t <- 0 until height) {
        val red = (255 - 2 * t).toByte
        val green = (2 * t).toByte
        val blue = 0
        for (x <- 0 until width) {
          val y = x ^ t
          image.position(stride * y + x * 3)
          image.put(red.toByte)
          image.put(green.toByte)
          image.put(blue.toByte)
        }
      }
      image.position(0)
      image
    }
  }


}
