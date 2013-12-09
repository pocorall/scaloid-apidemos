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

import android.app.ListActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnKeyListener
import android.widget.ArrayAdapter
import android.widget.EditText
import com.example.android.apis.R
import java.util.ArrayList

/**
 * Demonstrates the using a list view in transcript mode
 *
 */
class List12 extends ListActivity with OnClickListener with OnKeyListener {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.list_12)
    mAdapter = new ArrayAdapter[String](this, android.R.layout.simple_list_item_1, mStrings)
    setListAdapter(mAdapter)
    mUserText = findViewById(R.id.userText).asInstanceOf[EditText]
    mUserText.setOnClickListener(this)
    mUserText.setOnKeyListener(this)
  }

  def onClick(v: View) {
    sendText
  }

  private def sendText {
    val text: String = mUserText.getText.toString
    mAdapter.add(text)
    mUserText.setText(null)
  }

  def onKey(v: View, keyCode: Int, event: KeyEvent): Boolean = {
    if (event.getAction == KeyEvent.ACTION_DOWN) {
      keyCode match {
        case KeyEvent.KEYCODE_DPAD_CENTER =>
        case KeyEvent.KEYCODE_ENTER =>
          sendText
          return true
        case _ =>
          return false
      }
    }
    return false
  }

  private var mUserText: EditText = null
  private var mAdapter: ArrayAdapter[String] = null
  private var mStrings: ArrayList[String] = new ArrayList[String]
}