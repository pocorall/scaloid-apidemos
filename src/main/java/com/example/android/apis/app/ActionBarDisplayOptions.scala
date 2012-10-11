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
import android.app.ActionBar
import android.app.ActionBar.Tab
import android.app.Activity
import android.app.FragmentTransaction
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.view.ViewGroup.LayoutParams

/**
 * This demo shows how various action bar display option flags can be combined and their effects.
 */
class ActionBarDisplayOptions extends Activity with View.OnClickListener with ActionBar.TabListener {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.action_bar_display_options)
    findViewById(R.id.toggle_home_as_up).setOnClickListener(this)
    findViewById(R.id.toggle_show_home).setOnClickListener(this)
    findViewById(R.id.toggle_use_logo).setOnClickListener(this)
    findViewById(R.id.toggle_show_title).setOnClickListener(this)
    findViewById(R.id.toggle_show_custom).setOnClickListener(this)
    findViewById(R.id.toggle_navigation).setOnClickListener(this)
    findViewById(R.id.cycle_custom_gravity).setOnClickListener(this)
    mCustomView = getLayoutInflater.inflate(R.layout.action_bar_display_options_custom, null)
    val bar = getActionBar
    bar.setCustomView(mCustomView, new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
    bar.addTab(bar.newTab.setText("Tab 1").setTabListener(this))
    bar.addTab(bar.newTab.setText("Tab 2").setTabListener(this))
    bar.addTab(bar.newTab.setText("Tab 3").setTabListener(this))
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.display_options_actions, menu)
    return true
  }

  def onClick(v: View) {
    val bar = getActionBar
    var flags: Int = 0
    v.getId match {
      case R.id.toggle_home_as_up => flags = ActionBar.DISPLAY_HOME_AS_UP
      case R.id.toggle_show_home => flags = ActionBar.DISPLAY_SHOW_HOME
      case R.id.toggle_use_logo => flags = ActionBar.DISPLAY_USE_LOGO
      case R.id.toggle_show_title => flags = ActionBar.DISPLAY_SHOW_TITLE
      case R.id.toggle_show_custom => flags = ActionBar.DISPLAY_SHOW_CUSTOM
      case R.id.toggle_navigation =>
        bar.setNavigationMode(if (bar.getNavigationMode == ActionBar.NAVIGATION_MODE_STANDARD) ActionBar.NAVIGATION_MODE_TABS else ActionBar.NAVIGATION_MODE_STANDARD)
        return
      case R.id.cycle_custom_gravity =>
        val lp: ActionBar.LayoutParams = mCustomView.getLayoutParams.asInstanceOf[ActionBar.LayoutParams]
        var newGravity: Int = 0
        (lp.gravity & Gravity.HORIZONTAL_GRAVITY_MASK) match {
          case Gravity.LEFT => newGravity = Gravity.CENTER_HORIZONTAL
          case Gravity.CENTER_HORIZONTAL => newGravity = Gravity.RIGHT
          case Gravity.RIGHT => newGravity = Gravity.LEFT
        }
        lp.gravity = lp.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | newGravity
        bar.setCustomView(mCustomView, lp)
        return
    }
    val change = bar.getDisplayOptions ^ flags
    bar.setDisplayOptions(change, flags)
  }

  def onTabSelected(tab: ActionBar.Tab, ft: FragmentTransaction) {
  }

  def onTabUnselected(tab: ActionBar.Tab, ft: FragmentTransaction) {
  }

  def onTabReselected(tab: ActionBar.Tab, ft: FragmentTransaction) {
  }

  private var mCustomView: View = null
}