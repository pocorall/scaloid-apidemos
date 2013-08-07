/*
 * Copyright (C) 2012 The Android Open Source Project
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

import android.content.Intent
import android.os.Bundle
import org.scaloid.common._
import android.view.Gravity

class FinishAffinity extends SActivity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    var textViewSeq: STextView = null
    contentView = new SVerticalLayout {
      STextView("No matter how deep you go, Activity.finishAffinity() will get you back.").<<(MATCH_PARENT, WRAP_CONTENT).marginBottom(4 dip)
      textViewSeq = STextView().<<(MATCH_PARENT, WRAP_CONTENT).marginBottom(4 dip).>>
      SButton("Nest some more", {
        val intent = new Intent(FinishAffinity.this, classOf[FinishAffinity])
        intent.putExtra("nesting", mNesting + 1)
        startActivity(intent)
      }).<<.wrap.>>.requestFocus()
      SButton("FINISH!", finishAffinity()).<<.wrap.>>.requestFocus()
    }.gravity(Gravity.CENTER_HORIZONTAL).padding(4 dip)

    mNesting = getIntent.getIntExtra("nesting", 1)
    textViewSeq.setText("Current nesting: " + mNesting)
  }
  var mNesting: Int = 0
}