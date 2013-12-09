package com.example.android.apis.graphics.kube

import android.util.Log
import java.nio.ShortBuffer
import java.util.ArrayList
//remove if not needed
import scala.collection.JavaConversions._

class GLFace {


  private var mVertexList: ArrayList[GLVertex] = new ArrayList[GLVertex]()

  private var mColor: GLColor = _

  def this(v1: GLVertex, v2: GLVertex, v3: GLVertex) {
    this()
    addVertex(v1)
    addVertex(v2)
    addVertex(v3)
  }

  def this(v1: GLVertex,
           v2: GLVertex,
           v3: GLVertex,
           v4: GLVertex) {
    this()
    addVertex(v1)
    addVertex(v2)
    addVertex(v3)
    addVertex(v4)
  }

  def addVertex(v: GLVertex) {
    mVertexList.add(v)
  }

  def setColor(c: GLColor) {
    val last = mVertexList.size - 1
    if (last < 2) {
      Log.e("GLFace", "not enough vertices in setColor()")
    } else {
      var vertex = mVertexList.get(last)
      if (mColor == null) {
        while (vertex.color != null) {
          mVertexList.add(0, vertex)
          mVertexList.remove(last + 1)
          vertex = mVertexList.get(last)
        }
      }
      vertex.color = c
    }
    mColor = c
  }

  def getIndexCount(): Int = (mVertexList.size - 2) * 3

  def putIndices(buffer: ShortBuffer) {
    val last = mVertexList.size - 1
    var v0 = mVertexList.get(0)
    val vn = mVertexList.get(last)
    for (i <- 1 until last) {
      val v1 = mVertexList.get(i)
      buffer.put(v0.index.toShort)
      buffer.put(v1.index.toShort)
      buffer.put(vn.index.toShort)
      v0 = v1
    }
  }

}
