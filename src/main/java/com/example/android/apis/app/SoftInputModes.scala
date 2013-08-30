package com.example.android.apis.app

import com.example.android.apis.R
import android.os.Bundle
import android.view.{Gravity, View, WindowManager}
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import org.scaloid.common._
/**
 * Demonstrates how the various soft input modes impact window resizing.
 */
class SoftInputModes extends SActivity {
  onCreate {
    // See assets/res/any/layout/save_restore_state.xml for this
    // view layout definition, which is being set here as
    // the content of our screen.
    contentView = new SVerticalLayout {
      STextView(R.string.soft_input_modes_summary).padding(0,0,0,4 dip).<<(MATCH_PARENT, WRAP_CONTENT).Weight(0).>>.setTextAppearance(context, android.R.attr.textAppearanceMedium)
      this += new SLinearLayout {
        STextView(R.string.soft_input_modes_label).<<.wrap.>>.setTextAppearance(context, android.R.attr.textAppearanceMedium)
        mResizeMode = SSpinner().<<.wrap.>>
      }.<<(MATCH_PARENT, WRAP_CONTENT).>>.gravity(Gravity.CENTER)
      STextView(R.string.soft_input_modes_summary).backgroundDrawable(R.drawable.red).padding(0,0,0,6 dip).<<(MATCH_PARENT, WRAP_CONTENT).Weight(1).>>.setTextAppearance(context, android.R.attr.textAppearanceMedium)
      SEditText(R.string.soft_input_modes_initial_text).freezesText(true).backgroundDrawable(R.drawable.green).<<(MATCH_PARENT, WRAP_CONTENT).Weight(0).>>.requestFocus() //.setTextAppearance(context, android.R.attr.textAppearanceMedium)
    }
    val adapter = new ArrayAdapter[CharSequence](this, android.R.layout.simple_spinner_item, mResizeModeLabels)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    mResizeMode.setAdapter(adapter)
    mResizeMode.selection = 0
    mResizeMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener {
      def onItemSelected(parent: AdapterView[_], view: View, position: Int, id: Long) = getWindow.setSoftInputMode(mResizeModeValues(position))
      def onNothingSelected(parent: AdapterView[_]) =  getWindow.setSoftInputMode(mResizeModeValues(0))
    })
  }
  private[app] var mResizeMode: SSpinner = null
  private[app] final val mResizeModeLabels = Array[CharSequence]("Unspecified", "Resize", "Pan", "Nothing")
  private[app] final val mResizeModeValues = Array[Int](WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
}