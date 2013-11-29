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
package com.example.android.apis.animation

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

import android.animation.Animator
import android.animation.ObjectAnimator
import com.example.android.apis.R
import android.animation.AnimatorListenerAdapter
import android.animation.Keyframe
import android.animation.LayoutTransition
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.app.Activity
import android.os.Bundle
import android.widget.Button

/**
 * This application demonstrates how to use LayoutTransition to automate transition animations
 * as items are removed from or added to a container.
 */
class LayoutAnimations extends Activity {
  private var numButtons: Int = 1
  private[animation] var container: ViewGroup = null
  private[animation] var defaultAppearingAnim: Animator = null
  private[animation] var defaultDisappearingAnim: Animator = null
  private[animation] var defaultChangingAppearingAnim: Animator = null
  private[animation] var defaultChangingDisappearingAnim: Animator = null
  private[animation] var customAppearingAnim: Animator = null
  private[animation] var customDisappearingAnim: Animator = null
  private[animation] var customChangingAppearingAnim: Animator = null
  private[animation] var customChangingDisappearingAnim: Animator = null
  private[animation] var currentAppearingAnim: Animator = null
  private[animation] var currentDisappearingAnim: Animator = null
  private[animation] var currentChangingAppearingAnim: Animator = null
  private[animation] var currentChangingDisappearingAnim: Animator = null

