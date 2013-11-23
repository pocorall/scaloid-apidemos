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
import android.text.TextUtils
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView

/**
 * Shows a list that can be filtered in-place with a SearchView in non-iconified mode.
 */
object SearchViewFilterMode {
  private final val TAG: String = "SearchViewFilterMode"
}

class SearchViewFilterMode extends Activity with SearchView.OnQueryTextListener {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    getWindow.requestFeature(Window.FEATURE_ACTION_BAR)
    setContentView(R.layout.searchview_filter)
    mSearchView = findViewById(R.id.search_view).asInstanceOf[SearchView]
    mListView = findViewById(R.id.list_view).asInstanceOf[ListView]
    var mAdapter: android.widget.ListAdapter = new ArrayAdapter[String](this, android.R.layout.simple_list_item_1, mStrings)
    mListView.setAdapter(mAdapter)
    mListView.setTextFilterEnabled(true)
    setupSearchView
  }

  private def setupSearchView {
    mSearchView.setIconifiedByDefault(false)
    mSearchView.setOnQueryTextListener(this)
    mSearchView.setSubmitButtonEnabled(false)
    mSearchView.setQueryHint(getString(R.string.cheese_hunt_hint))
  }

  def onQueryTextChange(newText: String): Boolean = {
    if (TextUtils.isEmpty(newText)) {
      mListView.clearTextFilter
    }
    else {
      mListView.setFilterText(newText.toString)
    }
    return true
  }

  def onQueryTextSubmit(query: String): Boolean = {
    return false
  }

  private var mSearchView: SearchView = null
  private var mListView: ListView = null
  private var mAdapter: ArrayAdapter[String] = null
  private final val mStrings: Array[String] = Cheeses.sCheeseStrings
}