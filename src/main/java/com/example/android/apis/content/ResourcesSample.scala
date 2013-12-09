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
package com.example.android.apis.content

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

import com.example.android.apis.R
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.widget.TextView

/**
 * Demonstration of loading resources.
 *
 * <p>
 * Each context has a resources object that you can access.  Additionally,
 * the Context class (an Activity is a Context) has a getString convenience
 * method getString() that looks up a string resource.
 *
 * @see StyledText for more depth about using styled text, both with getString()
 *      and in the layout xml files.
 */
class ResourcesSample extends Activity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.resources)
    var tv: TextView = null
    var cs: CharSequence = null
    var str: String = null
    cs = getText(R.string.styled_text)
    tv = findViewById(R.id.styled_text).asInstanceOf[TextView]
    tv.setText(cs)
    str = getString(R.string.styled_text)
    tv = findViewById(R.id.plain_text).asInstanceOf[TextView]
    tv.setText(str)
    val context: Context = this
    val res: Resources = context.getResources
    cs = res.getText(R.string.styled_text)
    tv = findViewById(R.id.res1).asInstanceOf[TextView]
    tv.setText(cs)
  }
}