package com.example.android.apis.graphics.kube

import java.nio.IntBuffer
import GLVertex._
//remove if not needed
import scala.collection.JavaConversions._

object GLVertex {

  def toFixed(x: Float): Int = (x * 65536.0f).toInt
}

class GLVertex() {

  var x: Float = 0

  var y: Float = 0

  var z: Float = 0

  var index = -1

  var color: GLColor = _

  def this(x: Float,
           y: Float,
           z: Float,
           index: Int) {
    this()
    this.x = x
    this.y = y
    this.z = z
    this.index = index.toShort
  }

  override def equals(other: Any): Boolean = {
    if (other.isInstanceOf[GLVertex]) {
      val v = other.asInstanceOf[GLVertex]
      return (x == v.x && y == v.y && z == v.z)
    }
    false
  }

  def put(vertexBuffer: IntBuffer, colorBuffer: IntBuffer) {
    vertexBuffer.put(toFixed(x))
    vertexBuffer.put(toFixed(y))
    vertexBuffer.put(toFixed(z))
    if (color == null) {
      colorBuffer.put(0)
      colorBuffer.put(0)
      colorBuffer.put(0)
      colorBuffer.put(0)
    } else {
      colorBuffer.put(color.red)
      colorBuffer.put(color.green)
      colorBuffer.put(color.blue)
      colorBuffer.put(color.alpha)
    }
  }

  def update(vertexBuffer: IntBuffer, transform: M4) {
    vertexBuffer.position(index * 3)
    if (transform == null) {
      vertexBuffer.put(toFixed(x))
      vertexBuffer.put(toFixed(y))
      vertexBuffer.put(toFixed(z))
    } else {
      val temp = new GLVertex()
      transform.multiply(this, temp)
      vertexBuffer.put(toFixed(temp.x))
      vertexBuffer.put(toFixed(temp.y))
      vertexBuffer.put(toFixed(temp.z))
    }
  }
}
