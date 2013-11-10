package com.example.android.apis.app

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.view.KeyEvent
import android.os.Bundle
import android.util.Log
//remove if not needed
import scala.collection.JavaConversions._

class ContactsFilterInstrumentation extends Instrumentation {

  override def onCreate(arguments: Bundle) {
    super.onCreate(arguments)
    start()
  }

  override def onStart() {
    super.onStart()
    val intent = new Intent(Intent.ACTION_MAIN)
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.setClassName(getTargetContext, "com.android.phone.Dialer")
    val activity = startActivitySync(intent)
    Log.i("ContactsFilterInstrumentation", "Started: " + activity)
    sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_M))
    sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_M))
    sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_A))
    sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_A))
    waitForIdleSync()
    Log.i("ContactsFilterInstrumentation", "Done!")
    finish(Activity.RESULT_OK, null)
  }
}
