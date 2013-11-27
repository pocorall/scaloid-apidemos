package com.example.android.apis.graphics.kube

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer
import java.nio.ShortBuffer
import java.util.Iterator
import java.util.ArrayList
import javax.microedition.khronos.opengles.GL10
import GLWorld._
//remove if not needed
import scala.collection.JavaConversions._

object GLWorld {

  def toFloat(x: Int): Float = x / 65536.0f
}

class GLWorld {
  private var mShapeList: ArrayList[GLShape] = new ArrayList[GLShape]()

  private var mVertexList: ArrayList[GLVertex] = new ArrayList[GLVertex]()

  private var mIndexCount: Int = 0

  private var mVertexBuffer: IntBuffer = _

  private var mColorBuffer: IntBuffer = _

  private var mIndexBuffer: ShortBuffer = _


  def addShape(shape: GLShape) {
    mShapeList.add(shape)
    mIndexCount += shape.getIndexCount
  }

  def generate() {
    var bb = ByteBuffer.allocateDirect(mVertexList.size * 4 * 4)
    bb.order(ByteOrder.nativeOrder())
    mColorBuffer = bb.asIntBuffer()
    bb = ByteBuffer.allocateDirect(mVertexList.size * 4 * 3)
    bb.order(ByteOrder.nativeOrder())
    mVertexBuffer = bb.asIntBuffer()
    bb = ByteBuffer.allocateDirect(mIndexCount * 2)
    bb.order(ByteOrder.nativeOrder())
    mIndexBuffer = bb.asShortBuffer()
    val iter2 = mVertexList.iterator()
    while (iter2.hasNext) {
      val vertex = iter2.next()
      vertex.put(mVertexBuffer, mColorBuffer)
    }
    val iter3 = mShapeList.iterator()
    while (iter3.hasNext) {
      val shape = iter3.next()
      shape.putIndices(mIndexBuffer)
    }
  }

  def addVertex(x: Float, y: Float, z: Float): GLVertex = {
    val vertex = new GLVertex(x, y, z, mVertexList.size)
    mVertexList.add(vertex)
    vertex
  }

  def transformVertex(vertex: GLVertex, transform: M4) {
    vertex.update(mVertexBuffer, transform)
  }

  var count: Int = 0

  def draw(gl: GL10) {
    mColorBuffer.position(0)
    mVertexBuffer.position(0)
    mIndexBuffer.position(0)
    gl.glFrontFace(GL10.GL_CW)
    gl.glShadeModel(GL10.GL_FLAT)
    gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer)
    gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer)
    gl.glDrawElements(GL10.GL_TRIANGLES, mIndexCount, GL10.GL_UNSIGNED_SHORT, mIndexBuffer)
    count += 1
  }


}
