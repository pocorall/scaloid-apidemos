/*
 * Copyright (C) 2010 The Android Open Source Project
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

import com.example.android.apis.R
import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.Toast

/**
 * This activity demonstrates two different ways in which views can be made more secure to
 * touch spoofing attacks by leveraging framework features.
 *
 * The activity presents 3 buttons that obtensibly perform a risky security critical
 * function.  Under ordinary circumstances, the user would never click on these buttons
 * or would at least think long and hard about it.  However, a carefully crafted toast can
 * overlay the contents of the activity in such a way as to make the user believe the buttons
 * are innocuous.  Since the toast cannot receive input, the touches are passed down to the
 * activity potentially yielding an effect other than what the user intended.
 *
 * To simulate the spoofing risk, this activity pops up a specially crafted overlay as
 * a toast layed out so as to cover the buttons and part of the descriptive text.
 * For the purposes of this demonstration, pretend that the overlay was actually popped
 * up by a malicious application published by the International Cabal of Evil Penguins.
 *
 * The 3 buttons are set up as follows:
 *
 * 1. The "unsecured button" does not apply any touch filtering of any kind.
 * When the toast appears, this button remains clickable as usual which creates an
 * opportunity for spoofing to occur.
 *
 * 2. The "built-in secured button" leverages the android:filterTouchesWhenObscured view
 * attribute to ask the framework to filter out touches when the window is obscured.
 * When the toast appears, the button does not receive the touch and appears to be inoperable.
 *
 * 3. The "custom secured button" adds a touch listener to the button which intercepts the
 * touch event and checks whether the window is obscured.  If so, it warns the user and
 * drops the touch event.  This example is intended to demonstrate how a view can
 * perform its own filtering and provide additional feedback by examining the {@MotionEvent}
 * flags to determine whether the window is obscured.  Here we use a touch listener but
 * a custom view subclass could perform the filtering by overriding
 * {@link View#onFilterTouchEventForSecurity(MotionEvent)}.
 *
 * Refer to the comments on {@View} for more information about view security.
 */
class SecureView extends Activity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.secure_view)
    val toastButton: Button = findViewById(R.id.secure_view_toast_button).asInstanceOf[Button]
    toastButton.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        showOverlay
      }
    })
    val unsecureButton: Button = findViewById(R.id.secure_view_unsecure_button).asInstanceOf[Button]
    setClickedAction(unsecureButton)
    val builtinSecureButton: Button = findViewById(R.id.secure_view_builtin_secure_button).asInstanceOf[Button]
    setClickedAction(builtinSecureButton)
    val customSecureButton: Button = findViewById(R.id.secure_view_custom_secure_button).asInstanceOf[Button]
    setClickedAction(customSecureButton)
    setTouchFilter(customSecureButton)
  }

  private def showOverlay {
    val overlay: SecureViewOverlay = getLayoutInflater.inflate(R.layout.secure_view_overlay, null).asInstanceOf[SecureViewOverlay]
    overlay.setActivityToSpoof(this)
    val toast: Toast = new Toast(getApplicationContext)
    toast.setGravity(Gravity.FILL, 0, 0)
    toast.setView(overlay)
    toast.show
  }

  private def setClickedAction(button: Button) {
    button.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        val messages: Array[String] = getResources.getStringArray(R.array.secure_view_clicked)
        val message: String = messages(({
          mClickCount += 1; mClickCount - 1
        }) % messages.length)
        new AlertDialog.Builder(SecureView.this).setTitle(R.string.secure_view_action_dialog_title).setMessage(message).setNeutralButton(getResources.getString(R.string.secure_view_action_dialog_dismiss), null).show
      }
    })
  }

  private def setTouchFilter(button: Button) {
    button.setOnTouchListener(new View.OnTouchListener {
      def onTouch(v: View, event: MotionEvent): Boolean = {
        if ((event.getFlags & MotionEvent.FLAG_WINDOW_IS_OBSCURED) != 0) {
          if (event.getAction == MotionEvent.ACTION_UP) {
            new AlertDialog.Builder(SecureView.this).setTitle(R.string.secure_view_caught_dialog_title).setMessage(R.string.secure_view_caught_dialog_message).setNeutralButton(getResources.getString(R.string.secure_view_caught_dialog_dismiss), null).show
          }
          return true
        }
        return false
      }
    })
  }

  private var mClickCount: Int = 0
}