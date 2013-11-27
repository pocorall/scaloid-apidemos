package com.example.android.apis.graphics

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle
//remove if not needed
import scala.collection.JavaConversions._

class MatrixPaletteActivity extends Activity {
  private var mGLSurfaceView: GLSurfaceView = _

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    mGLSurfaceView = new GLSurfaceView(this)
    mGLSurfaceView.setRenderer(new MatrixPaletteRenderer(this))
    setContentView(mGLSurfaceView)
  }

  protected override def onResume() {
    super.onResume()
    mGLSurfaceView.onResume()
  }

  protected override def onPause() {
    super.onPause()
    mGLSurfaceView.onPause()
  }


}
