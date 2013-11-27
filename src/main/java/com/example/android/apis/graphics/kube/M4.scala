package com.example.android.apis.graphics.kube

//remove if not needed
import scala.collection.JavaConversions._

class M4 {

  var m: Array[Array[Float]] = Array.ofDim[Float](4, 4)

  def this(other: M4 = null) {
    this()

    if ( other == null ) {
    } else {


      for (i <- 0 until 4; j <- 0 until 4) {
        m(i)(j) = other.m(i)(j)
      }
    }
  }

  def multiply(src: GLVertex, dest: GLVertex) {
    dest.x = src.x * m(0)(0) + src.y * m(1)(0) + src.z * m(2)(0) +
      m(3)(0)
    dest.y = src.x * m(0)(1) + src.y * m(1)(1) + src.z * m(2)(1) +
      m(3)(1)
    dest.z = src.x * m(0)(2) + src.y * m(1)(2) + src.z * m(2)(2) +
      m(3)(2)
  }

  def multiply(other: M4): M4 = {
    val result = new M4()
    val m1 = m
    val m2 = other.m
    for (i <- 0 until 4; j <- 0 until 4) {
      result.m(i)(j) = m1(i)(0) * m2(0)(j) + m1(i)(1) * m2(1)(j) + m1(i)(2) * m2(2)(j) +
        m1(i)(3) * m2(3)(j)
    }
    result
  }

  def setIdentity() {
    for (i <- 0 until 4; j <- 0 until 4) {
      m(i)(j) = (if (i == j) 1f else 0f)
    }
  }

  override def toString(): String = {
    val builder = new StringBuilder("[ ")
    for (i <- 0 until 4) {
      for (j <- 0 until 4) {
        builder.append(m(i)(j))
        builder.append(" ")
      }
      if (i < 2) builder.append("\n  ")
    }
    builder.append(" ]")
    builder.toString
  }
}
