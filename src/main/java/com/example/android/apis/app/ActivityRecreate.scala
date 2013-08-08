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
import android.os.Bundle
import android.widget.Button
import org.scaloid.common._
import android.view.Gravity

class ActivityRecreate extends SActivity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState != null) {
      mCurTheme = savedInstanceState.getInt("theme")
      // Switch to a new theme different from last theme.
      mCurTheme match {
        case android.R.style.Theme_Holo_Light =>
          mCurTheme = android.R.style.Theme_Holo_Dialog
        case android.R.style.Theme_Holo_Dialog =>
          mCurTheme = android.R.style.Theme_Holo
        case _ =>
          mCurTheme = android.R.style.Theme_Holo_Light
      }
      setTheme(mCurTheme)
    }
    contentView = new SVerticalLayout {
      STextView(R.string.activity_recreate_msg).<<(MATCH_PARENT, WRAP_CONTENT).marginBottom(4 dip).Weight(0.0f)
      SButton(R.string.recreate, recreate).<<.wrap.>>.requestFocus()
    }.gravity(Gravity.CENTER_HORIZONTAL).padding(4 dip)
  }
  protected override def onSaveInstanceState(savedInstanceState: Bundle) {
    super.onSaveInstanceState(savedInstanceState)
    savedInstanceState.putInt("theme", mCurTheme)
  }
  private[app] var mCurTheme: Int = 0
}