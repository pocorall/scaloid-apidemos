package com.example.android.apis.app

import android.content.SearchRecentSuggestionsProvider
import SearchSuggestionSampleProvider._
//remove if not needed
import scala.collection.JavaConversions._

object SearchSuggestionSampleProvider {
  // Scaloid >>
  val DATABASE_MODE_QUERIES = android.content.SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES
  // Scaloid <<

  val AUTHORITY = "com.example.android.apis.SuggestionProvider"

  val MODE = DATABASE_MODE_QUERIES
}

class SearchSuggestionSampleProvider extends SearchRecentSuggestionsProvider() {

  setupSuggestions(AUTHORITY, MODE)
}
