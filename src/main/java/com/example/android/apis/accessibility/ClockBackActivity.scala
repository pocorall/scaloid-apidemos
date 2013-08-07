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
package com.example.android.apis.accessibility

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import com.example.android.apis.R
import org.scaloid.common._
import android.widget.ImageView
import android.view.Gravity

class ClockBackActivity extends SActivity {
  /**
   * {@inheritDoc}
   */
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    contentView = new SVerticalLayout {
      STextView(R.string.accessibility_service_instructions).<<.wrap
      SImageButton().backgroundResource(R.drawable.ic_launcher_settings).onClick(startActivity(sSettingsIntent)).scaleType(ImageView.ScaleType.FIT_CENTER).adjustViewBounds(true).<<(32 dip, 32 dip).marginTop(50 dip).Gravity(Gravity.CENTER)
    }
  }
  val sSettingsIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
}