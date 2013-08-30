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
import android.view.Gravity
import android.graphics.drawable.Drawable

/**
 * <h3>SetWallpaper Activity</h3>
 *
 * <p>This demonstrates the how to write an activity that gets the current system wallpaper,
 * modifies it and sets the modified bitmap as system wallpaper.</p>
 */
class SetWallpaperActivity extends SActivity {
  onCreate {
    // See res/layout/wallpaper_2.xml for this
    // view layout definition, which is being set here as
    // the content of our screen.
    contentView = new SFrameLayout {
      imageView = SImageView()
      this += new SLinearLayout {
        SButton(R.string.randomize, {
          wallpaperDrawable.setColorFilter(mColors(Math.floor(Math.random * mColors.length).asInstanceOf[Int]), PorterDuff.Mode.MULTIPLY)
          imageView.setImageDrawable(wallpaperDrawable)
          imageView.invalidate
        }).<<.wrap.>>
        SButton(R.string.set_wallpaper, {
          try {
            wallpaperManager.setBitmap(imageView.getDrawingCache)
            finish
          }
          catch {
            case e: IOException => {
              e.printStackTrace
            }
          }
        }).<<.wrap.>>
      }
    }
    wallpaperManager = WallpaperManager.getInstance(this)
    wallpaperDrawable = wallpaperManager.getDrawable
    imageView.setDrawingCacheEnabled(true)
    imageView.setImageDrawable(wallpaperDrawable)
  }
  val mColors: Array[Int] = Array(Color.BLUE, Color.GREEN, Color.RED, Color.LTGRAY, Color.MAGENTA, Color.CYAN, Color.YELLOW, Color.WHITE)
  var imageView: SImageView = null
  var wallpaperManager: WallpaperManager = null
  var wallpaperDrawable: Drawable =   null
}