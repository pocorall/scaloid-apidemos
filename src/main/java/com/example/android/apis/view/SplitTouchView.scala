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
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.Toast
import android.widget.AdapterView.OnItemClickListener

/**
 * Demonstrates splitting touch events across multiple views within a view group.
 */
class SplitTouchView extends Activity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.split_touch_view)
    val list1: ListView = findViewById(R.id.list1).asInstanceOf[ListView]
    val list2: ListView = findViewById(R.id.list2).asInstanceOf[ListView]
    val adapter: ListAdapter = new ArrayAdapter[String](this, android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings)
    list1.setAdapter(adapter)
    list2.setAdapter(adapter)
    list1.setOnItemClickListener(itemClickListener)
    list2.setOnItemClickListener(itemClickListener)
  }

  private var responseIndex: Int = 0
  private final val itemClickListener: AdapterView.OnItemClickListener = new AdapterView.OnItemClickListener {
    def onItemClick(parent: AdapterView[_], view: View, position: Int, id: Long) {
      val responses: Array[String] = getResources.getStringArray(R.array.cheese_responses)
      val response: String = responses(({
        responseIndex += 1; responseIndex - 1
      }) % responses.length)
      val message: String = getResources.getString(R.string.split_touch_view_cheese_toast, Cheeses.sCheeseStrings(position), response)
      val toast: Toast = Toast.makeText(getApplicationContext, message, Toast.LENGTH_SHORT)
      toast.show
    }
  }
}