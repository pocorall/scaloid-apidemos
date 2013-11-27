package com.example.android.apis.graphics.kube

//remove if not needed
import scala.collection.JavaConversions._

class GLColor(val red: Int,
              val green: Int,
              val blue: Int,
              val alpha: Int = 0x10000) {

  override def equals(other: Any): Boolean = {
    if (other.isInstanceOf[GLColor]) {
      val color = other.asInstanceOf[GLColor]
      return (red == color.red && green == color.green && blue == color.blue &&
        alpha == color.alpha)
    }
    false
  }
}
