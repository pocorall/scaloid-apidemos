package com.example.android.apis.view

import com.example.android.apis.R
import android.app.Activity
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.TextView
//remove if not needed
import scala.collection.JavaConversions._

class DragAndDropDemo extends Activity {

  var mResultText: TextView = _

  var mHiddenDot: DraggableDot = _

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.drag_layout)
    val text = findViewById(R.id.drag_text).asInstanceOf[TextView]
    var dot = findViewById(R.id.drag_dot_1).asInstanceOf[DraggableDot]
    dot.setReportView(text)
    dot = findViewById(R.id.drag_dot_2).asInstanceOf[DraggableDot]
    dot.setReportView(text)
    dot = findViewById(R.id.drag_dot_3).asInstanceOf[DraggableDot]
    dot.setReportView(text)
    mHiddenDot = findViewById(R.id.drag_dot_hidden).asInstanceOf[DraggableDot]
    mHiddenDot.setReportView(text)
    mResultText = findViewById(R.id.drag_result_text).asInstanceOf[TextView]
    mResultText.setOnDragListener(new View.OnDragListener() {

      def onDrag(v: View, event: DragEvent): Boolean = {
        val action = event.getAction
        action match {
          case DragEvent.ACTION_DRAG_STARTED => {
            mHiddenDot.setVisibility(View.VISIBLE)
          }
          case DragEvent.ACTION_DRAG_ENDED => {
            mHiddenDot.setVisibility(View.INVISIBLE)
            val dropped = event.getResult
            mResultText.setText(if (dropped) "Dropped!" else "No drop")
          }
        }
        false
      }
    })
  }
}
