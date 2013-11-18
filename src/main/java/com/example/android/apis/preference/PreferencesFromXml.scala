package com.example.android.apis.preference

import com.example.android.apis.R
import android.os.Bundle
import android.preference.PreferenceActivity
//remove if not needed
import scala.collection.JavaConversions._

class PreferencesFromXml extends PreferenceActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    addPreferencesFromResource(R.xml.preferences)
  }
}
