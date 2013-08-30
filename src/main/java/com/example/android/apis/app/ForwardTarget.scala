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
import android.os.Bundle
import org.scaloid.common._
import android.view.Gravity

/**
 * Example of removing yourself from the history stack after forwarding to
 * another activity.
 */
class ForwardTarget extends SActivity {
  onCreate {
    contentView = new SVerticalLayout {
    val textView = STextView(R.string.forward_target).<<(MATCH_PARENT, WRAP_CONTENT).Weight(0).>>.setTextAppearance(context, android.R.attr.textAppearanceMedium)
    }.gravity(Gravity.CENTER_HORIZONTAL).padding(4 dip)
  }
}