package com.example.android.apis.graphics.kube

import android.app.Activity
import android.os.Bundle
import android.view.Window
import android.opengl.GLSurfaceView
import java.util.Random
import Kube._
//remove if not needed
import scala.collection.JavaConversions._

object Kube {

  var mLayerPermutations: Array[Array[Int]] = Array(Array(2, 5, 8, 1, 4, 7, 0, 3, 6, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26), Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 20, 23, 26, 19, 22, 25, 18, 21, 24), Array(6, 1, 2, 15, 4, 5, 24, 7, 8, 3, 10, 11, 12, 13, 14, 21, 16, 17, 0, 19, 20, 9, 22, 23, 18, 25, 26), Array(0, 1, 8, 3, 4, 17, 6, 7, 26, 9, 10, 5, 12, 13, 14, 15, 16, 23, 18, 19, 2, 21, 22, 11, 24, 25, 20), Array(0, 1, 2, 3, 4, 5, 24, 15, 6, 9, 10, 11, 12, 13, 14, 25, 16, 7, 18, 19, 20, 21, 22, 23, 26, 17, 8), Array(18, 9, 0, 3, 4, 5, 6, 7, 8, 19, 10, 1, 12, 13, 14, 15, 16, 17, 20, 11, 2, 21, 22, 23, 24, 25, 26), Array(0, 7, 2, 3, 16, 5, 6, 25, 8, 9, 4, 11, 12, 13, 14, 15, 22, 17, 18, 1, 20, 21, 10, 23, 24, 19, 26), Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 11, 14, 17, 10, 13, 16, 9, 12, 15, 18, 19, 20, 21, 22, 23, 24, 25, 26), Array(0, 1, 2, 21, 12, 3, 6, 7, 8, 9, 10, 11, 22, 13, 4, 15, 16, 17, 18, 19, 20, 23, 14, 5, 24, 25, 26))

  val kUp = 0

  val kDown = 1

  val kLeft = 2

  val kRight = 3

  val kFront = 4

  val kBack = 5

  val kMiddle = 6

  val kEquator = 7

  val kSide = 8
}

class Kube extends Activity with KubeRenderer.AnimationCallback {
  var mView: GLSurfaceView = _

  var mRenderer: KubeRenderer = _

  var mCubes: Array[Cube] = new Array[Cube](27)

  var mLayers: Array[Layer] = new Array[Layer](9)

  var mPermutation: Array[Int] = _

  var mRandom: Random = new Random(System.currentTimeMillis())

  var mCurrentLayer: Layer = null

  var mCurrentAngle: Float = _

  var mEndAngle: Float = _

  var mAngleIncrement: Float = _

  var mCurrentLayerPermutation: Array[Int] = _

