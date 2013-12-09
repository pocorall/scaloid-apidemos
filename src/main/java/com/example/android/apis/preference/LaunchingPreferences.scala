package com.example.android.apis.preference

import com.example.android.apis.R
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.LinearLayout.LayoutParams
import LaunchingPreferences._
//remove if not needed
import scala.collection.JavaConversions._

object LaunchingPreferences {

  private val REQUEST_CODE_PREFERENCES = 1
}

class LaunchingPreferences extends Activity with OnClickListener {
  // Scaloid >>
  val LayoutParams_MATCH_PARENT = android.view.ViewGroup.LayoutParams.MATCH_PARENT
  val LayoutParams_WRAP_CONTENT = android.view.ViewGroup.LayoutParams.WRAP_CONTENT
  // Scaloid <<

  private var mCounterText: TextView = _

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    PreferenceManager.setDefaultValues(this, R.xml.advanced_preferences, false)
    val layout = new LinearLayout(this)
    layout.setOrientation(LinearLayout.VERTICAL)
    setContentView(layout)
    val launchPreferences = new Button(this)
    launchPreferences.setText(getString(R.string.launch_preference_activity))
    launchPreferences.setOnClickListener(this)
    layout.addView(launchPreferences, new LayoutParams(LayoutParams_MATCH_PARENT, LayoutParams_WRAP_CONTENT))
    mCounterText = new TextView(this)
    layout.addView(mCounterText, new LayoutParams(LayoutParams_MATCH_PARENT, LayoutParams_WRAP_CONTENT))
    updateCounterText()
  }

  def onClick(v: View) {
    val launchPreferencesIntent = new Intent().setClass(this, classOf[AdvancedPreferences])
    startActivityForResult(launchPreferencesIntent, REQUEST_CODE_PREFERENCES)
  }

  protected override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == REQUEST_CODE_PREFERENCES) {
      updateCounterText()
    }
  }

  private def updateCounterText() {
    val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
    val counter = sharedPref.getInt(AdvancedPreferences.KEY_MY_PREFERENCE, 0)
    mCounterText.setText(getString(R.string.counter_value_is) + " " + counter)
  }
}
