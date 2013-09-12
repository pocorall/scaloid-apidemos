/*
 * Copyright (C) 2010 The Android Open Source Project
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
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import org.scaloid.common._
import android.view.Gravity

/**
 * Demonstrates how to show an AlertDialog that is managed by a Fragment.
 */
object FragmentAlertDialog {
  object MyAlertDialogFragment {
    def newInstance(title: Int) = {
      val frag = new FragmentAlertDialog.MyAlertDialogFragment
      val args = new Bundle
      args.putInt("title", title)
      frag.setArguments(args)
      frag
    }
  }
  class MyAlertDialogFragment extends DialogFragment {
    override def onCreateDialog(savedInstanceState: Bundle) = {
      val title = getArguments.getInt("title")
      new AlertDialog.Builder(getActivity).setIcon(R.drawable.alert_dialog_icon).setTitle(title).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener {
        def onClick(dialog: DialogInterface, whichButton: Int) {
          (getActivity.asInstanceOf[FragmentAlertDialog]).doPositiveClick
        }
      }).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener {
        def onClick(dialog: DialogInterface, whichButton: Int) {
          (getActivity.asInstanceOf[FragmentAlertDialog]).doNegativeClick
        }
      }).create
    }
  }
}

class FragmentAlertDialog extends SActivity {
  onCreate {
    contentView = new SVerticalLayout {
      STextView("Example of displaying an alert dialog with a DialogFragment").Weight(1).Gravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP).wrap.>>   //.setTextAppearance(android.R.attr.textAppearanceMedium)
      SButton(R.string.show, showDialog).<<.wrap.Weight(0)
    }.padding(4 dip).gravity(Gravity.CENTER_HORIZONTAL)
  }
  import FragmentAlertDialog._
  private[app] def showDialog {
    MyAlertDialogFragment.newInstance(R.string.alert_dialog_two_buttons_title).show(getFragmentManager, "dialog")
  }
  def doPositiveClick {
    Log.i("FragmentAlertDialog", "Positive click!")
  }
  def doNegativeClick {
    Log.i("FragmentAlertDialog", "Negative click!")
  }
}