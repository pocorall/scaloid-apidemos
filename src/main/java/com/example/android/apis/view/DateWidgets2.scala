package com.example.android.apis.view

import com.example.android.apis.R
import android.app.Activity
import android.widget.TextView
import android.widget.TimePicker
import android.os.Bundle
import DateWidgets2._
//remove if not needed
import scala.collection.JavaConversions._

object DateWidgets2 {

  private def pad(c: Int): String = {
    if (c >= 10) String.valueOf(c) else "0" + String.valueOf(c)
  }
}

class DateWidgets2 extends Activity {

  private var mTimeDisplay: TextView = _

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.date_widgets_example_2)
    val timePicker = findViewById(R.id.timePicker).asInstanceOf[TimePicker]
    timePicker.setCurrentHour(12)
    timePicker.setCurrentMinute(15)
    mTimeDisplay = findViewById(R.id.dateDisplay).asInstanceOf[TextView]
    updateDisplay(12, 15)
    timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

      def onTimeChanged(view: TimePicker, hourOfDay: Int, minute: Int) {
        updateDisplay(hourOfDay, minute)
      }
    })
  }

  private def updateDisplay(hourOfDay: Int, minute: Int) {
    mTimeDisplay.setText(new StringBuilder().append(pad(hourOfDay)).append(":")
      .append(pad(minute)))
  }
}
