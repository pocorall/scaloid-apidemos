package com.example.android.apis.animation

import android.animation.AnimatorListenerAdapter
import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.android.apis.R
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import ListFlipper._
import org.scaloid.common._

//remove if not needed
import scala.collection.JavaConversions._

object ListFlipper {

  private val DURATION = 1500

  private val LIST_STRINGS_EN = Array("One", "Two", "Three", "Four", "Five", "Six")

  private val LIST_STRINGS_FR = Array("Un", "Deux", "Trois", "Quatre", "Le Five", "Six")
}

class ListFlipper extends SActivity {

  private var mSeekBar: SeekBar = _

  var mEnglishList: ListView = _

  var mFrenchList: ListView = _

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.rotating_list)
    mEnglishList = findViewById(R.id.list_en).asInstanceOf[ListView]
    mFrenchList = findViewById(R.id.list_fr).asInstanceOf[ListView]
    val adapterEn = new ArrayAdapter[String](this, android.R.layout.simple_list_item_1, LIST_STRINGS_EN)
    val adapterFr = new ArrayAdapter[String](this, android.R.layout.simple_list_item_1, LIST_STRINGS_FR)
    mEnglishList.setAdapter(adapterEn)
    mFrenchList.setAdapter(adapterFr)
    mFrenchList.setRotationY(-90f)
    val starter = findViewById(R.id.button).asInstanceOf[Button]
    starter.setOnClickListener(new View.OnClickListener() {

      def onClick(v: View) {
        flipit()
      }
    })
  }

  private var accelerator: Interpolator = new AccelerateInterpolator()

  private var decelerator: Interpolator = new DecelerateInterpolator()

  private def flipit() {
    var visibleList: ListView = null
    var invisibleList: ListView = null
    if (mEnglishList.getVisibility == View.GONE) {
      visibleList = mFrenchList
      invisibleList = mEnglishList
    } else {
      invisibleList = mFrenchList
      visibleList = mEnglishList
    }
    val visToInvis = ObjectAnimator.ofFloat(visibleList, "rotationY", 0f, 90f)
    visToInvis.setDuration(500)
    visToInvis.setInterpolator(accelerator)
    val invisToVis = ObjectAnimator.ofFloat(invisibleList, "rotationY", -90f, 0f)
    invisToVis.setDuration(500)
    invisToVis.setInterpolator(decelerator)
    visToInvis.addListener(new AnimatorListenerAdapter() {

      override def onAnimationEnd(anim: Animator) {
        visibleList.setVisibility(View.GONE)
        invisToVis.start()
        invisibleList.setVisibility(View.VISIBLE)
      }
    })
    visToInvis.start()
  }
}
