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
import android.app.{FragmentTransaction, ActionBar}
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.app.ActionBar._
import Gravity._
import org.scaloid.common._

/**
 * This demo shows how various action bar display option flags can be combined and their effects.
 */
class ActionBarDisplayOptions extends SActivity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    val bar = getActionBar
    def displayFlag(flags: Int) = bar.setDisplayOptions(bar.getDisplayOptions ^ flags, flags)

    def onCustomGravityClicked = {
      val lp = mCustomView.getLayoutParams.asInstanceOf[ActionBar.LayoutParams]
      val newGravity: Int = (lp.gravity & HORIZONTAL_GRAVITY_MASK) match {
        case LEFT => CENTER_HORIZONTAL
        case CENTER_HORIZONTAL => RIGHT
        case RIGHT => LEFT
      }
      lp.gravity = lp.gravity & ~HORIZONTAL_GRAVITY_MASK | newGravity
      bar.setCustomView(mCustomView, lp)
    }

    contentView = new SVerticalLayout {
      SButton(R.string.toggle_home_as_up, displayFlag(DISPLAY_HOME_AS_UP)).<<.wrap
      SButton(R.string.toggle_show_home, displayFlag(DISPLAY_SHOW_HOME)).<<.wrap
      SButton(R.string.toggle_use_logo, displayFlag(DISPLAY_USE_LOGO)).<<.wrap
      SButton(R.string.toggle_show_title, displayFlag(DISPLAY_SHOW_TITLE)).<<.wrap
      SButton(R.string.toggle_show_custom, displayFlag(DISPLAY_SHOW_CUSTOM)).<<.wrap
      SButton(R.string.toggle_navigation, (bar.setNavigationMode(
        if (bar.getNavigationMode == NAVIGATION_MODE_STANDARD) NAVIGATION_MODE_TABS else NAVIGATION_MODE_STANDARD))).<<.wrap
      SButton(R.string.cycle_custom_gravity, onCustomGravityClicked _).<<.wrap
    }

    mCustomView = getLayoutInflater.inflate(R.layout.action_bar_display_options_custom, null)

    bar.setCustomView(mCustomView, new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
    val nullListener = new ActionBar.TabListener {
      def onTabSelected(p1: Tab, p2: FragmentTransaction) {}

      def onTabUnselected(p1: Tab, p2: FragmentTransaction) {}

      def onTabReselected(p1: Tab, p2: FragmentTransaction) {}
    }
    bar.addTab(bar.newTab.setText("Tab 1").setTabListener(nullListener))
    bar.addTab(bar.newTab.setText("Tab 2").setTabListener(nullListener))
    bar.addTab(bar.newTab.setText("Tab 3").setTabListener(nullListener))
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.display_options_actions, menu)
    true
  }

  private var mCustomView: View = null
}