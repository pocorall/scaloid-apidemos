/*
 * Copyright (C) 2008 The Android Open Source Project
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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.TextView
import com.example.android.apis.R

/**
 * This Activity actually handles two stages of a launcher shortcut's life cycle.
 *
 * 1. Your application offers to provide shortcuts to the launcher.  When
 * the user installs a shortcut, an activity within your application
 * generates the actual shortcut and returns it to the launcher, where it
 * is shown to the user as an icon.
 *
 * 2. Any time the user clicks on an installed shortcut, an intent is sent.
 * Typically this would then be handled as necessary by an activity within
 * your application.
 *
 * We handle stage 1 (creating a shortcut) by simply sending back the information (in the form
 * of an {@link android.content.Intent} that the launcher will use to create the shortcut.
 *
 * You can also implement this in an interactive way, by having your activity actually present
 * UI for the user to select the specific nature of the shortcut, such as a contact, picture, URL,
 * media item, or action.
 *
 * We handle stage 2 (responding to a shortcut) in this sample by simply displaying the contents
 * of the incoming {@link android.content.Intent}.
 *
 * In a real application, you would probably use the shortcut intent to display specific content
 * or start a particular operation.
 */
class LauncherShortcuts extends Activity {
  // Scaloid >>
  val RESULT_OK = android.app.Activity.RESULT_OK
  // Scaloid <<

  private final val EXTRA_KEY: String = "com.example.android.apis.app.LauncherShortcuts"

  override def onCreate(icicle: Bundle) {
    super.onCreate(icicle)
    val intent: Intent = getIntent
    val action: String = intent.getAction
    if (Intent.ACTION_CREATE_SHORTCUT == action) {
      setupShortcut
      finish
      return
    }
    setContentView(R.layout.launcher_shortcuts)
    val intentInfo: TextView = findViewById(R.id.txt_shortcut_intent).asInstanceOf[TextView]
    var info: String = intent.toString
    val extra: String = intent.getStringExtra(EXTRA_KEY)
    if (extra != null) {
      info = info + " " + extra
    }
    intentInfo.setText(info)
  }

  /**
   * This function creates a shortcut and returns it to the caller.  There are actually two
   * intents that you will send back.
   *
   * The first intent serves as a container for the shortcut and is returned to the launcher by
   * setResult().  This intent must contain three fields:
   *
   * <ul>
   * <li>{@link android.content.Intent#EXTRA_SHORTCUT_INTENT} The shortcut intent.</li>
   * <li>{@link android.content.Intent#EXTRA_SHORTCUT_NAME} The text that will be displayed with
   * the shortcut.</li>
   * <li>{@link android.content.Intent#EXTRA_SHORTCUT_ICON} The shortcut's icon, if provided as a
   * bitmap, <i>or</i> {@link android.content.Intent#EXTRA_SHORTCUT_ICON_RESOURCE} if provided as
   * a drawable resource.</li>
   * </ul>
   *
   * If you use a simple drawable resource, note that you must wrapper it using
   * {@link android.content.Intent.ShortcutIconResource}, as shown below.  This is required so
   * that the launcher can access resources that are stored in your application's .apk file.  If
   * you return a bitmap, such as a thumbnail, you can simply put the bitmap into the extras
   * bundle using {@link android.content.Intent#EXTRA_SHORTCUT_ICON}.
   *
   * The shortcut intent can be any intent that you wish the launcher to send, when the user
   * clicks on the shortcut.  Typically this will be {@link android.content.Intent#ACTION_VIEW}
   * with an appropriate Uri for your content, but any Intent will work here as long as it
   * triggers the desired action within your Activity.
   */
  private def setupShortcut {
    val shortcutIntent: Intent = new Intent(Intent.ACTION_MAIN)
    shortcutIntent.setClassName(this, this.getClass.getName)
    shortcutIntent.putExtra(EXTRA_KEY, "ApiDemos Provided This Shortcut")
    val intent: Intent = new Intent
    intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent)
    intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.shortcut_name))
    val iconResource: Parcelable = Intent.ShortcutIconResource.fromContext(this, R.drawable.app_sample_code)
    intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource)
    setResult(RESULT_OK, intent)
  }
}