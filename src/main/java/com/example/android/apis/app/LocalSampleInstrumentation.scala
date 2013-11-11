package com.example.android.apis.app

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.view.KeyEvent
import android.os.Bundle
import android.util.Log
import LocalSampleInstrumentation._

object LocalSampleInstrumentation {

  abstract class ActivityRunnable(val activity: Activity) extends Runnable
}

class LocalSampleInstrumentation extends Instrumentation {

  override def onCreate(arguments: Bundle) {
    super.onCreate(arguments)
    start()
  }

  override def onStart() {
    super.onStart()
    val intent = new Intent(Intent.ACTION_MAIN)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.setClass(getTargetContext, classOf[SaveRestoreState])
    val activity = startActivitySync(intent).asInstanceOf[SaveRestoreState]
    Log.i("LocalSampleInstrumentation", "Initial text: " + activity.getSavedText)
    runOnMainSync(new ActivityRunnable(activity) {

      def run() {
        activity.asInstanceOf[SaveRestoreState].setSavedText("")
      }
    })
    sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SHIFT_LEFT))
    sendCharacterSync(KeyEvent.KEYCODE_H)
    sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_SHIFT_LEFT))
    sendCharacterSync(KeyEvent.KEYCODE_E)
    sendCharacterSync(KeyEvent.KEYCODE_L)
    sendCharacterSync(KeyEvent.KEYCODE_L)
    sendCharacterSync(KeyEvent.KEYCODE_O)
    waitForIdleSync()
    Log.i("LocalSampleInstrumentation", "Final text: " + activity.getSavedText)
    Log.i("ContactsFilterInstrumentation", "Done!")
    finish(Activity.RESULT_OK, null)
  }
}
