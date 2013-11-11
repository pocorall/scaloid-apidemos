package com.example.android.apis.animation

import com.example.android.apis.R
import android.view.View
import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import org.scaloid.common.SActivity

//remove if not needed
import scala.collection.JavaConversions._

class LayoutAnimationsByDefault extends SActivity {

  private var numButtons: Int = 1

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.layout_animations_by_default)
    val gridContainer = findViewById(R.id.gridContainer).asInstanceOf[GridLayout]
    val addButton = findViewById(R.id.addNewButton).asInstanceOf[Button]
    addButton.setOnClickListener(new View.OnClickListener() {

      def onClick(v: View) {
        val newButton = new Button(LayoutAnimationsByDefault.this)
        newButton.setText(String.valueOf(numButtons += 1))
        newButton.setOnClickListener(new View.OnClickListener() {

          def onClick(v: View) {
            gridContainer.removeView(v)
          }
        })
        gridContainer.addView(newButton, Math.min(1, gridContainer.getChildCount))
      }
    })
  }
}
