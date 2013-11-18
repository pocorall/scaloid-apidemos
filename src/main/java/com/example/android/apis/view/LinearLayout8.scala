package com.example.android.apis.view

import com.example.android.apis.R
import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.Gravity
import android.view.MenuItem
import android.widget.LinearLayout
import LinearLayout8._
//remove if not needed
import scala.collection.JavaConversions._

object LinearLayout8 {

  val VERTICAL_ID = Menu.FIRST

  val HORIZONTAL_ID = Menu.FIRST + 1

  val TOP_ID = Menu.FIRST + 2

  val MIDDLE_ID = Menu.FIRST + 3

  val BOTTOM_ID = Menu.FIRST + 4

  val LEFT_ID = Menu.FIRST + 5

  val CENTER_ID = Menu.FIRST + 6

  val RIGHT_ID = Menu.FIRST + 7
}

class LinearLayout8 extends Activity {

  private var mLinearLayout: LinearLayout = _

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.linear_layout_8)
    mLinearLayout = findViewById(R.id.layout).asInstanceOf[LinearLayout]
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    super.onCreateOptionsMenu(menu)
    menu.add(0, VERTICAL_ID, 0, R.string.linear_layout_8_vertical)
    menu.add(0, HORIZONTAL_ID, 0, R.string.linear_layout_8_horizontal)
    menu.add(0, TOP_ID, 0, R.string.linear_layout_8_top)
    menu.add(0, MIDDLE_ID, 0, R.string.linear_layout_8_middle)
    menu.add(0, BOTTOM_ID, 0, R.string.linear_layout_8_bottom)
    menu.add(0, LEFT_ID, 0, R.string.linear_layout_8_left)
    menu.add(0, CENTER_ID, 0, R.string.linear_layout_8_center)
    menu.add(0, RIGHT_ID, 0, R.string.linear_layout_8_right)
    true
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = item.getItemId match {
    case VERTICAL_ID =>
      mLinearLayout.setOrientation(LinearLayout.VERTICAL)
      true

    case HORIZONTAL_ID =>
      mLinearLayout.setOrientation(LinearLayout.HORIZONTAL)
      true

    case TOP_ID =>
      mLinearLayout.setVerticalGravity(Gravity.TOP)
      true

    case MIDDLE_ID =>
      mLinearLayout.setVerticalGravity(Gravity.CENTER_VERTICAL)
      true

    case BOTTOM_ID =>
      mLinearLayout.setVerticalGravity(Gravity.BOTTOM)
      true

    case LEFT_ID =>
      mLinearLayout.setHorizontalGravity(Gravity.LEFT)
      true

    case CENTER_ID =>
      mLinearLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL)
      true

    case RIGHT_ID =>
      mLinearLayout.setHorizontalGravity(Gravity.RIGHT)
      true

  }
}
