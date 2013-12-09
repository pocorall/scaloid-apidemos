package com.example.android.apis.graphics

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer
import javax.microedition.khronos.opengles.GL10
//remove if not needed
import scala.collection.JavaConversions._

class Cube {

  val one = 0x10000

  val vertices = Array(-one, -one, -one, one, -one, -one, one, one, -one, -one, one, -one, -one, -one, one, one, -one, one, one, one, one, -one, one, one)

  val colors = Array(0, 0, 0, one, one, 0, 0, one, one, one, 0, one, 0, one, 0, one, 0, 0, one, one, one, 0, one, one, one, one, one, one, 0, one, one, one)

  val indices = Array(0, 4, 5, 0, 5, 1, 1, 5, 6, 1, 6, 2, 2, 6, 7, 2, 7, 3, 3, 7, 4, 3, 4, 0, 4, 7, 6, 4, 6, 5, 3, 0, 1, 3, 1, 2)

  val vbb = ByteBuffer.allocateDirect(vertices.length * 4)

  vbb.order(ByteOrder.nativeOrder())

  private var mVertexBuffer: IntBuffer = vbb.asIntBuffer()

  private var mColorBuffer: IntBuffer = cbb.asIntBuffer()

  private var mIndexBuffer: ByteBuffer = ByteBuffer.allocateDirect(indices.length)

  mVertexBuffer.put(vertices)

  mVertexBuffer.position(0)

  val cbb = ByteBuffer.allocateDirect(colors.length * 4)

  cbb.order(ByteOrder.nativeOrder())

  mColorBuffer.put(colors)

  mColorBuffer.position(0)

  mIndexBuffer.put(indices.toString.toByte)

  mIndexBuffer.position(0)

  def draw(gl: GL10) {
    gl.glFrontFace(GL10.GL_CW)
    gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer)
    gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer)
    gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE, mIndexBuffer)
  }
}
