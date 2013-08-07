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
package com.example.android.apis.app

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

import com.example.android.apis.R
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.SystemClock
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import org.scaloid.common._

/**
 * This demonstrates how you can schedule an alarm that causes a service to
 * be started.  This is useful when you want to schedule alarms that initiate
 * long-running operations, such as retrieving recent e-mails.
 */
class AlarmService extends SActivity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    mAlarmSender = PendingIntent.getService(AlarmService.this, 0, new Intent(AlarmService.this, classOf[AlarmService_Service]), 0)
    setContentView(R.layout.alarm_service)
//    val center_horizontal = 1
//    contentView = new SVerticalLayout {
//         STextView(R.string.alarm_service).<<(MATCH_PARENT, WRAP_CONTENT).marginBottom(4 dip).Weight(0.0f)
//
//    }.gravity(center_horizontal).padding(4 dip) //.<<.fill

    find[Button](R.id.start_alarm).onClick {
      val firstTime: Long = SystemClock.elapsedRealtime
      val am = getSystemService(ALARM_SERVICE).asInstanceOf[AlarmManager]
      am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 30 * 1000, mAlarmSender)
      Toast.makeText(AlarmService.this, R.string.repeating_scheduled, Toast.LENGTH_LONG).show
    }
    find[Button](R.id.stop_alarm).onClick {
      val am = getSystemService(ALARM_SERVICE).asInstanceOf[AlarmManager]
      am.cancel(mAlarmSender)
      Toast.makeText(AlarmService.this, R.string.repeating_unscheduled, Toast.LENGTH_LONG).show
    }
  }
  val ALARM_SERVICE = "alarm"
  private var mAlarmSender: PendingIntent = null
}