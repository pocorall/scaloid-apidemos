package com.example.android.apis.preference

import com.example.android.apis.R
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceManager
import DefaultValues._
//remove if not needed
import scala.collection.JavaConversions._

object DefaultValues {
  // Scaloid >>
  val MODE_PRIVATE = android.content.Context.MODE_PRIVATE
  // Scaloid <<

  val PREFS_NAME = "defaults"

  def getPrefs(context: Context): SharedPreferences = {
    PreferenceManager.setDefaultValues(context, PREFS_NAME, MODE_PRIVATE, R.xml.default_values, false)
    context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
  }
}

class DefaultValues extends PreferenceActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    getPrefs(this)
    getPreferenceManager.setSharedPreferencesName(PREFS_NAME)
    addPreferencesFromResource(R.xml.default_values)
  }
}
