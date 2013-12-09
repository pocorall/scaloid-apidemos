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
package com.example.android.apis.app

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

import com.example.android.apis.R
import android.app.Activity
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.RelativeLayout

/**
 * Activity used by StatusBarNotification to show the notification to the user.
 */
class NotificationDisplay extends Activity with View.OnClickListener {
  // Scaloid >>
  val RelativeLayout_LayoutParams_WRAP_CONTENT = android.view.ViewGroup.LayoutParams.WRAP_CONTENT
  val NOTIFICATION_SERVICE = android.content.Context.NOTIFICATION_SERVICE
  // Scaloid <<

  /**
   * Initialization of the Activity after it is first created.  Must at least
   * call {@link android.app.Activity#setContentView setContentView()} to
   * describe what is to be displayed in the screen.
   */
  protected override def onCreate(icicle: Bundle) {
    super.onCreate(icicle)
    getWindow.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
    val container: RelativeLayout = new RelativeLayout(this)
    val button: ImageButton = new ImageButton(this)
    button.setImageResource(getIntent.getIntExtra("moodimg", 0))
    button.setOnClickListener(this)
    val lp: RelativeLayout.LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout_LayoutParams_WRAP_CONTENT, RelativeLayout_LayoutParams_WRAP_CONTENT)
    lp.addRule(RelativeLayout.CENTER_IN_PARENT)
    container.addView(button, lp)
    setContentView(container)
  }

  def onClick(v: View) {
    (getSystemService(NOTIFICATION_SERVICE).asInstanceOf[NotificationManager]).cancel(R.layout.status_bar_notifications)
    val intent: Intent = new Intent(this, classOf[StatusBarNotifications])
    intent.setAction(Intent.ACTION_MAIN)
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
    finish
  }
}