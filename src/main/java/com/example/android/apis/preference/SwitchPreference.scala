package com.example.android.apis.preference

import com.example.android.apis.R
import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceManager
//remove if not needed
import scala.collection.JavaConversions._

class SwitchPreference extends PreferenceActivity {
  // Scaloid >>
  val MODE_PRIVATE = android.content.Context.MODE_PRIVATE
  // Scaloid <<

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    PreferenceManager.setDefaultValues(this, "switch", MODE_PRIVATE, R.xml.default_values, false)
    getPreferenceManager.setSharedPreferencesName("switch")
    addPreferencesFromResource(R.xml.preference_switch)
  }
}
