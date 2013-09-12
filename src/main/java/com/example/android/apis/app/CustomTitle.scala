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

import android.view.{Gravity, Window}
import android.widget.TextView
import com.example.android.apis.R
import org.scaloid.common._
/**
 * Example of how to use a custom title {@link android.view.Window#FEATURE_CUSTOM_TITLE}.
 * <h3>CustomTitle</h3>

<p>This demonstrates how a custom title can be used.</p>

<h4>Demo</h4>
App/Title/Custom Title

<h4>Source files</h4>
 * <table class="LinkTable">
 * <tr>
 * <td >src/com.example.android.apis/app/CustomTitle.java</td>
 * <td >The Custom Title implementation</td>
 * </tr>
 * <tr>
 * <td >/res/any/layout/custom_title.xml</td>
 * <td >Defines contents of the screen</td>
 * </tr>
 * </table>
 */
class CustomTitle extends SActivity {
  /**
   * Initialization of the Activity after it is first created.  Must at least
   * call {@link android.app.Activity#setContentView(int)} to
   * describe what is to be displayed in the screen.
   */
  onCreate {
    requestWindowFeature(Window.FEATURE_CUSTOM_TITLE)
    var leftTextEdit:SEditText = null
    var leftTextButton:SButton = null
    var rightTextEdit:SEditText = null
    var rightTextButton:SButton = null
    contentView = new SVerticalLayout {
      this += new SLinearLayout{
        leftTextEdit = SEditText(R.string.custom_title_left).ems(10).Gravity(Gravity.CENTER_VERTICAL).wrap.>>
        leftTextButton = SButton(R.string.custom_title_left_button).Gravity(Gravity.CENTER_VERTICAL).wrap.>>
      }
      this += new SLinearLayout{
        rightTextEdit = SEditText(R.string.custom_title_right).ems(10).Gravity(Gravity.CENTER_VERTICAL).wrap.>>
        rightTextButton = SButton(R.string.custom_title_right_button).Gravity(Gravity.CENTER_VERTICAL).wrap.>>
      }
    }
    getWindow.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_1)
    val leftText = find[TextView](R.id.left_text)
    val rightText = find[TextView](R.id.right_text)
    leftTextButton.onClick(leftText.setText(leftTextEdit.text))
    rightTextButton.onClick(rightText.setText(rightTextEdit.text))
}
}