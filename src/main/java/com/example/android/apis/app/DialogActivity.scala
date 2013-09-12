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
package com.example.android.apis.app

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

import com.example.android.apis.R
import android.os.Bundle
import android.view.{Gravity, Window}
import android.widget.Button
import android.widget.LinearLayout
import org.scaloid.common._

/**
 * <h3>Dialog Activity</h3>
 *
 * <p>This demonstrates the how to write an activity that looks like
 * a pop-up dialog.</p>
 */
class DialogActivity extends SActivity {
  /**
   * Initialization of the Activity after it is first created.  Must at least
   * call {@link android.app.Activity#setContentView setContentView()} to
   * describe what is to be displayed in the screen.
   */
  onCreate {
    requestWindowFeature(Window.FEATURE_LEFT_ICON)
    var innerLayout: SLinearLayout = null
    contentView = new SVerticalLayout {
      STextView(R.string.dialog_activity_text).<<.wrap.>>.gravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL).setTextAppearance(context, android.R.attr.textAppearanceMedium)
      innerLayout = new SLinearLayout().padding(4 dip).<<.wrap.>>
      this += innerLayout
      this += new SLinearLayout {
        SButton(R.string.dialog_activity_add, {
          innerLayout.addView(new SImageView().imageDrawable(R.drawable.icon48x48_1).padding(4))
        }).<<.wrap.Weight(1.0f).>> //.setButtonStyle(android.R.attr.buttonBarButtonStyle)
        SButton(R.string.dialog_activity_remove, {
          val num = innerLayout.getChildCount
          if (num > 0) innerLayout.removeViewAt(num - 1)
        }).<<.wrap.Weight(1.0f).>> //.setButtonStyle(android.R.attr.buttonBarButtonStyle)
      }
    }.padding(4 dip).gravity(Gravity.CENTER_HORIZONTAL)
    getWindow.setTitle("This is just a test")
    getWindow.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, android.R.drawable.ic_dialog_alert)
  }
}