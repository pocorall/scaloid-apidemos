/*
 * Copyright (C) 2008 The Android Open Source Project
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
package com.example.android.apis.view

import com.example.android.apis.R
import android.app.TabActivity
import android.os.Bundle
import android.view.View
import android.widget.TabHost
import android.widget.TextView

/**
 * Demonstrates the Tab scrolling when too many tabs are displayed to fit in the screen.
 */
class Tabs5 extends TabActivity with TabHost.TabContentFactory {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.tabs_scroll)
    val tabHost: TabHost = getTabHost
    var i: Int = 1
    while (i <= 30) {
      {
        val name: String = "Tab " + i
        tabHost.addTab(tabHost.newTabSpec(name).setIndicator(name).setContent(this))
      }
      ({
        i += 1; i - 1
      })
    }
  }

  /** {@inheritDoc} */
  def createTabContent(tag: String): View = {
    val tv: TextView = new TextView(this)
    tv.setText("Content for tab with tag " + tag)
    return tv
  }
}