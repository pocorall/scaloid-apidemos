package com.example.android.apis.graphics

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import com.example.android.apis.R
import org.scaloid.common._

//remove if not needed
import scala.collection.JavaConversions._

class SurfaceViewOverlay extends SActivity {

  var mVictimContainer: View = _

  var mVictim1: View = _

  var mVictim2: View = _

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.surface_view_overlay)
    val glSurfaceView = findViewById(R.id.glsurfaceview).asInstanceOf[GLSurfaceView]
    glSurfaceView.setRenderer(new CubeRenderer(false))
    mVictimContainer = findViewById(R.id.hidecontainer)
    mVictim1 = findViewById(R.id.hideme1)
    mVictim1.setOnClickListener(new HideMeListener(mVictim1))
    mVictim2 = findViewById(R.id.hideme2)
    mVictim2.setOnClickListener(new HideMeListener(mVictim2))
    val visibleButton = findViewById(R.id.vis).asInstanceOf[Button]
    val invisibleButton = findViewById(R.id.invis).asInstanceOf[Button]
    val goneButton = findViewById(R.id.gone).asInstanceOf[Button]
    visibleButton.setOnClickListener(mVisibleListener)
    invisibleButton.setOnClickListener(mInvisibleListener)
    goneButton.setOnClickListener(mGoneListener)
  }

  class HideMeListener(val mTarget: View) extends OnClickListener {

    def onClick(v: View) {
      mTarget.setVisibility(View.INVISIBLE)
    }
  }

  var mVisibleListener: OnClickListener = new OnClickListener() {

    def onClick(v: View) {
      mVictim1.setVisibility(View.VISIBLE)
      mVictim2.setVisibility(View.VISIBLE)
      mVictimContainer.setVisibility(View.VISIBLE)
    }
  }

  var mInvisibleListener: OnClickListener = new OnClickListener() {

    def onClick(v: View) {
      mVictim1.setVisibility(View.INVISIBLE)
      mVictim2.setVisibility(View.INVISIBLE)
      mVictimContainer.setVisibility(View.INVISIBLE)
    }
  }

  var mGoneListener: OnClickListener = new OnClickListener() {

    def onClick(v: View) {
      mVictim1.setVisibility(View.GONE)
      mVictim2.setVisibility(View.GONE)
      mVictimContainer.setVisibility(View.GONE)
    }
  }
}
