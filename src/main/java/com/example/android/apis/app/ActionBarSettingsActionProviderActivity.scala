/*
 * Copyright (C) 2011 The Android Open Source Project
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

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.ActionProvider
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import com.example.android.apis.R
import org.scaloid.common._

/**
 * This activity demonstrates how to implement an {@link android.view.ActionProvider}
 * for adding functionality to the Action Bar. In particular this demo creates an
 * ActionProvider for launching the system settings and adds a menu item with that
 * provider.
 */
object ActionBarSettingsActionProviderActivity {
  class SettingsActionProvider(mContext: Context) extends ActionProvider(mContext) {
    /** An intent for launching the system settings. */
    private val sSettingsIntent = new Intent(Settings.ACTION_SETTINGS)
    def onCreateActionView = {
      val layoutInflater = LayoutInflater.from(mContext)
      val view = layoutInflater.inflate(R.layout.action_bar_settings_action_provider, null)
      view.find[ImageButton](R.id.button).onClick(mContext.startActivity(sSettingsIntent))
      view
    }
    override def onPerformDefaultAction = {
      mContext.startActivity(sSettingsIntent)
      true
    }
  }
}
class ActionBarSettingsActionProviderActivity extends SActivity {
  override def onCreateOptionsMenu(menu: Menu) = {
    super.onCreateOptionsMenu(menu)
    getMenuInflater.inflate(R.menu.action_bar_settings_action_provider, menu)
    true
  }
  override def onOptionsItemSelected(item: MenuItem) = {
    toast(R.string.action_bar_settings_action_provider_no_handling)
    false
  }
}