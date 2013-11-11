package com.example.android.apis.animation

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
//remove if not needed
import scala.collection.JavaConversions._
import android.view.View._

class FixedGridLayout(context: Context) extends ViewGroup(context) {

 var mCellWidth: Int = _

  var mCellHeight: Int = _

  def setCellWidth(px: Int) {
    mCellWidth = px
    requestLayout()
  }

  def setCellHeight(px: Int) {
    mCellHeight = px
    requestLayout()
  }

  protected override def onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val cellWidthSpec = MeasureSpec.makeMeasureSpec(mCellWidth, MeasureSpec.AT_MOST)
    val cellHeightSpec = MeasureSpec.makeMeasureSpec(mCellHeight, MeasureSpec.AT_MOST)
    val count = getChildCount
    for (index <- 0 until count) {
      val child = getChildAt(index)
      child.measure(cellWidthSpec, cellHeightSpec)
    }
    val minCount = if (count > 3) count else 3
    setMeasuredDimension(resolveSize(mCellWidth * minCount, widthMeasureSpec), resolveSize(mCellHeight * minCount,
      heightMeasureSpec))
  }

  protected override def onLayout(changed: Boolean,
                                  l: Int,
                                  t: Int,
                                  r: Int,
                                  b: Int) {
    val cellWidth = mCellWidth
    val cellHeight = mCellHeight
    var columns = (r - l) / cellWidth
    if (columns < 0) {
      columns = 1
    }
    var x = 0
    var y = 0
    var i = 0
    val count = getChildCount
    for (index <- 0 until count) {
      val child = getChildAt(index)
      val w = child.getMeasuredWidth
      val h = child.getMeasuredHeight
      val left = x + ((cellWidth - w) / 2)
      val top = y + ((cellHeight - h) / 2)
      child.layout(left, top, left + w, top + h)
      if (i >= (columns - 1)) {
        i = 0
        x = 0
        y += cellHeight
      } else {
        i += 1
        x += cellWidth
      }
    }
  }
}
