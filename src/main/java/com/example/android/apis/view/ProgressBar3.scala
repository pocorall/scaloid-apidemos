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

import com.example.android.apis.R
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Button

class ProgressBar3 extends Activity {
  private final val DIALOG1_KEY: Int = 0
  private final val DIALOG2_KEY: Int = 1

  private[view] var mDialog1: ProgressDialog = null
  private[view] var mDialog2: ProgressDialog = null

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.progressbar_3)
    var button: Button = findViewById(R.id.showIndeterminate).asInstanceOf[Button]
    button.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        showDialog(DIALOG1_KEY)
      }
    })
    button = findViewById(R.id.showIndeterminateNoTitle).asInstanceOf[Button]
    button.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        showDialog(DIALOG2_KEY)
      }
    })
  }

  protected override def onCreateDialog(id: Int): Dialog = {
    id match {
      case DIALOG1_KEY => {
        val dialog: ProgressDialog = new ProgressDialog(this)
        dialog.setTitle("Indeterminate")
        dialog.setMessage("Please wait while loading...")
        dialog.setIndeterminate(true)
        dialog.setCancelable(true)
        return dialog
      }
      case DIALOG2_KEY => {
        val dialog: ProgressDialog = new ProgressDialog(this)
        dialog.setMessage("Please wait while loading...")
        dialog.setIndeterminate(true)
        dialog.setCancelable(true)
        return dialog
      }
      case _ =>
        return null

    }
    return null
  }
}