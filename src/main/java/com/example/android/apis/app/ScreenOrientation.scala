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
import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.AdapterView.OnItemSelectedListener


class ScreenOrientation extends Activity {
  private[app] final val mOrientationValues: Array[Int] = Array[Int](ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, ActivityInfo.SCREEN_ORIENTATION_USER, ActivityInfo.SCREEN_ORIENTATION_BEHIND, ActivityInfo.SCREEN_ORIENTATION_SENSOR, ActivityInfo.SCREEN_ORIENTATION_NOSENSOR, ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE, ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT, ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE, ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT, ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR)
  private[app] var mOrientation: Spinner = null

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.screen_orientation)
    mOrientation = findViewById(R.id.orientation).asInstanceOf[Spinner]
    val adapter: ArrayAdapter[CharSequence] = ArrayAdapter.createFromResource(this, R.array.screen_orientations, android.R.layout.simple_spinner_item)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    mOrientation.setAdapter(adapter)
    mOrientation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener {
      def onItemSelected(parent: AdapterView[_], view: View, position: Int, id: Long) {
        setRequestedOrientation(mOrientationValues(position))
      }

      def onNothingSelected(parent: AdapterView[_]) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
      }
    })
  }


}