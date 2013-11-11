package com.example.android.apis.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.widget.LinearLayout
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
import org.scaloid.common._

//remove if not needed
import scala.collection.JavaConversions._

class LayoutAnimationsHideShow extends SActivity {

  private var numButtons: Int = 1

  var container: ViewGroup = null

  private var mTransitioner: LayoutTransition = _

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.layout_animations_hideshow)
    val hideGoneCB = findViewById(R.id.hideGoneCB).asInstanceOf[CheckBox]
    container = new LinearLayout(this)
    container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    for (i <- 0 until 4) {
      val newButton = new Button(this)
      newButton.setText(String.valueOf(i))
      container.addView(newButton)
      newButton.setOnClickListener(new View.OnClickListener() {

        def onClick(v: View) {
          v.setVisibility(if (hideGoneCB.isChecked) View.GONE else View.INVISIBLE)
        }
      })
    }
    resetTransition()
    val parent = findViewById(R.id.parent).asInstanceOf[ViewGroup]
    parent.addView(container)
    val addButton = findViewById(R.id.addNewButton).asInstanceOf[Button]
    addButton.setOnClickListener(new View.OnClickListener() {

      def onClick(v: View) {
        for (i <- 0 until container.getChildCount) {
          val view = container.getChildAt(i).asInstanceOf[View]
          view.setVisibility(View.VISIBLE)
        }
      }
    })
    val customAnimCB = findViewById(R.id.customAnimCB).asInstanceOf[CheckBox]
    customAnimCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      def onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        var duration: Long = 0l
        if (isChecked) {
          mTransitioner.setStagger(LayoutTransition.CHANGE_APPEARING, 30)
          mTransitioner.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 30)
          setupCustomAnimations()
          duration = 500
        } else {
          resetTransition()
          duration = 300
        }
        mTransitioner.setDuration(duration)
      }
    })
  }

  private def resetTransition() {
    mTransitioner = new LayoutTransition()
    container.setLayoutTransition(mTransitioner)
  }

  private def setupCustomAnimations() {
    val pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1)
    val pvhTop = PropertyValuesHolder.ofInt("top", 0, 1)
    val pvhRight = PropertyValuesHolder.ofInt("right", 0, 1)
    val pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 1)
    val pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f)
    val pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f)
    val changeIn = ObjectAnimator.ofPropertyValuesHolder(this, pvhLeft, pvhTop, pvhRight, pvhBottom,
      pvhScaleX, pvhScaleY)
      .setDuration(mTransitioner.getDuration(LayoutTransition.CHANGE_APPEARING))
    mTransitioner.setAnimator(LayoutTransition.CHANGE_APPEARING, changeIn)
    changeIn.addListener(new AnimatorListenerAdapter() {

      override def onAnimationEnd(anim: Animator) {
        val view = anim.asInstanceOf[ObjectAnimator].getTarget.asInstanceOf[View]
        view.setScaleX(1f)
        view.setScaleY(1f)
      }
    })
    val kf0 = Keyframe.ofFloat(0f, 0f)
    val kf1 = Keyframe.ofFloat(.9999f, 360f)
    val kf2 = Keyframe.ofFloat(1f, 0f)
    val pvhRotation = PropertyValuesHolder.ofKeyframe("rotation", kf0, kf1, kf2)
    val changeOut = ObjectAnimator.ofPropertyValuesHolder(this, pvhLeft, pvhTop, pvhRight, pvhBottom,
      pvhRotation)
      .setDuration(mTransitioner.getDuration(LayoutTransition.CHANGE_DISAPPEARING))
    mTransitioner.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, changeOut)
    changeOut.addListener(new AnimatorListenerAdapter() {

      override def onAnimationEnd(anim: Animator) {
        val view = anim.asInstanceOf[ObjectAnimator].getTarget.asInstanceOf[View]
        view.setRotation(0f)
      }
    })
    val animIn = ObjectAnimator.ofFloat(null, "rotationY", 90f, 0f).setDuration(mTransitioner.getDuration(LayoutTransition.APPEARING))
    mTransitioner.setAnimator(LayoutTransition.APPEARING, animIn)
    animIn.addListener(new AnimatorListenerAdapter() {

      override def onAnimationEnd(anim: Animator) {
        val view = anim.asInstanceOf[ObjectAnimator].getTarget.asInstanceOf[View]
        view.setRotationY(0f)
      }
    })
    val animOut = ObjectAnimator.ofFloat(null, "rotationX", 0f, 90f).setDuration(mTransitioner.getDuration(LayoutTransition.DISAPPEARING))
    mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut)
    animOut.addListener(new AnimatorListenerAdapter() {

      override def onAnimationEnd(anim: Animator) {
        val view = anim.asInstanceOf[ObjectAnimator].getTarget.asInstanceOf[View]
        view.setRotationX(0f)
      }
    })
  }
}
