/*
 * Copyright (C) 2007 The Android Open Source Project
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
package com.example.android.apis.view

import com.example.android.apis.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import java.util.Calendar
import org.scaloid.common._

/**
 * Basic example of using date and time widgets, including
 * {@link android.app.TimePickerDialog} and {@link android.widget.DatePicker}.
 *
 * Also provides a good example of using {@link Activity#onCreateDialog},
 * {@link Activity#onPrepareDialog} and {@link Activity#showDialog} to have the
 * activity automatically save and restore the state of the dialogs.
 */
object DateWidgets1 {
  private def pad(c: Int): String = (if (c >= 10) "" else "0") + String.valueOf(c)

  private[view] final val TIME_DIALOG_ID: Int = 0
  private[view] final val DATE_DIALOG_ID: Int = 1
}

import DateWidgets1._

class DateWidgets1 extends SActivity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.date_widgets_example_1)
    mDateDisplay = find[TextView](R.id.dateDisplay)
    find[Button](R.id.pickDate).onClick(showDialog(DATE_DIALOG_ID))
    find[Button](R.id.pickTime).onClick(showDialog(TIME_DIALOG_ID))
    val c = Calendar.getInstance
    mYear = c.get(Calendar.YEAR)
    mMonth = c.get(Calendar.MONTH)
    mDay = c.get(Calendar.DAY_OF_MONTH)
    mHour = c.get(Calendar.HOUR_OF_DAY)
    mMinute = c.get(Calendar.MINUTE)
    updateDisplay()
  }

  protected override def onCreateDialog(id: Int): Dialog = id match {
    case TIME_DIALOG_ID => new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false)
    case DATE_DIALOG_ID => new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay)
    case _ => null
  }

  protected override def onPrepareDialog(id: Int, dialog: Dialog) {
    id match {
      case TIME_DIALOG_ID => (dialog.asInstanceOf[TimePickerDialog]).updateTime(mHour, mMinute)
      case DATE_DIALOG_ID => (dialog.asInstanceOf[DatePickerDialog]).updateDate(mYear, mMonth, mDay)
    }
  }

  private def updateDisplay() {
    mDateDisplay.setText("" + (mMonth + 1) + "-" + mDay + "-" + mYear + " " + pad(mHour) + ":" + pad(mMinute))
  }

  private var mDateDisplay: TextView = null
  private var mYear: Int = 0
  private var mMonth: Int = 0
  private var mDay: Int = 0
  private var mHour: Int = 0
  private var mMinute: Int = 0
  private val mDateSetListener = new DatePickerDialog.OnDateSetListener {
    def onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
      mYear = year
      mMonth = monthOfYear
      mDay = dayOfMonth
      updateDisplay()
    }
  }

  private val mTimeSetListener = new TimePickerDialog.OnTimeSetListener {
    def onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
      mHour = hourOfDay
      mMinute = minute
      updateDisplay()
    }
  }
}