/*
 * Copyright (C) 2008 The Android Open Source Project
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

import com.example.android.apis.R
import android.app.Activity
import android.app.AlertDialog
import android.app.SearchManager
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner

class SearchInvoke extends Activity {
  // Scaloid >>
  val DEFAULT_KEYS_SEARCH_LOCAL = android.app.Activity.DEFAULT_KEYS_SEARCH_LOCAL
  val DEFAULT_KEYS_DISABLE = android.app.Activity.DEFAULT_KEYS_DISABLE
  // Scaloid <<
  private[app] final val MENUMODE_SEARCH_KEY: Int = 0
  private[app] final val MENUMODE_MENU_ITEM: Int = 1
  private[app] final val MENUMODE_TYPE_TO_SEARCH: Int = 2
  private[app] final val MENUMODE_DISABLED: Int = 3

  private[app] var mStartSearch: Button = null
  private[app] var mMenuMode: Spinner = null
  private[app] var mQueryPrefill: EditText = null
  private[app] var mQueryAppData: EditText = null

  /**
   * Called with the activity is first created.
   *
   * We aren't doing anything special in this implementation, other than
   * the usual activity setup code.
   */
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.search_invoke)
    mStartSearch = findViewById(R.id.btn_start_search).asInstanceOf[Button]
    mMenuMode = findViewById(R.id.spinner_menu_mode).asInstanceOf[Spinner]
    mQueryPrefill = findViewById(R.id.txt_query_prefill).asInstanceOf[EditText]
    mQueryAppData = findViewById(R.id.txt_query_appdata).asInstanceOf[EditText]
    val adapter: ArrayAdapter[CharSequence] = ArrayAdapter.createFromResource(this, R.array.search_menuModes, android.R.layout.simple_spinner_item)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    mMenuMode.setAdapter(adapter)
    mMenuMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener {
      def onItemSelected(parent: AdapterView[_], view: View, position: Int, id: Long) {
        if (position == MENUMODE_TYPE_TO_SEARCH) {
          setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL)
        }
        else {
          setDefaultKeyMode(DEFAULT_KEYS_DISABLE)
        }
      }

      def onNothingSelected(parent: AdapterView[_]) {
        setDefaultKeyMode(DEFAULT_KEYS_DISABLE)
      }
    })
    mStartSearch.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        onSearchRequested
      }
    })
  }

  /**
   * Called when your activity's options menu needs to be updated.
   */
  override def onPrepareOptionsMenu(menu: Menu): Boolean = {
    super.onPrepareOptionsMenu(menu)
    var item: MenuItem = null
    menu.removeItem(0)
    menu.removeItem(1)
    mMenuMode.getSelectedItemPosition match {
      case MENUMODE_SEARCH_KEY =>
        item = menu.add(0, 0, 0, "(Search Key)")
      case MENUMODE_MENU_ITEM =>
        item = menu.add(0, 0, 0, "Search")
        item.setAlphabeticShortcut(SearchManager.MENU_KEY)
      case MENUMODE_TYPE_TO_SEARCH =>
        item = menu.add(0, 0, 0, "(Type-To-Search)")
      case MENUMODE_DISABLED =>
        item = menu.add(0, 0, 0, "(Disabled)")
    }
    item = menu.add(0, 1, 0, "Clear History")
    return true
  }

  /** Handle the menu item selections */
  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case 0 =>
        mMenuMode.getSelectedItemPosition match {
          case MENUMODE_SEARCH_KEY =>
            new AlertDialog.Builder(this).setMessage("To invoke search, dismiss this dialog and press the search key" + " (F5 on the simulator).").setPositiveButton("OK", null).show
          case MENUMODE_MENU_ITEM =>
            onSearchRequested
          case MENUMODE_TYPE_TO_SEARCH =>
            new AlertDialog.Builder(this).setMessage("To invoke search, dismiss this dialog and start typing.").setPositiveButton("OK", null).show
          case MENUMODE_DISABLED =>
            new AlertDialog.Builder(this).setMessage("You have disabled search.").setPositiveButton("OK", null).show
        }
      case 1 =>
        clearSearchHistory
    }
    return super.onOptionsItemSelected(item)
  }

  /**
   * This hook is called when the user signals the desire to start a search.
   *
   * By overriding this hook we can insert local or context-specific data.
   *
   * @return Returns true if search launched, false if activity blocks it
   */
  override def onSearchRequested: Boolean = {
    if (mMenuMode.getSelectedItemPosition == MENUMODE_DISABLED) {
      return false
    }
    val queryPrefill: String = mQueryPrefill.getText.toString
    var appDataBundle: Bundle = null
    val queryAppDataString: String = mQueryAppData.getText.toString
    if (queryAppDataString != null) {
      appDataBundle = new Bundle
      appDataBundle.putString("demo_key", queryAppDataString)
    }
    startSearch(queryPrefill, false, appDataBundle, false)
    return true
  }

  /**
   * Any application that implements search suggestions based on previous actions (such as
   * recent queries, page/items viewed, etc.) should provide a way for the user to clear the
   * history.  This gives the user a measure of privacy, if they do not wish for their recent
   * searches to be replayed by other users of the device (via suggestions).
   *
   * This example shows how to clear the search history for apps that use
   * android.provider.SearchRecentSuggestions.  If you have developed a custom suggestions
   * provider, you'll need to provide a similar API for clearing history.
   *
   * In this sample app we call this method from a "Clear History" menu item.  You could also
   * implement the UI in your preferences, or any other logical place in your UI.
   */
  private def clearSearchHistory {
    val suggestions: SearchRecentSuggestions = new SearchRecentSuggestions(this, SearchSuggestionSampleProvider.AUTHORITY, SearchSuggestionSampleProvider.MODE)
    suggestions.clearHistory
  }


}