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
package com.example.android.apis.text

import android.widget.TextView
import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.text.method.MovementMethod
import android.text.Editable
import android.util.AttributeSet
import android.widget.TextView.BufferType

/**
 * This is a TextView that is Editable and by default scrollable,
 * like EditText without a cursor.
 *
 * <p>
 * <b>XML attributes</b>
 * <p>
 * See
 * {@link android.R.styleable#TextView TextView Attributes},
 * {@link android.R.styleable#View View Attributes}
 */
class LogTextBox(context: Context, attrs: AttributeSet, defStyle: Int) extends TextView(context, attrs, defStyle) {
  protected override def getDefaultMovementMethod: MovementMethod = {
    return ScrollingMovementMethod.getInstance
  }

  override def getText: Editable = {
    return super.getText.asInstanceOf[Editable]
  }

  override def setText(text: CharSequence, `type`: TextView.BufferType) {
    super.setText(text, BufferType.EDITABLE)
  }
}