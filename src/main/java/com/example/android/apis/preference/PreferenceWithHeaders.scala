package com.example.android.apis.preference

import com.example.android.apis.R
import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import java.util.List
import PreferenceWithHeaders._
import android.preference.PreferenceActivity.Header

//remove if not needed
import scala.collection.JavaConversions._

object PreferenceWithHeaders {

  class Prefs1Fragment extends PreferenceFragment {

    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      PreferenceManager.setDefaultValues(getActivity, R.xml.advanced_preferences, false)
      addPreferencesFromResource(R.xml.fragmented_preferences)
    }
  }

  class Prefs1FragmentInner extends PreferenceFragment {

    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      Log.i("args", "Arguments: " + getArguments)
      addPreferencesFromResource(R.xml.fragmented_preferences_inner)
    }
  }

  class Prefs2Fragment extends PreferenceFragment {

    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      Log.i("args", "Arguments: " + getArguments)
      addPreferencesFromResource(R.xml.preference_dependencies)
    }
  }
}

class PreferenceWithHeaders extends PreferenceActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    if (hasHeaders()) {
      val button = new Button(this)
      button.setText("Some action")
      setListFooter(button)
    }
  }

  override def onBuildHeaders(target: List[Header]) {
    loadHeadersFromResource(R.xml.preference_headers, target)
  }
}
