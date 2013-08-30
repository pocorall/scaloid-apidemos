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
import android.view.View
import android.widget.Button
import android.widget.TextView
import org.scaloid.common._
/**
 * Sub-activity that is executed by the redirection example when input is needed
 * from the user.
 */
class RedirectGetter extends SActivity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.redirect_getter)
    val applyButton = find[Button](R.id.apply)
    applyButton.onClick {
      val preferences = getSharedPreferences("RedirectData", 0)
      val editor: SharedPreferences.Editor = preferences.edit
      editor.putString("text", mText.getText.toString)
      if (editor.commit) {
        setResult(RESULT_OK)
      }
      finish
    }
    mText = find[TextView](R.id.text)
    loadPrefs
  }

  private final def loadPrefs {
    val preferences = getSharedPreferences("RedirectData", 0)
    mTextPref = preferences.getString("text", null)
    if (mTextPref != null) {
      mText.setText(mTextPref)
    }
    else {
      mText.setText("")
    }
  }
  val RESULT_OK = -1;
  private var mTextPref: String = null
  private var mText: TextView = null
}