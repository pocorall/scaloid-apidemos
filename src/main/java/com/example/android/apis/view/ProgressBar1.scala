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
import android.widget.Button
import android.widget.ProgressBar
import android.os.Bundle
import android.view.View
import android.view.Window

/**
 * Demonstrates how to use progress bars as widgets and in the title bar.  The progress bar
 * in the title will be shown until the progress is complete, at which point it fades away.
 */
class ProgressBar1 extends Activity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    requestWindowFeature(Window.FEATURE_PROGRESS)
    setContentView(R.layout.progressbar_1)
    setProgressBarVisibility(true)
    val progressHorizontal: ProgressBar = findViewById(R.id.progress_horizontal).asInstanceOf[ProgressBar]
    setProgress(progressHorizontal.getProgress * 100)
    setSecondaryProgress(progressHorizontal.getSecondaryProgress * 100)
    var button: Button = findViewById(R.id.increase).asInstanceOf[Button]
    button.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        progressHorizontal.incrementProgressBy(1)
        setProgress(100 * progressHorizontal.getProgress)
      }
    })
    button = findViewById(R.id.decrease).asInstanceOf[Button]
    button.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        progressHorizontal.incrementProgressBy(-1)
        setProgress(100 * progressHorizontal.getProgress)
      }
    })
    button = findViewById(R.id.increase_secondary).asInstanceOf[Button]
    button.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        progressHorizontal.incrementSecondaryProgressBy(1)
        setSecondaryProgress(100 * progressHorizontal.getSecondaryProgress)
      }
    })
    button = findViewById(R.id.decrease_secondary).asInstanceOf[Button]
    button.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        progressHorizontal.incrementSecondaryProgressBy(-1)
        setSecondaryProgress(100 * progressHorizontal.getSecondaryProgress)
      }
    })
  }
}