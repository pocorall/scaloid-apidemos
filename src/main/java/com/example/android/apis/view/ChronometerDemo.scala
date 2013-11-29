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

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

import com.example.android.apis.R
import android.app.Activity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.Chronometer

class ChronometerDemo extends Activity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.chronometer)
    var button: Button = null
    mChronometer = findViewById(R.id.chronometer).asInstanceOf[Chronometer]
    button = findViewById(R.id.start).asInstanceOf[Button]
    button.setOnClickListener(mStartListener)
    button = findViewById(R.id.stop).asInstanceOf[Button]
    button.setOnClickListener(mStopListener)
    button = findViewById(R.id.reset).asInstanceOf[Button]
    button.setOnClickListener(mResetListener)
    button = findViewById(R.id.set_format).asInstanceOf[Button]
    button.setOnClickListener(mSetFormatListener)
    button = findViewById(R.id.clear_format).asInstanceOf[Button]
    button.setOnClickListener(mClearFormatListener)
  }

  private[view] var mChronometer: Chronometer = null
  private[view] var mStartListener: View.OnClickListener = new View.OnClickListener {
    def onClick(v: View) {
      mChronometer.start
    }
  }
  private[view] var mStopListener: View.OnClickListener = new View.OnClickListener {
    def onClick(v: View) {
      mChronometer.stop
    }
  }
  private[view] var mResetListener: View.OnClickListener = new View.OnClickListener {
    def onClick(v: View) {
      mChronometer.setBase(SystemClock.elapsedRealtime)
    }
  }
  private[view] var mSetFormatListener: View.OnClickListener = new View.OnClickListener {
    def onClick(v: View) {
      mChronometer.setFormat("Formatted time (%s)")
    }
  }
  private[view] var mClearFormatListener: View.OnClickListener = new View.OnClickListener {
    def onClick(v: View) {
      mChronometer.setFormat(null)
    }
  }
}