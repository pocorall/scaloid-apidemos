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
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.ViewFlipper

class Animation2 extends Activity with AdapterView.OnItemSelectedListener {

  private var mStrings: Array[String] = Array("Push up", "Push left", "Cross fade", "Hyperspace")
  private var mFlipper: ViewFlipper = null

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.animation_2)
    mFlipper = (this.findViewById(R.id.flipper).asInstanceOf[ViewFlipper])
    mFlipper.startFlipping
    val s: Spinner = findViewById(R.id.spinner).asInstanceOf[Spinner]
    val adapter: ArrayAdapter[String] = new ArrayAdapter[String](this, android.R.layout.simple_spinner_item, mStrings)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    s.setAdapter(adapter)
    s.setOnItemSelectedListener(this)
  }

  def onItemSelected(parent: AdapterView[_], v: View, position: Int, id: Long) {
    position match {
      case 0 =>
        mFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_in))
        mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_out))
      case 1 =>
        mFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in))
        mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out))
      case 2 =>
        mFlipper.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in))
        mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out))
      case _ =>
        mFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.hyperspace_in))
        mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.hyperspace_out))
    }
  }

  def onNothingSelected(parent: AdapterView[_]) {
  }
}