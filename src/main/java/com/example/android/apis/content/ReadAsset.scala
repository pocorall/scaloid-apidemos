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
import android.os.Bundle
import android.widget.TextView
import java.io.IOException
import java.io.InputStream

/**
 * Demonstration of styled text resources.
 */
class ReadAsset extends Activity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.read_asset)
    try {
      val is: InputStream = getAssets.open("read_asset.txt")
      val size: Int = is.available
      val buffer: Array[Byte] = new Array[Byte](size)
      is.read(buffer)
      is.close
      val text: String = new String(buffer)
      val tv: TextView = findViewById(R.id.text).asInstanceOf[TextView]
      tv.setText(text)
    }
    catch {
      case e: IOException => {
        throw new RuntimeException(e)
      }
    }
  }
}