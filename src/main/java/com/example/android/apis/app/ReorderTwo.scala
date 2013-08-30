/*
 * Copyright (C) 2009 The Android Open Source Project
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
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import org.scaloid.common._

class ReorderTwo extends SActivity {
  protected override def onCreate(savedState: Bundle) {
    super.onCreate(savedState)
    setContentView(R.layout.reorder_two)
    find[Button](R.id.reorder_launch_three).onClick {
      startActivity(new Intent(ReorderTwo.this, classOf[ReorderThree]))
    }
  }
}