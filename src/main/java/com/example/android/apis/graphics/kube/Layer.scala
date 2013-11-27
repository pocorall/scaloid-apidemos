package com.example.android.apis.graphics.kube

import Layer._
//remove if not needed
import scala.collection.JavaConversions._

object Layer {

  val kAxisX = 0

  val kAxisY = 1

  val kAxisZ = 2
}

class Layer(var mAxis: Int) {

  var mShapes: Array[GLShape] = new Array[GLShape](9)

  var mTransform: M4 = new M4()

  mTransform.setIdentity()

  def startAnimation() {
    for (i <- 0 until mShapes.length) {
      val shape = mShapes(i)
      if (shape != null) {
        shape.startAnimation()
      }
    }
  }

  def endAnimation() {
    for (i <- 0 until mShapes.length) {
      val shape = mShapes(i)
      if (shape != null) {
        shape.endAnimation()
      }
    }
  }

  def setAngle(angle: Float) {
    val twopi: Float = Math.PI.toFloat * 2f
    var angleVar : Float = angle
    while (angleVar >= twopi) angleVar = angleVar - twopi
    while (angleVar < 0f) angleVar += twopi
    val sin = Math.sin(angleVar).toFloat
    val cos = Math.cos(angleVar).toFloat
    val m = mTransform.m
    mAxis match {
      case kAxisX =>
        m(1)(1) = cos
        m(1)(2) = sin
        m(2)(1) = -sin
        m(2)(2) = cos
        m(0)(0) = 1f
        m(0)(1) = 0f
        m(0)(2) = 0f
        m(1)(0) = 0f
        m(2)(0) = 0f

      case kAxisY =>
        m(0)(0) = cos
        m(0)(2) = sin
        m(2)(0) = -sin
        m(2)(2) = cos
        m(1)(1) = 1f
        m(0)(1) = 0f
        m(1)(0) = 0f
        m(1)(2) = 0f
        m(2)(1) = 0f

      case kAxisZ =>
        m(0)(0) = cos
        m(0)(1) = sin
        m(1)(0) = -sin
        m(1)(1) = cos
        m(2)(2) = 1f
        m(2)(0) = 0f
        m(2)(1) = 0f
        m(0)(2) = 0f
        m(1)(2) = 0f

    }
    for (i <- 0 until mShapes.length) {
      val shape = mShapes(i)
      if (shape != null) {
        shape.animateTransform(mTransform)
      }
    }
  }
}
