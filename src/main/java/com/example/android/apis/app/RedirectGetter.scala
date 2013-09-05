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
import android.content.SharedPreferences
import android.os.Bundle
import android.view.{Gravity, View}
import android.widget.Button
import android.widget.TextView
import org.scaloid.common._
/**
 * Sub-activity that is executed by the redirection example when input is needed
 * from the user.
 */
class RedirectGetter extends SActivity {
  onCreate {
    contentView = new SVerticalLayout {
      STextView(R.string.redirect_getter).padding(0,0,0,4 dip).<<(MATCH_PARENT, WRAP_CONTENT).Weight(0).>>.setTextAppearance(context, android.R.attr.textAppearanceMedium)
      mText = SEditText().padding(0,0,0,4 dip).<<(MATCH_PARENT, WRAP_CONTENT).Weight(0).>>
      mText.requestFocus()
      SButton(R.string.apply, {
        if (((getSharedPreferences("RedirectData", 0)).edit.putString("text", mText.text.toString)).commit) {
          setResult(RESULT_OK)
        }
        finish
      }).<<.wrap
    }.gravity(Gravity.CENTER_HORIZONTAL).padding(4 dip)
    loadPrefs
  }

  private final def loadPrefs {
    mTextPref = (getSharedPreferences("RedirectData", 0)).getString("text", null)
    if (mTextPref != null) {
      mText.setText(mTextPref)
    }
    else {
      mText.setText("")
    }
  }
  val RESULT_OK = -1
  var mTextPref: String = null
  var mText: SEditText = null
}