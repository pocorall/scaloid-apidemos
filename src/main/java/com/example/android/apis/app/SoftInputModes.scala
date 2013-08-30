package com.example.android.apis.app

import com.example.android.apis.R
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import org.scaloid.common._
/**
 * Demonstrates how the various soft input modes impact window resizing.
 */
class SoftInputModes extends SActivity {
  /**
   * Initialization of the Activity after it is first created.  Here we use
   * {@link android.app.Activity#setContentView setContentView()} to set up
   * the Activity's content, and retrieve the EditText widget whose state we
   * will persistent.
   */
  protected override def onCreate(savedInstanceState: Bundle) {
    // Be sure to call the super class.
    super.onCreate(savedInstanceState)
    // See assets/res/any/layout/save_restore_state.xml for this
    // view layout definition, which is being set here as
    // the content of our screen.
    setContentView(R.layout.soft_input_modes)
    mResizeMode = find[Spinner](R.id.resize_mode)
    val adapter = new ArrayAdapter[CharSequence](this, android.R.layout.simple_spinner_item, mResizeModeLabels)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    mResizeMode.setAdapter(adapter)
    mResizeMode.setSelection(0)
    mResizeMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener {
      def onItemSelected(parent: AdapterView[_], view: View, position: Int, id: Long) {
        getWindow.setSoftInputMode(mResizeModeValues(position))
      }
      def onNothingSelected(parent: AdapterView[_]) {
        getWindow.setSoftInputMode(mResizeModeValues(0))
      }
    })
  }
  private[app] var mResizeMode: Spinner = null
  private[app] final val mResizeModeLabels = Array[CharSequence]("Unspecified", "Resize", "Pan", "Nothing")
  private[app] final val mResizeModeValues = Array[Int](WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
}