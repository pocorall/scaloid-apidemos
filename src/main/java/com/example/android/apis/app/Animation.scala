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
package com.example.android.apis.app

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

import com.example.android.apis.R
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.{Gravity, View}
import android.view.View.OnClickListener
import android.widget.Button
import org.scaloid.common._

/**
 * <p>Example of using a custom animation when transitioning between activities.</p>
 */
class Animation extends SActivity {
  onCreate {
    var modernFadeButton: SButton = null
    var modernZoomButton: SButton = null
    var scaleUpButton: SButton = null
    var zoomThumbnailButton: SButton = null
    contentView = new SVerticalLayout {
      STextView(R.string.activity_animation_msg).<<(MATCH_PARENT, WRAP_CONTENT).marginBottom(4 dip).Weight(0.0f)
      SButton(R.string.activity_animation_fade, {
        // Request the next activity transition (here starting a new one).
        startActivity[AlertDialogSamples]
        // Supply a custom animation.  This one will just fade the new
        // activity on top.  Note that we need to also supply an animation
        // (here just doing nothing for the same amount of time) for the
        // old activity to prevent it from going away too soon.
        overridePendingTransition(R.anim.fade, R.anim.hold)
      }).<<.wrap.>>.requestFocus()
      SButton(R.string.activity_animation_zoom, {
        // Request the next activity transition (here starting a new one).
        startActivity[AlertDialogSamples]
        // This is a more complicated animation, involving transformations
        // on both this (exit) and the new (enter) activity.  Note how for
        // the duration of the animation we force the exiting activity
        // to be Z-ordered on top (even though it really isn't) to achieve
        // the effect we want.
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit)
      }).<<.wrap.>>
      modernFadeButton = SButton(R.string.activity_modern_animation_fade).<<.wrap.>>
      modernFadeButton.requestFocus()
      modernZoomButton = SButton(R.string.activity_modern_animation_zoom).<<.wrap.>>
      scaleUpButton = SButton(R.string.activity_scale_up_animation).<<.wrap.>>
      zoomThumbnailButton = SButton(R.string.activity_zoom_thumbnail_animation).<<.wrap.>>
    }.padding(4 dip).gravity(Gravity.CENTER_HORIZONTAL)
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
      modernFadeButton.onClick(startActivityOption(ActivityOptions.makeCustomAnimation(Animation.this, R.anim.fade, R.anim.hold)))
      modernZoomButton.onClick(startActivityOption(ActivityOptions.makeCustomAnimation(Animation.this, R.anim.zoom_enter, R.anim.zoom_enter)))
      scaleUpButton.onClick((v: View) => startActivityOption(ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth, v.getHeight)))
      zoomThumbnailButton.onClick {
        (v: View) =>
          v.enableDrawingCache
          v.pressed = false
          v.refreshDrawableState()
          val bm = v.getDrawingCache
          val c = new Canvas(bm)
          startActivityOption(ActivityOptions.makeThumbnailScaleUpAnimation(v, bm, 0, 0))
          v.disableDrawingCache
      }
    }
    else {
      modernFadeButton.disable
      modernZoomButton.disable
      scaleUpButton.disable
      zoomThumbnailButton.disable
    }
    def startActivityOption(opts: ActivityOptions) {
      startActivity(SIntent[AlertDialogSamples], opts.toBundle)
    }
  }
}