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
import android.app.SearchManager
import android.app.SearchableInfo
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.MenuItem.OnActionExpandListener
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import java.util.List

/**
 * This demonstrates the usage of SearchView in an ActionBar as a menu item.
 * It sets a SearchableInfo on the SearchView for suggestions and submitting queries to.
 */
class SearchViewActionBar extends Activity with SearchView.OnQueryTextListener {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    getWindow.requestFeature(Window.FEATURE_ACTION_BAR)
    setContentView(R.layout.searchview_actionbar)
    mStatusView = findViewById(R.id.status_text).asInstanceOf[TextView]
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    super.onCreateOptionsMenu(menu)
    val inflater: MenuInflater = getMenuInflater
    inflater.inflate(R.menu.searchview_in_menu, menu)
    val searchItem: MenuItem = menu.findItem(R.id.action_search)
    mSearchView = searchItem.getActionView.asInstanceOf[SearchView]
    setupSearchView(searchItem)
    return true
  }

  private def setupSearchView(searchItem: MenuItem) {
    if (isAlwaysExpanded) {
      mSearchView.setIconifiedByDefault(false)
    }
    else {
      searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
    }
    val searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE).asInstanceOf[SearchManager]
    if (searchManager != null) {
      val searchables: List[SearchableInfo] = searchManager.getSearchablesInGlobalSearch
      var info: SearchableInfo = searchManager.getSearchableInfo(getComponentName)
      import scala.collection.JavaConversions._
      for (inf <- searchables) {
        if (inf.getSuggestAuthority != null && inf.getSuggestAuthority.startsWith("applications")) {
          info = inf
        }
      }
      mSearchView.setSearchableInfo(info)
    }
    mSearchView.setOnQueryTextListener(this)
  }

  def onQueryTextChange(newText: String): Boolean = {
    mStatusView.setText("Query = " + newText)
    return false
  }

  def onQueryTextSubmit(query: String): Boolean = {
    mStatusView.setText("Query = " + query + " : submitted")
    return false
  }

  def onClose: Boolean = {
    mStatusView.setText("Closed!")
    return false
  }

  protected def isAlwaysExpanded: Boolean = {
    return false
  }

  private var mSearchView: SearchView = null
  private var mStatusView: TextView = null
}