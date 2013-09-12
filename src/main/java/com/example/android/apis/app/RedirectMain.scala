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
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.scaloid.common._
import android.view.Gravity

/**
 * Entry into our redirection example, describing what will happen.
 */
 object RedirectMain {
  val INIT_TEXT_REQUEST = 0
  val NEW_TEXT_REQUEST = 1
}
class RedirectMain extends SActivity {
  import RedirectMain._
  onCreate {
    contentView = new SVerticalLayout {
      STextView(R.string.redirect_main).padding(0,0,0,4 dip).<<(MATCH_PARENT, WRAP_CONTENT).Weight(0).>>.setTextAppearance(context, android.R.attr.textAppearanceMedium)
      text = STextView().padding(0,0,0,4 dip).<<(MATCH_PARENT, WRAP_CONTENT).Weight(0).>> //.setTextAppearance(context, android.R.attr.textAppearanceMedium)
      SButton(R.string.clear_text, {
        // Erase the preferences and exit!
        (getSharedPreferences("RedirectData", 0)).edit.remove("text").commit
        finish
      }).<<.wrap
      SButton(R.string.new_text, {
        // Retrieve new text preferences.
        startActivityForResult(SIntent[RedirectGetter], NEW_TEXT_REQUEST)
      }).<<.wrap
    }.padding(4 dip).gravity(Gravity.CENTER_HORIZONTAL)
    // Retrieve the current text preference.  If there is no text
    // preference set, we need to get it from the user by invoking the
    // activity that retrieves it.  To do this cleanly, we will
    // temporarily hide our own activity so it is not displayed until the
    // result is returned.
    if (!loadPrefs)
      startActivityForResult(SIntent[RedirectGetter], INIT_TEXT_REQUEST)
  }

  protected override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    if (requestCode == INIT_TEXT_REQUEST) {
      // If the request was cancelled, then we are cancelled as well.
      if (resultCode == RESULT_CANCELED)
        finish
        // Otherwise, there now should be text...  reload the prefs,
        // and show our UI.  (Optionally we could verify that the text
        // is now set and exit if it isn't.)
      else
        loadPrefs
    }
    else if (requestCode == NEW_TEXT_REQUEST) {
      // In this case we are just changing the text, so if it was
      // cancelled then we can leave things as-is.
      if (resultCode != RESULT_CANCELED)
        loadPrefs
    }
  }
  private final def loadPrefs = {
    // Retrieve the current redirect values.
    // NOTE: because this preference is shared between multiple
    // activities, you must be careful about when you read or write
    // it in order to keep from stepping on yourself.
    mTextPref = (getSharedPreferences("RedirectData", 0)).getString("text", null)
    if (mTextPref != null) {
      text.setText(mTextPref)
      true
    }
    false
  }
  val RESULT_CANCELED = 0
  var mTextPref: String = null
  var text: STextView = null
}