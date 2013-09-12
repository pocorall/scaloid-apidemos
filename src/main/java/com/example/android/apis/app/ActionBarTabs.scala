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
import android.app.Fragment
import android.app.FragmentTransaction
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.scaloid.common._
/**
 * This demonstrates the use of action bar tabs and how they interact
 * with other action bar features.
 */
class ActionBarTabs extends SActivity {
  var frameLayoutId:Int = 0
  onCreate{
    contentView = new SVerticalLayout {
      lazy val fLayout =  new SFrameLayout().<<(MATCH_PARENT, 0 dip).Weight(1).>>
      frameLayoutId = fLayout.uniqueId
      this += fLayout
      this += new SVerticalLayout {
        SButton(R.string.btn_add_tab, {
          val tabCount = getActionBar.getTabCount
          val text = "Tab " + tabCount
          getActionBar.addTab(getActionBar.newTab.setText(text).setTabListener(new TabListener(new TabContentFragment(text))))
        }).<<.wrap
        SButton(R.string.btn_remove_tab, {
          getActionBar.removeTabAt(getActionBar.getTabCount - 1)
        }).<<.wrap
        SButton(R.string.btn_toggle_tabs,  {
          if (getActionBar.getNavigationMode == ActionBar.NAVIGATION_MODE_TABS) {
            getActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD)
            getActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE, ActionBar.DISPLAY_SHOW_TITLE)
          }
          else {
            getActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS)
            getActionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE)
          }
        }).<<.wrap
        SButton(R.string.btn_remove_all_tabs,  getActionBar.removeAllTabs()).<<.wrap
      }.<<(MATCH_PARENT, 0 dip).Weight(1f).>>
    }
  }

  /**
   * A TabListener receives event callbacks from the action bar as tabs
   * are deselected, selected, and reselected. A FragmentTransaction
   * is provided to each of these callbacks; if any operations are added
   * to it, it will be committed at the end of the full tab switch operation.
   * This lets tab switches be atomic without the app needing to track
   * the interactions between different tabs.
   *
   * NOTE: This is a very simple implementation that does not retain
   * fragment state of the non-visible tabs across activity instances.
   * Look at the FragmentTabs example for how to do a more complete
   * implementation.
   */
  private class TabListener extends ActionBar.TabListener {
    def this(fragment: TabContentFragment) {
      this()
      mFragment = fragment
    }
    def onTabSelected(tab: ActionBar.Tab, ft: FragmentTransaction) {
      ft.add(frameLayoutId, mFragment, mFragment.getText)
    }
    def onTabUnselected(tab: ActionBar.Tab, ft: FragmentTransaction) {
      ft.remove(mFragment)
    }
    def onTabReselected(tab: ActionBar.Tab, ft: FragmentTransaction) {
      toast("Reselected!")
    }
    private var mFragment: TabContentFragment = null
  }
  private class TabContentFragment(mText: String) extends Fragment {
    def getText = mText
    override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle) = {
      val fragView = inflater.inflate(R.layout.action_bar_tab_content, container, false)
      (fragView.find[TextView](R.id.text)).setText(mText)
      fragView
    }
  }
}