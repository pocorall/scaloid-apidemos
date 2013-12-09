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
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.widget.TextView

class SearchQueryResults extends Activity {
  private[app] var mQueryText: TextView = null
  private[app] var mAppDataText: TextView = null
  private[app] var mDeliveredByText: TextView = null

  /** Called with the activity is first created.
    *
    * After the typical activity setup code, we check to see if we were launched
    * with the ACTION_SEARCH intent, and if so, we handle it.
    */
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.search_query_results)
    mQueryText = findViewById(R.id.txt_query).asInstanceOf[TextView]
    mAppDataText = findViewById(R.id.txt_appdata).asInstanceOf[TextView]
    mDeliveredByText = findViewById(R.id.txt_deliveredby).asInstanceOf[TextView]
    val queryIntent: Intent = getIntent
    val queryAction: String = queryIntent.getAction
    if (Intent.ACTION_SEARCH == queryAction) {
      doSearchQuery(queryIntent, "onCreate()")
    }
    else {
      mDeliveredByText.setText("onCreate(), but no ACTION_SEARCH intent")
    }
  }

  /**
   * Called when new intent is delivered.
   *
   * This is where we check the incoming intent for a query string.
   *
   * @param newIntent The intent used to restart this activity
   */
  override def onNewIntent(newIntent: Intent) {
    super.onNewIntent(newIntent)
    val queryIntent: Intent = getIntent
    val queryAction: String = queryIntent.getAction
    if (Intent.ACTION_SEARCH == queryAction) {
      doSearchQuery(queryIntent, "onNewIntent()")
    }
    else {
      mDeliveredByText.setText("onNewIntent(), but no ACTION_SEARCH intent")
    }
  }

  /**
   * Generic search handler.
   *
   * In a "real" application, you would use the query string to select results from
   * your data source, and present a list of those results to the user.
   */
  private def doSearchQuery(queryIntent: Intent, entryPoint: String) {
    val queryString: String = queryIntent.getStringExtra(SearchManager.QUERY)
    mQueryText.setText(queryString)
    val suggestions: SearchRecentSuggestions = new SearchRecentSuggestions(this, SearchSuggestionSampleProvider.AUTHORITY, SearchSuggestionSampleProvider.MODE)
    suggestions.saveRecentQuery(queryString, null)
    val appData: Bundle = queryIntent.getBundleExtra(SearchManager.APP_DATA)
    if (appData == null) {
      mAppDataText.setText("<no app data bundle>")
    }
    if (appData != null) {
      val testStr: String = appData.getString("demo_key")
      mAppDataText.setText(if ((testStr == null)) "<no app data>" else testStr)
    }
    mDeliveredByText.setText(entryPoint)
  }

}