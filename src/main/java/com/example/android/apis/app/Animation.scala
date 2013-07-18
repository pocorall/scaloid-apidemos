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
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import org.scaloid.common._

/**
 * <p>Example of using a custom animation when transitioning between activities.</p>
 */
class Animation extends SActivity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_animation)
    // Watch for button clicks.
    find[Button](R.id.fade_animation).onClick  {
      // Request the next activity transition (here starting a new one).
      startActivity(new Intent(Animation.this, classOf[AlertDialogSamples]))
      // Supply a custom animation.  This one will just fade the new
      // activity on top.  Note that we need to also supply an animation
      // (here just doing nothing for the same amount of time) for the
      // old activity to prevent it from going away too soon.
      overridePendingTransition(R.anim.fade, R.anim.hold)
    }
    find[Button](R.id.zoom_animation).onClick {
      // Request the next activity transition (here starting a new one).
      startActivity(new Intent(Animation.this, classOf[AlertDialogSamples]))
      // This is a more complicated animation, involving transformations
      // on both this (exit) and the new (enter) activity.  Note how for
      // the duration of the animation we force the exiting activity
      // to be Z-ordered on top (even though it really isn't) to achieve
      // the effect we want.
      overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit)
    }

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
      find[Button](R.id.modern_fade_animation).onClick {
        // Create the desired custom animation, involving transformations
        // on both this (exit) and the new (enter) activity.  Note how for
        // the duration of the animation we force the exiting activity
        // to be Z-ordered on top (even though it really isn't) to achieve
        // the effect we want.
        val opts = ActivityOptions.makeCustomAnimation(Animation.this, R.anim.fade, R.anim.hold)
        // Request the activity be started, using the custom animation options.
        startActivity(new Intent(Animation.this, classOf[AlertDialogSamples]), opts.toBundle)
      }
      find[Button](R.id.modern_zoom_animation).onClick {
        // Create a more complicated animation, involving transformations
        // on both this (exit) and the new (enter) activity.  Note how for
        // the duration of the animation we force the exiting activity
        // to be Z-ordered on top (even though it really isn't) to achieve
        // the effect we want.
        val opts = ActivityOptions.makeCustomAnimation(Animation.this, R.anim.zoom_enter, R.anim.zoom_enter)
        // Request the activity be started, using the custom animation options.
        startActivity(new Intent(Animation.this, classOf[AlertDialogSamples]), opts.toBundle)
      }
      find[Button](R.id.scale_up_animation).onClick {
        // Create a scale-up animation that originates at the button
        // being pressed.
        (v: View) => val opts = ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth, v.getHeight)
        // Request the activity be started, using the custom animation options.
        startActivity(new Intent(Animation.this, classOf[AlertDialogSamples]), opts.toBundle)
      }
      find[Button](R.id.zoom_thumbnail_animation).onClick {
        // Create a thumbnail animation.  We are going to build our thumbnail
        // just from the view that was pressed.  We make sure the view is
        // not selected, because by the time the animation starts we will
        // have finished with the selection of the tap.

          (v: View) => v.setDrawingCacheEnabled(true)
          (v: View) => v.setPressed(false)
          (v: View) => v.refreshDrawableState
          (v: View) => val bm: Bitmap = v.getDrawingCache
        val c = new Canvas(bm)
        //c.drawARGB(255, 255, 0, 0);
          (v: View) => val opts = ActivityOptions.makeThumbnailScaleUpAnimation(v, bm, 0, 0)
        // Request the activity be started, using the custom animation options.
        startActivity(new Intent(Animation.this, classOf[AlertDialogSamples]), opts.toBundle)
          (v: View) => v.setDrawingCacheEnabled(false)
      }
    }
    else {
      findViewById(R.id.modern_fade_animation).enabled(false)
      findViewById(R.id.modern_zoom_animation).enabled(false)
      findViewById(R.id.scale_up_animation).enabled(false)
      findViewById(R.id.zoom_thumbnail_animation).enabled(false)
    }
  }
}