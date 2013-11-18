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
package com.example.android.apis.preference

import com.example.android.apis.R
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceActivity
import android.preference.CheckBoxPreference
import android.widget.Toast

/**
 * Example that shows finding a preference from the hierarchy and a custom preference type.
 */
object AdvancedPreferences {
  val KEY_MY_PREFERENCE: String = "my_preference"
  val KEY_ADVANCED_CHECKBOX_PREFERENCE: String = "advanced_checkbox_preference"
}

class AdvancedPreferences extends PreferenceActivity with OnSharedPreferenceChangeListener {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    addPreferencesFromResource(R.xml.advanced_preferences)
    mCheckBoxPreference = getPreferenceScreen.findPreference(AdvancedPreferences.KEY_ADVANCED_CHECKBOX_PREFERENCE).asInstanceOf[CheckBoxPreference]
  }

  protected override def onResume {
    super.onResume
    mForceCheckBoxRunnable.run
    getPreferenceScreen.getSharedPreferences.registerOnSharedPreferenceChangeListener(this)
  }

  protected override def onPause {
    super.onPause
    getPreferenceScreen.getSharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    mHandler.removeCallbacks(mForceCheckBoxRunnable)
  }

  def onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
    if (key == AdvancedPreferences.KEY_MY_PREFERENCE) {
      Toast.makeText(this, "Thanks! You increased my count to " + sharedPreferences.getInt(key, 0), Toast.LENGTH_SHORT).show
    }
  }

  private var mCheckBoxPreference: CheckBoxPreference = null
  private var mHandler: Handler = new Handler
  /**
   * This is a simple example of controlling a preference from code.
   */
  private var mForceCheckBoxRunnable: Runnable = new Runnable {
    def run {
      if (mCheckBoxPreference != null) {
        mCheckBoxPreference.setChecked(!mCheckBoxPreference.isChecked)
      }
      mHandler.postDelayed(this, 1000)
    }
  }
}