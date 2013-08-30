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
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import org.scaloid.common._
import android.view.Gravity

/**
 * Entry into our redirection example, describing what will happen.
 */
class RedirectEnter extends SActivity {
   onCreate {
     contentView = new SVerticalLayout{
       STextView(R.string.redirect_enter).padding(0,0,0,4 dip).<<(MATCH_PARENT, WRAP_CONTENT).>>.Weight(0)  //.setTextAppearance(context, android.R.attr.textAppearanceMedium)
       SButton(R.string.go, {
         // Here we start up the main entry point of our redirection
         // example.
         startActivity[RedirectMain]
       }).<<.wrap.>>.requestFocus();
     }.padding(4 dip).gravity(Gravity.CENTER_HORIZONTAL)
  }
}