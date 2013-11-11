package com.example.android.apis.animation

import android.view.animation.Animation
import android.view.animation.Transformation
import android.graphics.Camera
import android.graphics.Matrix
//remove if not needed
import scala.collection.JavaConversions._

class Rotate3dAnimation(private val mFromDegrees: Float,
                        private val mToDegrees: Float,
                        private val mCenterX: Float,
                        private val mCenterY: Float,
                        private val mDepthZ: Float,
                        private val mReverse: Boolean) extends Animation {

  private var mCamera: Camera = _

  override def initialize(width: Int,
                          height: Int,
                          parentWidth: Int,
                          parentHeight: Int) {
    super.initialize(width, height, parentWidth, parentHeight)
    mCamera = new Camera()
  }

  protected override def applyTransformation(interpolatedTime: Float, t: Transformation) {
    val fromDegrees = mFromDegrees
    val degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime)
    val centerX = mCenterX
    val centerY = mCenterY
    val camera = mCamera
    val matrix = t.getMatrix
    camera.save()
    if (mReverse) {
      camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime)
    } else {
      camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime))
    }
    camera.rotateY(degrees)
    camera.getMatrix(matrix)
    camera.restore()
    matrix.preTranslate(-centerX, -centerY)
    matrix.postTranslate(centerX, centerY)
  }
}
