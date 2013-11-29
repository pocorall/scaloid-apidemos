/*
 * Copyright (C) 2009 The Android Open Source Project
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
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

class Animation3 extends Activity with AdapterView.OnItemSelectedListener {
  private final val INTERPOLATORS: Array[String] = Array("Accelerate", "Decelerate", "Accelerate/Decelerate", "Anticipate", "Overshoot", "Anticipate/Overshoot", "Bounce")

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.animation_3)
    val s: Spinner = findViewById(R.id.spinner).asInstanceOf[Spinner]
    val adapter: ArrayAdapter[String] = new ArrayAdapter[String](this, android.R.layout.simple_spinner_item, INTERPOLATORS)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    s.setAdapter(adapter)
    s.setOnItemSelectedListener(this)
  }

  def onItemSelected(parent: AdapterView[_], v: View, position: Int, id: Long) {
    val target: View = findViewById(R.id.target)
    val targetParent: View = target.getParent.asInstanceOf[View]
    val a: Animation = new TranslateAnimation(0.0f, targetParent.getWidth - target.getWidth - targetParent.getPaddingLeft - targetParent.getPaddingRight, 0.0f, 0.0f)
    a.setDuration(1000)
    a.setStartOffset(300)
    a.setRepeatMode(Animation.RESTART)
    a.setRepeatCount(Animation.INFINITE)
    position match {
      case 0 =>
        a.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.anim.accelerate_interpolator))
      case 1 =>
        a.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.anim.decelerate_interpolator))
      case 2 =>
        a.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.anim.accelerate_decelerate_interpolator))
      case 3 =>
        a.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.anim.anticipate_interpolator))
      case 4 =>
        a.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.anim.overshoot_interpolator))
      case 5 =>
        a.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.anim.anticipate_overshoot_interpolator))
      case 6 =>
        a.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.anim.bounce_interpolator))
    }
    target.startAnimation(a)
  }

  def onNothingSelected(parent: AdapterView[_]) {
  }
}