  private def makeGLWorld(): GLWorld = {
    val world = new GLWorld()
    val one = 0x10000
    val half = 0x08000
    val red = new GLColor(one, 0, 0)
    val green = new GLColor(0, one, 0)
    val blue = new GLColor(0, 0, one)
    val yellow = new GLColor(one, one, 0)
    val orange = new GLColor(one, half, 0)
    val white = new GLColor(one, one, one)
    val black = new GLColor(0, 0, 0)
    val c0 = -1.0f
    val c1 = -0.38f
    val c2 = -0.32f
    val c3 = 0.32f
    val c4 = 0.38f
    val c5 = 1.0f
    mCubes(0) = new Cube(world, c0, c4, c0, c1, c5, c1)
    mCubes(1) = new Cube(world, c2, c4, c0, c3, c5, c1)
    mCubes(2) = new Cube(world, c4, c4, c0, c5, c5, c1)
    mCubes(3) = new Cube(world, c0, c4, c2, c1, c5, c3)
    mCubes(4) = new Cube(world, c2, c4, c2, c3, c5, c3)
    mCubes(5) = new Cube(world, c4, c4, c2, c5, c5, c3)
    mCubes(6) = new Cube(world, c0, c4, c4, c1, c5, c5)
    mCubes(7) = new Cube(world, c2, c4, c4, c3, c5, c5)
    mCubes(8) = new Cube(world, c4, c4, c4, c5, c5, c5)
    mCubes(9) = new Cube(world, c0, c2, c0, c1, c3, c1)
    mCubes(10) = new Cube(world, c2, c2, c0, c3, c3, c1)
    mCubes(11) = new Cube(world, c4, c2, c0, c5, c3, c1)
    mCubes(12) = new Cube(world, c0, c2, c2, c1, c3, c3)
    mCubes(13) = null
    mCubes(14) = new Cube(world, c4, c2, c2, c5, c3, c3)
    mCubes(15) = new Cube(world, c0, c2, c4, c1, c3, c5)
    mCubes(16) = new Cube(world, c2, c2, c4, c3, c3, c5)
    mCubes(17) = new Cube(world, c4, c2, c4, c5, c3, c5)
    mCubes(18) = new Cube(world, c0, c0, c0, c1, c1, c1)
    mCubes(19) = new Cube(world, c2, c0, c0, c3, c1, c1)
    mCubes(20) = new Cube(world, c4, c0, c0, c5, c1, c1)
    mCubes(21) = new Cube(world, c0, c0, c2, c1, c1, c3)
    mCubes(22) = new Cube(world, c2, c0, c2, c3, c1, c3)
    mCubes(23) = new Cube(world, c4, c0, c2, c5, c1, c3)
    mCubes(24) = new Cube(world, c0, c0, c4, c1, c1, c5)
    mCubes(25) = new Cube(world, c2, c0, c4, c3, c1, c5)
    mCubes(26) = new Cube(world, c4, c0, c4, c5, c1, c5)
    var i: Int = 0
    var j: Int = 0
    i = 0
    while (i < 27) {
      val cube = mCubes(i)
      if (cube != null) {
        j = 0
        while (j < 6) {
          cube.setFaceColor(j, black);j += 1
        }
      }
      i += 1
    }
    i = 0
    while (i < 9) {mCubes(i).setFaceColor(Cube.kTop, orange);i += 1}
    i = 18
    while (i < 27) {mCubes(i).setFaceColor(Cube.kBottom, red);i += 1}
    i = 0
    while (i < 27) {mCubes(i).setFaceColor(Cube.kLeft, yellow);i += 3}
    i = 2
    while (i < 27) {mCubes(i).setFaceColor(Cube.kRight, white);i += 3}
    i = 0
    while (i < 27) {j = 0
      while (j < 3) {mCubes(i + j).setFaceColor(Cube.kBack, blue);j += 1}
      i += 9
    }
    i = 6
    while (i < 27) {j = 0
      while (j < 3) {mCubes(i + j).setFaceColor(Cube.kFront, green);j += 1
      };i += 9
    }
    i = 0
    while (i < 27) {if (mCubes(i) != null) world.addShape(mCubes(i));i += 1
    }
    mPermutation = Array.ofDim[Int](27)
    i = 0
    while (i < mPermutation.length) {mPermutation(i) = i;i += 1
    }
    createLayers()
    updateLayers()
    world.generate()
    world
  }

  private def createLayers() {
    mLayers(kUp) = new Layer(Layer.kAxisY)
    mLayers(kDown) = new Layer(Layer.kAxisY)
    mLayers(kLeft) = new Layer(Layer.kAxisX)
    mLayers(kRight) = new Layer(Layer.kAxisX)
    mLayers(kFront) = new Layer(Layer.kAxisZ)
    mLayers(kBack) = new Layer(Layer.kAxisZ)
    mLayers(kMiddle) = new Layer(Layer.kAxisX)
    mLayers(kEquator) = new Layer(Layer.kAxisY)
    mLayers(kSide) = new Layer(Layer.kAxisZ)
  }

