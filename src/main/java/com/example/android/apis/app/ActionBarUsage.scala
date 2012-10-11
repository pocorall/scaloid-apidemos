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

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.TextView
import android.widget.Toast
import com.example.android.apis.R

/**
 * This demonstrates idiomatic usage of the Action Bar. The default Honeycomb theme
 * includes the action bar by default and a menu resource is used to populate the
 * menu data itself. If you'd like to see how these things work under the hood, see
 * ActionBarMechanics.
 */
class ActionBarUsage extends Activity with OnQueryTextListener {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    mSearchText = new TextView(this)
    setContentView(mSearchText)
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.actions, menu)
    val searchView = menu.findItem(R.id.action_search).getActionView.asInstanceOf[SearchView]
    searchView.setOnQueryTextListener(this)
    true
  }

  override def onPrepareOptionsMenu(menu: Menu): Boolean = {
    if (mSortMode != -1) {
      val icon: Drawable = menu.findItem(mSortMode).getIcon
      menu.findItem(R.id.action_sort).setIcon(icon)
    }
    super.onPrepareOptionsMenu(menu)
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    Toast.makeText(this, "Selected Item: " + item.getTitle, Toast.LENGTH_SHORT).show()
    true
  }

  def onSort(item: MenuItem) {
    mSortMode = item.getItemId
    invalidateOptionsMenu()
  }

  def onQueryTextChange(newText: String): Boolean = {
    val newTextt = if (newText.isEmpty) "" else "Query so far: " + newText
    mSearchText.setText(newTextt)
    true
  }

  def onQueryTextSubmit(query: String): Boolean = {
    Toast.makeText(this, "Searching for: " + query + "...", Toast.LENGTH_SHORT).show()
    true
  }

  private[app] var mSearchText: TextView = null
  private[app] var mSortMode: Int = -1
}