/*
 * Copyright (C) 2007 The Android Open Source Project
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
import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

/**
 * Demonstrates inflating menus from XML. There are different menu XML resources
 * that the user can choose to inflate. First, select an example resource from
 * the spinner, and then hit the menu button. To choose another, back out of the
 * activity and start over.
 */
class MenuInflateFromXml extends Activity {
  // Scaloid >>
  val LinearLayout_LayoutParams_MATCH_PARENT = android.view.ViewGroup.LayoutParams.MATCH_PARENT
  val LinearLayout_LayoutParams_WRAP_CONTENT = android.view.ViewGroup.LayoutParams.WRAP_CONTENT
  // Scaloid <<

  /**
   * Different example menu resources.
   */
  private final val sMenuExampleResources: Array[Int] = Array(R.menu.title_only, R.menu.title_icon, R.menu.submenu, R.menu.groups, R.menu.checkable, R.menu.shortcuts, R.menu.order, R.menu.category_order, R.menu.visible, R.menu.disabled)
  /**
   * Names corresponding to the different example menu resources.
   */
  private final val sMenuExampleNames: Array[String] = Array("Title only", "Title and Icon", "Submenu", "Groups", "Checkable", "Shortcuts", "Order", "Category and Order", "Visible", "Disabled")

  /**
   * Lets the user choose a menu resource.
   */
  private var mSpinner: Spinner = null
  /**
   * Shown as instructions.
   */
  private var mInstructionsText: TextView = null
  /**
   * Safe to hold on to this.
   */
  private var mMenu: Menu = null

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val layout: LinearLayout = new LinearLayout(this)
    layout.setOrientation(LinearLayout.VERTICAL)
    val adapter: ArrayAdapter[String] = new ArrayAdapter[String](this, android.R.layout.simple_spinner_item, sMenuExampleNames)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    mSpinner = new Spinner(this)
    mSpinner.setId(R.id.spinner)
    mSpinner.setAdapter(adapter)
    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener {
      def onItemSelected(parent: AdapterView[_], view: View, position: Int, id: Long) {
        invalidateOptionsMenu
      }

      def onNothingSelected(parent: AdapterView[_]) {
      }
    })
    layout.addView(mSpinner, new LinearLayout.LayoutParams(LinearLayout_LayoutParams_MATCH_PARENT, LinearLayout_LayoutParams_WRAP_CONTENT))
    mInstructionsText = new TextView(this)
    mInstructionsText.setText(getResources.getString(R.string.menu_from_xml_instructions_press_menu))
    val lp: LinearLayout.LayoutParams = new LinearLayout.LayoutParams(LinearLayout_LayoutParams_MATCH_PARENT, LinearLayout_LayoutParams_WRAP_CONTENT)
    lp.setMargins(10, 10, 10, 10)
    layout.addView(mInstructionsText, lp)
    setContentView(layout)
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    mMenu = menu
    val inflater: MenuInflater = getMenuInflater
    inflater.inflate(sMenuExampleResources(mSpinner.getSelectedItemPosition), menu)
    mInstructionsText.setText(getResources.getString(R.string.menu_from_xml_instructions_go_back))
    return true
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case R.id.jump =>
        Toast.makeText(this, "Jump up in the air!", Toast.LENGTH_SHORT).show
        invalidateOptionsMenu
        return true
      case R.id.dive =>
        Toast.makeText(this, "Dive into the water!", Toast.LENGTH_SHORT).show
        return true
      case R.id.browser_visibility =>
        val shouldShowBrowser: Boolean = !mMenu.findItem(R.id.refresh).isVisible
        mMenu.setGroupVisible(R.id.browser, shouldShowBrowser)
      case R.id.email_visibility =>
        val shouldShowEmail: Boolean = !mMenu.findItem(R.id.reply).isVisible
        mMenu.setGroupVisible(R.id.email, shouldShowEmail)
      case _ =>
        if (!item.hasSubMenu) {
          Toast.makeText(this, item.getTitle, Toast.LENGTH_SHORT).show
          return true
        }
    }
    return false
  }


}