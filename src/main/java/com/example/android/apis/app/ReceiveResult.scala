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
import android.text.Editable
import android.os.Bundle
import android.view.{Gravity, View}
import android.widget.Button
import android.widget.TextView
import org.scaloid.common._

/**
 * Shows how an activity can send data to its launching activity when done.y.
 * <p>This can be used, for example, to implement a dialog alowing the user to
 pick an e-mail address or image -- the picking activity sends the selected
 data back to the originating activity when done.</p>

 <p>The example here is composed of two activities: ReceiveResult launches
 the picking activity and receives its results; SendResult allows the user
 to pick something and sends the selection back to its caller.  Implementing
 this functionality involves the
 {@link android.app.Activity#setResult setResult()} method for sending a
 result and
 {@link android.app.Activity#onActivityResult onActivityResult()} to
 receive it.</p>

 <h4>Demo</h4>
 App/Activity/Receive Result

 <h4>Source files</h4>
 <table class="LinkTable">
 <tr>
 <td class="LinkColumn">src/com.example.android.apis/app/ReceiveResult.java</td>
 <td class="DescrColumn">Launches pick activity and receives its result</td>
 </tr>
 <tr>
 <td class="LinkColumn">src/com.example.android.apis/app/SendResult.java</td>
 <td class="DescrColumn">Allows user to pick an option and sends it back to its caller</td>
 </tr>
 <tr>
 <td class="LinkColumn">/res/any/layout/receive_result.xml</td>
 <td class="DescrColumn">Defines contents of the ReceiveResult screen</td>
 </tr>
 <tr>
 <td class="LinkColumn">/res/any/layout/send_result.xml</td>
 <td class="DescrColumn">Defines contents of the SendResult screen</td>
 </tr>
 </table>
 */
object ReceiveResult {
  private final val GET_CODE: Int = 0
}
class ReceiveResult extends SActivity {
  /**
   * Initialization of the Activity after it is first created.  Must at least
   * call {@link android.app.Activity#setContentView setContentView()} to
   * describe what is to be displayed in the screen.
   */
  onCreate {
    contentView = new SVerticalLayout {
      STextView(R.string.receive_result_instructions).<<(MATCH_PARENT, WRAP_CONTENT).>>.padding(0,0,0,4 dip).Weight(0)   //.setTextAppearance(context, android.R.attr.textAppearanceMedium)
      mResults = STextView().backgroundDrawable(R.drawable.green).padding(0,0,0,4 dip).<<(MATCH_PARENT, 10 dip).Weight(1).>>
      SButton(R.string.receive_result_result, {
        // Start the activity whose result we want to retrieve.  The
        // result will come back with request code GET_CODE.
        val intent = new Intent(ReceiveResult.this, classOf[SendResult])
        startActivityForResult(intent, GET_CODE)
      }).<<.wrap.>>.Weight(0)
    }.padding(4 dip).gravity(Gravity.CENTER_HORIZONTAL)
    mResults.setText(mResults.text, TextView.BufferType.EDITABLE)
  }
  /**
   * This method is called when the sending activity has finished, with the
   * result it supplied.
   * @param requestCode The original request code as given to
   *                    startActivity().
   * @param resultCode From sending activity as per setResult().
   * @param data From sending activity as per setResult().
   */
  protected override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    // You can use the requestCode to select between multiple child
    // activities you may have started.  Here there is only one thing
    // we launch.
    if (requestCode == GET_CODE) {
      // We will be adding to our text.
      val text = mResults.text.asInstanceOf[Editable]
      // This is a standard resultCode that is sent back if the
      // activity doesn't supply an explicit result.  It will also
      // be returned if the activity failed to launch.
      //constant value of RESULT_CANCELED : 0
      val RESULT_CANCELED = 0
      if (resultCode == RESULT_CANCELED) {
        text.append("(cancelled)")
        // Our protocol with the sending activity is that it will send
        // text in 'data' as its result.
      } else {
        text.append("(okay ")
        text.append(Integer.toString(resultCode))
        text.append(") ")
        if (data != null) {
          text.append(data.getAction)
        }
      }
      text.append("\n")
    }
  }
  // Definition of the one requestCode we use for receiving resuls.
  val GET_CODE = 0;
  private var mResults: STextView = null
}