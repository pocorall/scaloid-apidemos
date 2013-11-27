package com.example.android.apis.graphics.spritetext

import javax.microedition.khronos.opengles.GL
import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle
//remove if not needed
import scala.collection.JavaConversions._

class SpriteTextActivity extends Activity {
  private var mGLSurfaceView: GLSurfaceView = _

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    mGLSurfaceView = new GLSurfaceView(this)
    mGLSurfaceView.setGLWrapper(new GLSurfaceView.GLWrapper() {

      def wrap(gl: GL): GL = return new MatrixTrackingGL(gl)
    })
    mGLSurfaceView.setRenderer(new SpriteTextRenderer(this))
    setContentView(mGLSurfaceView)
  }

  protected override def onPause() {
    super.onPause()
    mGLSurfaceView.onPause()
  }

  protected override def onResume() {
    super.onResume()
    mGLSurfaceView.onResume()
  }

}
