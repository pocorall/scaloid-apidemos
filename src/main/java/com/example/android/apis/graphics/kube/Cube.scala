package com.example.android.apis.graphics.kube

import Cube._
//remove if not needed
import scala.collection.JavaConversions._

object Cube {

  val kBottom = 0

  val kFront = 1

  val kLeft = 2

  val kRight = 3

  val kBack = 4

  val kTop = 5
}

class Cube(world: GLWorld,
           left: Float,
           bottom: Float,
           back: Float,
           right: Float,
           top: Float,
           front: Float) extends GLShape(world) {

  val leftBottomBack = addVertex(left, bottom, back)

  val rightBottomBack = addVertex(right, bottom, back)

  val leftTopBack = addVertex(left, top, back)

  val rightTopBack = addVertex(right, top, back)

  val leftBottomFront = addVertex(left, bottom, front)

  val rightBottomFront = addVertex(right, bottom, front)

  val leftTopFront = addVertex(left, top, front)

  val rightTopFront = addVertex(right, top, front)

  addFace(new GLFace(leftBottomBack, leftBottomFront, rightBottomFront, rightBottomBack))

  addFace(new GLFace(leftBottomFront, leftTopFront, rightTopFront, rightBottomFront))

  addFace(new GLFace(leftBottomBack, leftTopBack, leftTopFront, leftBottomFront))

  addFace(new GLFace(rightBottomBack, rightBottomFront, rightTopFront, rightTopBack))

  addFace(new GLFace(leftBottomBack, rightBottomBack, rightTopBack, leftTopBack))

  addFace(new GLFace(leftTopBack, rightTopBack, rightTopFront, leftTopFront))
}
