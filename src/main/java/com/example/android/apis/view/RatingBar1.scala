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

import android.app.Activity
import android.os.Bundle
import android.widget.RatingBar
import android.widget.TextView
import com.example.android.apis.R

/**
 * Demonstrates how to use a rating bar
 */
class RatingBar1 extends Activity with RatingBar.OnRatingBarChangeListener {
  private[view] var mSmallRatingBar: RatingBar = null
  private[view] var mIndicatorRatingBar: RatingBar = null
  private[view] var mRatingText: TextView = null

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.ratingbar_1)
    mRatingText = findViewById(R.id.rating).asInstanceOf[TextView]
    mIndicatorRatingBar = findViewById(R.id.indicator_ratingbar).asInstanceOf[RatingBar]
    mSmallRatingBar = findViewById(R.id.small_ratingbar).asInstanceOf[RatingBar]
    (findViewById(R.id.ratingbar1).asInstanceOf[RatingBar]).setOnRatingBarChangeListener(this)
    (findViewById(R.id.ratingbar2).asInstanceOf[RatingBar]).setOnRatingBarChangeListener(this)
  }

  def onRatingChanged(ratingBar: RatingBar, rating: Float, fromTouch: Boolean) {
    val numStars: Int = ratingBar.getNumStars
    mRatingText.setText(getString(R.string.ratingbar_rating) + " " + rating + "/" + numStars)
    if (mIndicatorRatingBar.getNumStars != numStars) {
      mIndicatorRatingBar.setNumStars(numStars)
      mSmallRatingBar.setNumStars(numStars)
    }
    if (mIndicatorRatingBar.getRating != rating) {
      mIndicatorRatingBar.setRating(rating)
      mSmallRatingBar.setRating(rating)
    }
    val ratingBarStepSize: Float = ratingBar.getStepSize
    if (mIndicatorRatingBar.getStepSize != ratingBarStepSize) {
      mIndicatorRatingBar.setStepSize(ratingBarStepSize)
      mSmallRatingBar.setStepSize(ratingBarStepSize)
    }
  }
}