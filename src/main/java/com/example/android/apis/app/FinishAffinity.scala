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

import com.example.android.apis.R
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import org.scaloid.common._

class FinishAffinity extends SActivity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_finish_affinity)
    mNesting = getIntent.getIntExtra("nesting", 1)
    (findViewById(R.id.seq).asInstanceOf[TextView]).setText("Current nesting: " + mNesting)
    var button: Button = findViewById(R.id.nest).asInstanceOf[Button]
    button.setOnClickListener(mNestListener)
    button = findViewById(R.id.finish).asInstanceOf[Button]
    button.setOnClickListener(mFinishListener)
  }

  var mNesting: Int = 0
  private var mNestListener: View.OnClickListener = new View.OnClickListener {
    def onClick(v: View) {
      val intent: Intent = new Intent(FinishAffinity.this, classOf[FinishAffinity])
      intent.putExtra("nesting", mNesting + 1)
      startActivity(intent)
    }
  }
  private var mFinishListener: View.OnClickListener = new View.OnClickListener {
    def onClick(v: View) {
      finishAffinity
    }
  }
}