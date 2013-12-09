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

import com.example.android.apis.Shakespeare
import android.app.ListActivity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView

/**
 * A list view example where the data comes from a custom ListAdapter
 */
class List4 extends ListActivity {
  // Scaloid >>
  val LayoutParams_MATCH_PARENT = android.view.ViewGroup.LayoutParams.MATCH_PARENT
  val LayoutParams_WRAP_CONTENT = android.view.ViewGroup.LayoutParams.WRAP_CONTENT
  val VERTICAL = android.widget.LinearLayout.VERTICAL
  // Scaloid <<

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setListAdapter(new SpeechListAdapter(this))
  }

  /**
   * A sample ListAdapter that presents content from arrays of speeches and
   * text.
   *
   */
  private class SpeechListAdapter(mContext: Context) extends BaseAdapter {

    /**
     * The number of items in the list is determined by the number of speeches
     * in our array.
     *
     * @see android.widget.ListAdapter#getCount()
     */
    def getCount: Int = {
      return Shakespeare.TITLES.length
    }

    /**
     * Since the data comes from an array, just returning the index is
     * sufficent to get at the data. If we were using a more complex data
     * structure, we would return whatever object represents one row in the
     * list.
     *
     * @see android.widget.ListAdapter#getItem(int)
     */
    def getItem(position: Int): AnyRef = {
      return new Integer(position)
    }

    /**
     * Use the array index as a unique id.
     *
     * @see android.widget.ListAdapter#getItemId(int)
     */
    def getItemId(position: Int): Long = {
      return position
    }

    /**
     * Make a SpeechView to hold each row.
     *
     * @see android.widget.ListAdapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */
    def getView(position: Int, convertView: View, parent: ViewGroup): View = {
      var sv: SpeechView = null
      if (convertView == null) {
        sv = new SpeechView(mContext, Shakespeare.TITLES(position), Shakespeare.DIALOGUE(position))
      }
      else {
        sv = convertView.asInstanceOf[SpeechView]
        sv.setTitle("Hello")
        sv.setDialogue(Shakespeare.DIALOGUE(position))
      }
      return sv
    }

  }

  /**
   * We will use a SpeechView to display each speech. It's just a LinearLayout
   * with two text fields.
   *
   */
  private class SpeechView(context: Context, title: String, words: String) extends LinearLayout(context) {

      this.setOrientation(VERTICAL)
      mTitle = new TextView(context)
      mTitle.setText(title)
      addView(mTitle, new LinearLayout.LayoutParams(LayoutParams_MATCH_PARENT, LayoutParams_WRAP_CONTENT))
      mDialogue = new TextView(context)
      mDialogue.setText(words)
      addView(mDialogue, new LinearLayout.LayoutParams(LayoutParams_MATCH_PARENT, LayoutParams_WRAP_CONTENT))

    /**
     * Convenience method to set the title of a SpeechView
     */
    def setTitle(title: String) {
      mTitle.setText(title)
    }

    /**
     * Convenience method to set the dialogue of a SpeechView
     */
    def setDialogue(words: String) {
      mDialogue.setText(words)
    }

    private var mTitle: TextView = null
    private var mDialogue: TextView = null
  }

}