  /** Called when the activity is first created. */
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.layout_animations)
    container = new FixedGridLayout(this)
    container.setClipChildren(false)
    (container.asInstanceOf[FixedGridLayout]).setCellHeight(90)
    (container.asInstanceOf[FixedGridLayout]).setCellWidth(100)
    val transitioner: LayoutTransition = new LayoutTransition
    container.setLayoutTransition(transitioner)
    defaultAppearingAnim = transitioner.getAnimator(LayoutTransition.APPEARING)
    defaultDisappearingAnim = transitioner.getAnimator(LayoutTransition.DISAPPEARING)
    defaultChangingAppearingAnim = transitioner.getAnimator(LayoutTransition.CHANGE_APPEARING)
    defaultChangingDisappearingAnim = transitioner.getAnimator(LayoutTransition.CHANGE_DISAPPEARING)
    createCustomAnimations(transitioner)
    currentAppearingAnim = defaultAppearingAnim
    currentDisappearingAnim = defaultDisappearingAnim
    currentChangingAppearingAnim = defaultChangingAppearingAnim
    currentChangingDisappearingAnim = defaultChangingDisappearingAnim
    val parent: ViewGroup = findViewById(R.id.parent).asInstanceOf[ViewGroup]
    parent.addView(container)
    parent.setClipChildren(false)
    val addButton: Button = findViewById(R.id.addNewButton).asInstanceOf[Button]
    addButton.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        val newButton: Button = new Button(LayoutAnimations.this)
        newButton.setText(String.valueOf(({
          numButtons += 1; numButtons - 1
        })))
        newButton.setOnClickListener(new View.OnClickListener {
          def onClick(v: View) {
            container.removeView(v)
          }
        })
        container.addView(newButton, Math.min(1, container.getChildCount))
      }
    })
    val customAnimCB: CheckBox = findViewById(R.id.customAnimCB).asInstanceOf[CheckBox]
    customAnimCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener {
      def onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        setupTransition(transitioner)
      }
    })
    val appearingCB: CheckBox = findViewById(R.id.appearingCB).asInstanceOf[CheckBox]
    appearingCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener {
      def onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        setupTransition(transitioner)
      }
    })
    val disappearingCB: CheckBox = findViewById(R.id.disappearingCB).asInstanceOf[CheckBox]
    disappearingCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener {
      def onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        setupTransition(transitioner)
      }
    })
    val changingAppearingCB: CheckBox = findViewById(R.id.changingAppearingCB).asInstanceOf[CheckBox]
    changingAppearingCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener {
      def onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        setupTransition(transitioner)
      }
    })
    val changingDisappearingCB: CheckBox = findViewById(R.id.changingDisappearingCB).asInstanceOf[CheckBox]
    changingDisappearingCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener {
      def onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        setupTransition(transitioner)
      }
    })
  }

  private def setupTransition(transition: LayoutTransition) {
    val customAnimCB: CheckBox = findViewById(R.id.customAnimCB).asInstanceOf[CheckBox]
    val appearingCB: CheckBox = findViewById(R.id.appearingCB).asInstanceOf[CheckBox]
    val disappearingCB: CheckBox = findViewById(R.id.disappearingCB).asInstanceOf[CheckBox]
    val changingAppearingCB: CheckBox = findViewById(R.id.changingAppearingCB).asInstanceOf[CheckBox]
    val changingDisappearingCB: CheckBox = findViewById(R.id.changingDisappearingCB).asInstanceOf[CheckBox]
    transition.setAnimator(LayoutTransition.APPEARING, if (appearingCB.isChecked) (if (customAnimCB.isChecked) customAppearingAnim else defaultAppearingAnim) else null)
    transition.setAnimator(LayoutTransition.DISAPPEARING, if (disappearingCB.isChecked) (if (customAnimCB.isChecked) customDisappearingAnim else defaultDisappearingAnim) else null)
    transition.setAnimator(LayoutTransition.CHANGE_APPEARING, if (changingAppearingCB.isChecked) (if (customAnimCB.isChecked) customChangingAppearingAnim else defaultChangingAppearingAnim) else null)
    transition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, if (changingDisappearingCB.isChecked) (if (customAnimCB.isChecked) customChangingDisappearingAnim else defaultChangingDisappearingAnim) else null)
  }

  private def createCustomAnimations(transition: LayoutTransition) {
    val pvhLeft: PropertyValuesHolder = PropertyValuesHolder.ofInt("left", 0, 1)
    val pvhTop: PropertyValuesHolder = PropertyValuesHolder.ofInt("top", 0, 1)
    val pvhRight: PropertyValuesHolder = PropertyValuesHolder.ofInt("right", 0, 1)
    val pvhBottom: PropertyValuesHolder = PropertyValuesHolder.ofInt("bottom", 0, 1)
    val pvhScaleX: PropertyValuesHolder = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f)
    val pvhScaleY: PropertyValuesHolder = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f)
    customChangingAppearingAnim = ObjectAnimator.ofPropertyValuesHolder(this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScaleX, pvhScaleY).setDuration(transition.getDuration(LayoutTransition.CHANGE_APPEARING))
    customChangingAppearingAnim.addListener(new AnimatorListenerAdapter {
      override def onAnimationEnd(anim: Animator) {
        val view: View = (anim.asInstanceOf[ObjectAnimator]).getTarget.asInstanceOf[View]
        view.setScaleX(1f)
        view.setScaleY(1f)
      }
    })
    val kf0: Keyframe = Keyframe.ofFloat(0f, 0f)
    val kf1: Keyframe = Keyframe.ofFloat(.9999f, 360f)
    val kf2: Keyframe = Keyframe.ofFloat(1f, 0f)
    val pvhRotation: PropertyValuesHolder = PropertyValuesHolder.ofKeyframe("rotation", kf0, kf1, kf2)
    customChangingDisappearingAnim = ObjectAnimator.ofPropertyValuesHolder(this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhRotation).setDuration(transition.getDuration(LayoutTransition.CHANGE_DISAPPEARING))
    customChangingDisappearingAnim.addListener(new AnimatorListenerAdapter {
      override def onAnimationEnd(anim: Animator) {
        val view: View = (anim.asInstanceOf[ObjectAnimator]).getTarget.asInstanceOf[View]
        view.setRotation(0f)
      }
    })
    customAppearingAnim = ObjectAnimator.ofFloat(null, "rotationY", 90f, 0f).setDuration(transition.getDuration(LayoutTransition.APPEARING))
    customAppearingAnim.addListener(new AnimatorListenerAdapter {
      override def onAnimationEnd(anim: Animator) {
        val view: View = (anim.asInstanceOf[ObjectAnimator]).getTarget.asInstanceOf[View]
        view.setRotationY(0f)
      }
    })
    customDisappearingAnim = ObjectAnimator.ofFloat(null, "rotationX", 0f, 90f).setDuration(transition.getDuration(LayoutTransition.DISAPPEARING))
    customDisappearingAnim.addListener(new AnimatorListenerAdapter {
      override def onAnimationEnd(anim: Animator) {
        val view: View = (anim.asInstanceOf[ObjectAnimator]).getTarget.asInstanceOf[View]
        view.setRotationX(0f)
      }
    })
  }
}