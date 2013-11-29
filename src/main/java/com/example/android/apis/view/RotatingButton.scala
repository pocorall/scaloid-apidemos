/*
 * Copyright (C) 2010 The Android Open Source Project
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
import android.widget.Button
import android.widget.SeekBar

/**
 * This application demonstrates the ability to transform views in 2D and 3D, scaling them,
 * translating them, and rotating them (in 2D and 3D). Use the seek bars to set the various
 * transform properties of the button.
 */
class RotatingButton extends Activity {
  /** Called when the activity is first created. */
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.rotating_view)
    val rotatingButton: Button = findViewById(R.id.rotatingButton).asInstanceOf[Button]
    var seekBar: SeekBar = null
    seekBar = findViewById(R.id.translationX).asInstanceOf[SeekBar]
    seekBar.setMax(400)
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener {
      def onStopTrackingTouch(seekBar: SeekBar) {
      }

      def onStartTrackingTouch(seekBar: SeekBar) {
      }

      def onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        rotatingButton.setTranslationX(progress.asInstanceOf[Float])
      }
    })
    seekBar = findViewById(R.id.translationY).asInstanceOf[SeekBar]
    seekBar.setMax(800)
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener {
      def onStopTrackingTouch(seekBar: SeekBar) {
      }

      def onStartTrackingTouch(seekBar: SeekBar) {
      }

      def onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        rotatingButton.setTranslationY(progress.asInstanceOf[Float])
      }
    })
    seekBar = findViewById(R.id.scaleX).asInstanceOf[SeekBar]
    seekBar.setMax(50)
    seekBar.setProgress(10)
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener {
      def onStopTrackingTouch(seekBar: SeekBar) {
      }

      def onStartTrackingTouch(seekBar: SeekBar) {
      }

      def onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        rotatingButton.setScaleX(progress.asInstanceOf[Float] / 10f)
      }
    })
    seekBar = findViewById(R.id.scaleY).asInstanceOf[SeekBar]
    seekBar.setMax(50)
    seekBar.setProgress(10)
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener {
      def onStopTrackingTouch(seekBar: SeekBar) {
      }

      def onStartTrackingTouch(seekBar: SeekBar) {
      }

      def onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        rotatingButton.setScaleY(progress.asInstanceOf[Float] / 10f)
      }
    })
    seekBar = findViewById(R.id.rotationX).asInstanceOf[SeekBar]
    seekBar.setMax(360)
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener {
      def onStopTrackingTouch(seekBar: SeekBar) {
      }

      def onStartTrackingTouch(seekBar: SeekBar) {
      }

      def onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        rotatingButton.setRotationX(progress.asInstanceOf[Float])
      }
    })
    seekBar = findViewById(R.id.rotationY).asInstanceOf[SeekBar]
    seekBar.setMax(360)
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener {
      def onStopTrackingTouch(seekBar: SeekBar) {
      }

      def onStartTrackingTouch(seekBar: SeekBar) {
      }

      def onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        rotatingButton.setRotationY(progress.asInstanceOf[Float])
      }
    })
    seekBar = findViewById(R.id.rotationZ).asInstanceOf[SeekBar]
    seekBar.setMax(360)
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener {
      def onStopTrackingTouch(seekBar: SeekBar) {
      }

      def onStartTrackingTouch(seekBar: SeekBar) {
      }

      def onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        rotatingButton.setRotation(progress.asInstanceOf[Float])
      }
    })
  }
}