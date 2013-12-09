package com.example.android.apis.graphics.kube

import java.nio.ShortBuffer
import java.util.ArrayList
import java.util.Iterator
//remove if not needed
import scala.collection.JavaConversions._

class GLShape(protected var mWorld: GLWorld) {
  var mTransform: M4 = _

  var mAnimateTransform: M4 = _

  protected var mFaceList: ArrayList[GLFace] = new ArrayList[GLFace]()

  protected var mVertexList: ArrayList[GLVertex] = new ArrayList[GLVertex]()

  protected var mIndexList: ArrayList[Integer] = new ArrayList[Integer]()

  def addFace(face: GLFace) {
    mFaceList.add(face)
  }

  def setFaceColor(face: Int, color: GLColor) {
    mFaceList.get(face).setColor(color)
  }

  def putIndices(buffer: ShortBuffer) {
    val iter = mFaceList.iterator()
    while (iter.hasNext) {
      val face = iter.next()
      face.putIndices(buffer)
    }
  }

  def getIndexCount(): Int = {
    var count = 0
    val iter = mFaceList.iterator()
    while (iter.hasNext) {
      val face = iter.next()
      count += face.getIndexCount
    }
    count
  }

  def addVertex(x: Float, y: Float, z: Float): GLVertex = {
    val iter = mVertexList.iterator()
    while (iter.hasNext) {
      val vertex = iter.next()
      if (vertex.x == x && vertex.y == y && vertex.z == z) {
        return vertex
      }
    }
    val vertex = mWorld.addVertex(x, y, z)
    mVertexList.add(vertex)
    vertex
  }

  def animateTransform(transform: M4) {
    mAnimateTransform = transform
    var transformVar = transform
    if (mTransform != null) transformVar = mTransform.multiply(transformVar)
    val iter = mVertexList.iterator()
    while (iter.hasNext) {
      val vertex = iter.next()
      mWorld.transformVertex(vertex, transformVar)
    }
  }

  def startAnimation() {
  }

  def endAnimation() {
    mTransform = if (mTransform == null) new M4(mAnimateTransform) else mTransform.multiply(mAnimateTransform)
  }


}
