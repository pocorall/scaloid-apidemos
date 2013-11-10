package com.example.android.apis.app

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.content.IntentFilter
import android.view.KeyEvent
import android.provider.ContactsContract
import android.os.Bundle
import android.util.Log
//remove if not needed
import scala.collection.JavaConversions._

class ContactsSelectInstrumentation extends Instrumentation {

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
    Log.i("ContactsSelectInstrumentation", "Started: " + activity)
    val am = addMonitor(IntentFilter.create(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_ITEM_TYPE),
      null, true)
    sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN))
    sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_DOWN))
    sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_CENTER))
    sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_CENTER))
    if (checkMonitorHit(am, 1)) {
      Log.i("ContactsSelectInstrumentation", "Activity started!")
    } else {
      Log.i("ContactsSelectInstrumentation", "*** ACTIVITY NOT STARTED!")
    }
    Log.i("ContactsSelectInstrumentation", "Done!")
    finish(Activity.RESULT_OK, null)
  }
}
