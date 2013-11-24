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
package com.example.android.apis.view

import com.example.android.apis.R
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.RadioGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.LinearLayout

class RadioGroup1 extends Activity with RadioGroup.OnCheckedChangeListener with View.OnClickListener {
  // Scaloid >>
  val RadioGroup_LayoutParams_WRAP_CONTENT = android.view.ViewGroup.LayoutParams.WRAP_CONTENT
  // Scaloid <<
  private var mChoice: TextView = null
  private var mRadioGroup: RadioGroup = null

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.radio_group_1)
    mRadioGroup = findViewById(R.id.menu).asInstanceOf[RadioGroup]
    val newRadioButton: RadioButton = new RadioButton(this)
    newRadioButton.setText(R.string.radio_group_snack)
    newRadioButton.setId(R.id.snack)
    val layoutParams: LinearLayout.LayoutParams = new RadioGroup.LayoutParams(RadioGroup_LayoutParams_WRAP_CONTENT, RadioGroup_LayoutParams_WRAP_CONTENT)
    mRadioGroup.addView(newRadioButton, 0, layoutParams)
    val selection: String = getString(R.string.radio_group_selection)
    mRadioGroup.setOnCheckedChangeListener(this)
    mChoice = findViewById(R.id.choice).asInstanceOf[TextView]
    mChoice.setText(selection + mRadioGroup.getCheckedRadioButtonId)
    val clearButton: Button = findViewById(R.id.clear).asInstanceOf[Button]
    clearButton.setOnClickListener(this)
  }

  def onCheckedChanged(group: RadioGroup, checkedId: Int) {
    val selection: String = getString(R.string.radio_group_selection)
    val none: String = getString(R.string.radio_group_none)
    mChoice.setText(selection + (if (checkedId == View.NO_ID) none else checkedId))
  }

  def onClick(v: View) {
    mRadioGroup.clearCheck
  }


}