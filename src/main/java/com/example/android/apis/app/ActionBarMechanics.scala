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

import android.view.Menu
import android.view.MenuItem
import android.view.Window
import org.scaloid.common._

/**
 * This demonstrates the basics of the Action Bar and how it interoperates with the
 * standard options menu. This demo is for informative purposes only; see ActionBarUsage for
 * an example of using the Action Bar in a more idiomatic manner.
 */
class ActionBarMechanics extends SActivity {
 onCreate(getWindow.requestFeature(Window.FEATURE_ACTION_BAR))
  override def onCreateOptionsMenu(menu: Menu) = {
    menu.add("Normal item")
    val actionItem = menu.add("Action Button")
    actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    actionItem.setIcon(android.R.drawable.ic_menu_share)
    true
  }
  override def onOptionsItemSelected(item: MenuItem) = {
    toast("Selected Item: " + item.getTitle)
    true
  }
}