  private def updateLayers() {
    var layer: Layer = null
    var shapes: Array[GLShape] = null
    var i: Int = 0
    var j: Int = 0
    var k: Int = 0
    layer = mLayers(kUp)
    shapes = layer.mShapes
    i = 0
    while (i < 9) {shapes(i) = mCubes(mPermutation(i));i += 1
    }
    layer = mLayers(kDown)
    shapes = layer.mShapes
    i = 18
    k = 0
    while (i < 27) { shapes(k) = mCubes(mPermutation(i));i += 1 ;k += 1;
    }
    layer = mLayers(kLeft)
    shapes = layer.mShapes
    i = 0
    k = 0
    while (i < 27) {j = 0
      while (j < 9) { shapes(k) = mCubes(mPermutation(i + j));j += 3       ;k += 1;
      };i += 9
    }
    layer = mLayers(kRight)
    shapes = layer.mShapes
    i = 2
    k = 0
    while (i < 27) {j = 0
      while (j < 9) {shapes(k) = mCubes(mPermutation(i + j));j += 3 ;   k += 1;
      };i += 9
    }
    layer = mLayers(kFront)
    shapes = layer.mShapes
    i = 6
    k = 0
    while (i < 27) {j = 0
      while (j < 3) { shapes(k) = mCubes(mPermutation(i + j));j += 1;k += 1;
      };i += 9
    }
    layer = mLayers(kBack)
    shapes = layer.mShapes
    i = 0
    k = 0
    while (i < 27) {j = 0
      while (j < 3) { shapes(k) = mCubes(mPermutation(i + j));j += 1  ; k += 1;
      };i += 9
    }
    layer = mLayers(kMiddle)
    shapes = layer.mShapes
    i = 1
    k = 0
    while (i < 27) {j = 0
      while (j < 9) {shapes(k) = mCubes(mPermutation(i + j));j += 3; k += 1;
      };i += 9
    }
    layer = mLayers(kEquator)
    shapes = layer.mShapes
    i = 9
    k = 0
    while (i < 18) { shapes(k) = mCubes(mPermutation(i));i += 1   ; k += 1;
    }
    layer = mLayers(kSide)
    shapes = layer.mShapes
    i = 3
    k = 0
    while (i < 27) {j = 0
      while (j < 3) { shapes(k) = mCubes(mPermutation(i + j));j += 1   ; k += 1;
      };i += 9
    }
  }

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    mView = new GLSurfaceView(getApplication)
    mRenderer = new KubeRenderer(makeGLWorld(), this)
    mView.setRenderer(mRenderer)
    setContentView(mView)
  }

  protected override def onResume() {
    super.onResume()
    mView.onResume()
  }

  protected override def onPause() {
    super.onPause()
    mView.onPause()
  }

  def animate() {
    mRenderer.setAngle(mRenderer.getAngle + 1.2f)
    if (mCurrentLayer == null) {
      val layerID = mRandom.nextInt(9)
      mCurrentLayer = mLayers(layerID)
      mCurrentLayerPermutation = mLayerPermutations(layerID)
      mCurrentLayer.startAnimation()
      var direction = mRandom.nextBoolean()
      var count = mRandom.nextInt(3) + 1
      count = 1
      direction = false
      mCurrentAngle = 0
      if (direction) {
        mAngleIncrement = Math.PI.toFloat / 50
        mEndAngle = mCurrentAngle + (Math.PI.toFloat * count) / 2f
      } else {
        mAngleIncrement = -Math.PI.toFloat / 50
        mEndAngle = mCurrentAngle - (Math.PI.toFloat * count) / 2f
      }
    }
    mCurrentAngle += mAngleIncrement
    if ((mAngleIncrement > 0f && mCurrentAngle >= mEndAngle) ||
      (mAngleIncrement < 0f && mCurrentAngle <= mEndAngle)) {
      mCurrentLayer.setAngle(mEndAngle)
      mCurrentLayer.endAnimation()
      mCurrentLayer = null
      val newPermutation = Array.ofDim[Int](27)
      for (i <- 0 until 27) {
        newPermutation(i) = mPermutation(mCurrentLayerPermutation(i))
      }
      mPermutation = newPermutation
      updateLayers()
    } else {
      mCurrentLayer.setAngle(mCurrentAngle)
    }
  }


}
