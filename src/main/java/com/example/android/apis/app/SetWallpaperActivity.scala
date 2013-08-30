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
import java.io.IOException
import android.app.WallpaperManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import org.scaloid.common._

/**
 * <h3>SetWallpaper Activity</h3>
 *
 * <p>This demonstrates the how to write an activity that gets the current system wallpaper,
 * modifies it and sets the modified bitmap as system wallpaper.</p>
 */
object SetWallpaperActivity {
  private val mColors: Array[Int] = Array(Color.BLUE, Color.GREEN, Color.RED, Color.LTGRAY, Color.MAGENTA, Color.CYAN, Color.YELLOW, Color.WHITE)
}
class SetWallpaperActivity extends SActivity {
  /**
   * Initialization of the Activity after it is first created.  Must at least
   * call {@link android.app.Activity#setContentView setContentView()} to
   * describe what is to be displayed in the screen.
   */
  protected override def onCreate(savedInstanceState: Bundle) {
    // Be sure to call the super class.
    super.onCreate(savedInstanceState)
    // See res/layout/wallpaper_2.xml for this
    // view layout definition, which is being set here as
    // the content of our screen.
    setContentView(R.layout.wallpaper_2)
    val wallpaperManager = WallpaperManager.getInstance(this)
    val wallpaperDrawable = wallpaperManager.getDrawable
    val imageView = find[ImageView](R.id.imageview)
    imageView.setDrawingCacheEnabled(true)
    imageView.setImageDrawable(wallpaperDrawable)
    find[Button](R.id.randomize).onClick  {
        val mColor = Math.floor(Math.random * mColors.length).asInstanceOf[Int]
        wallpaperDrawable.setColorFilter(mColors(mColor), PorterDuff.Mode.MULTIPLY)
        imageView.setImageDrawable(wallpaperDrawable)
        imageView.invalidate
      }
       find[Button](R.id.setwallpaper).onClick {
        try {
          wallpaperManager.setBitmap(imageView.getDrawingCache)
          finish
        }
        catch {
          case e: IOException => {
            e.printStackTrace
          }
        }
      }
  }
  val mColors: Array[Int] = Array(Color.BLUE, Color.GREEN, Color.RED, Color.LTGRAY, Color.MAGENTA, Color.CYAN, Color.YELLOW, Color.WHITE)
}