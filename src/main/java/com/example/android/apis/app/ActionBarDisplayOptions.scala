/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.apis.app

import com.example.android.apis.R
import android.app.{ActionBar, Activity, FragmentTransaction}
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.app.ActionBar._
import Gravity._
import net.pocorall.android.common._

/**
 * This demo shows how various action bar display option flags can be combined and their effects.
 */
class ActionBarDisplayOptions extends Activity with ContextUtil {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.action_bar_display_options)
    val bar = getActionBar
    def c(flags: Int) {
      bar.setDisplayOptions(bar.getDisplayOptions ^ flags, flags)
    }
    findViewById(R.id.toggle_home_as_up).onClick(c(DISPLAY_HOME_AS_UP))
    findViewById(R.id.toggle_show_home).onClick(c(DISPLAY_SHOW_HOME))
    findViewById(R.id.toggle_use_logo).onClick(c(DISPLAY_USE_LOGO))
    findViewById(R.id.toggle_show_title).onClick(c(DISPLAY_SHOW_TITLE))
    findViewById(R.id.toggle_show_custom).onClick(c(DISPLAY_SHOW_CUSTOM))
    findViewById(R.id.toggle_navigation).onClick(bar.setNavigationMode(
      if (bar.getNavigationMode == NAVIGATION_MODE_STANDARD)
        NAVIGATION_MODE_TABS
      else NAVIGATION_MODE_STANDARD))
    findViewById(R.id.cycle_custom_gravity).onClick {
      val lp = mCustomView.getLayoutParams.asInstanceOf[ActionBar.LayoutParams]
      val newGravity: Int = (lp.gravity & HORIZONTAL_GRAVITY_MASK) match {
        case LEFT => CENTER_HORIZONTAL
        case CENTER_HORIZONTAL => RIGHT
        case RIGHT => LEFT
      }
      lp.gravity = lp.gravity & ~HORIZONTAL_GRAVITY_MASK | newGravity
      bar.setCustomView(mCustomView, lp)
    }
    mCustomView = getLayoutInflater.inflate(R.layout.action_bar_display_options_custom, null)

    bar.setCustomView(mCustomView, new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
    bar.addTab(bar.newTab.setText("Tab 1"))
    bar.addTab(bar.newTab.setText("Tab 2"))
    bar.addTab(bar.newTab.setText("Tab 3"))
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.display_options_actions, menu)
    true
  }

  private var mCustomView: View = null
}