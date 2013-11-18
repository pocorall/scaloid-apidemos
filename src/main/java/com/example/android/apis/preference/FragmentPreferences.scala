package com.example.android.apis.preference

import com.example.android.apis.R
import android.app.Activity
import android.os.Bundle
import android.preference.PreferenceFragment
import FragmentPreferences._
//remove if not needed
import scala.collection.JavaConversions._

object FragmentPreferences {

  class PrefsFragment extends PreferenceFragment {

    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      addPreferencesFromResource(R.xml.preferences)
    }
  }
}

class FragmentPreferences extends Activity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    getFragmentManager.beginTransaction().replace(android.R.id.content, new PrefsFragment())
      .commit()
  }
}
