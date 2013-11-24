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
package com.example.android.apis.view

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import com.example.android.apis.R

/**
 * This demonstrates the use of the PopupMenu class. Clicking the button will inflate and
 * show a popup menu from an XML resource.
 */
class PopupMenu1 extends Activity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.popup_menu_1)
  }

  def onPopupButtonClick(button: View) {
    val popup: PopupMenu = new PopupMenu(this, button)
    popup.getMenuInflater.inflate(R.menu.popup, popup.getMenu)
    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener {
      def onMenuItemClick(item: MenuItem): Boolean = {
        Toast.makeText(PopupMenu1.this, "Clicked popup menu item " + item.getTitle, Toast.LENGTH_SHORT).show
        return true
      }
    })
    popup.show
  }
}