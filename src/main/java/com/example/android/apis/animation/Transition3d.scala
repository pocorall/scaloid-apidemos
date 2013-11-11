package com.example.android.apis.animation

import com.example.android.apis.R
import android.app.Activity
import android.os.Bundle
import android.widget.ListView
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.ImageView
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import Transition3d._
import org.scaloid.common._

//remove if not needed
import scala.collection.JavaConversions._

object Transition3d {

  private val PHOTOS_NAMES = Array("Lyon", "Livermore", "Tahoe Pier", "Lake Tahoe", "Grand Canyon", "Bodie")

  private val PHOTOS_RESOURCES = Array(R.drawable.photo1, R.drawable.photo2, R.drawable.photo3, R.drawable.photo4, R.drawable.photo5, R.drawable.photo6)
}

class Transition3d extends SActivity with AdapterView.OnItemClickListener with View.OnClickListener {

  private var mPhotosList: ListView = _

  private var mContainer: ViewGroup = _

  private var mImageView: ImageView = _

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.animations_main_screen)
    mPhotosList = findViewById(android.R.id.list).asInstanceOf[ListView]
    mImageView = findViewById(R.id.picture).asInstanceOf[ImageView]
    mContainer = findViewById(R.id.container).asInstanceOf[ViewGroup]
    val adapter = new ArrayAdapter[String](this, android.R.layout.simple_list_item_1, PHOTOS_NAMES)
    mPhotosList.setAdapter(adapter)
    mPhotosList.setOnItemClickListener(this)
    mImageView.setClickable(true)
    mImageView.setFocusable(true)
    mImageView.setOnClickListener(this)
    mContainer.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE)
  }

  private def applyRotation(position: Int, start: Float, end: Float) {
    val centerX = mContainer.getWidth / 2.0f
    val centerY = mContainer.getHeight / 2.0f
    val rotation = new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true)
    rotation.setDuration(500)
    rotation.setFillAfter(true)
    rotation.setInterpolator(new AccelerateInterpolator())
    rotation.setAnimationListener(new DisplayNextView(position))
    mContainer.startAnimation(rotation)
  }

  def onItemClick(parent: AdapterView[_],
                  v: View,
                  position: Int,
                  id: Long) {
    mImageView.setImageResource(PHOTOS_RESOURCES(position))
    applyRotation(position, 0, 90)
  }

  def onClick(v: View) {
    applyRotation(-1, 180, 90)
  }

  private class DisplayNextView ( val mPosition: Int) extends Animation.AnimationListener {

    def onAnimationStart(animation: Animation) {
    }

    def onAnimationEnd(animation: Animation) {
      mContainer.post(new SwapViews(mPosition))
    }

    def onAnimationRepeat(animation: Animation) {
    }
  }

  private class SwapViews(private val mPosition: Int) extends Runnable {

    def run() {
      val centerX = mContainer.getWidth / 2.0f
      val centerY = mContainer.getHeight / 2.0f
      var rotation: Rotate3dAnimation = null
      if (mPosition > -1) {
        mPhotosList.setVisibility(View.GONE)
        mImageView.setVisibility(View.VISIBLE)
        mImageView.requestFocus()
        rotation = new Rotate3dAnimation(90, 180, centerX, centerY, 310.0f, false)
      } else {
        mImageView.setVisibility(View.GONE)
        mPhotosList.setVisibility(View.VISIBLE)
        mPhotosList.requestFocus()
        rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 310.0f, false)
      }
      rotation.setDuration(500)
      rotation.setFillAfter(true)
      rotation.setInterpolator(new DecelerateInterpolator())
      mContainer.startAnimation(rotation)
    }
  }
}
