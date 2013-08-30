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
package com.example.android.apis.app

import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.TextView
import com.example.android.apis.R
import org.scaloid.common._
/**
 * This demonstrates idiomatic usage of the Action Bar. The default Honeycomb theme
 * includes the action bar by default and a menu resource is used to populate the
 * menu data itself. If you'd like to see how these things work under the hood, see
 * ActionBarMechanics.
 */
class ActionBarUsage extends SActivity with OnQueryTextListener {
  onCreate {
    mSearchText = new TextView(this)
    setContentView(mSearchText)
  }
  override def onCreateOptionsMenu(menu: Menu) = {
    getMenuInflater.inflate(R.menu.actions, menu)
    val searchView = menu.findItem(R.id.action_search).getActionView.asInstanceOf[SearchView]
    searchView.setOnQueryTextListener(this)
    true
  }
  override def onPrepareOptionsMenu(menu: Menu) = {
    if (mSortMode != -1) {
      val icon = menu.findItem(mSortMode).getIcon
      menu.findItem(R.id.action_sort).setIcon(icon)
    }
    super.onPrepareOptionsMenu(menu)
  }
  override def onOptionsItemSelected(item: MenuItem) = {
    toast("Selected Item: " + item.getTitle)
    true
  }
  def onSort(item: MenuItem) {
    mSortMode = item.getItemId
    invalidateOptionsMenu()
  }
  def onQueryTextChange(newText: String) = {
    val newTextt = if (newText.isEmpty) "" else "Query so far: " + newText
    mSearchText.setText(newTextt)
    true
  }
  def onQueryTextSubmit(query: String) = {
    toast("Searching for: " + query + "...")
    true
  }
  private[app] var mSearchText: TextView = null
  private[app] var mSortMode: Int = -1
}