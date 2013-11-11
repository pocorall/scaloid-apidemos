package com.example.android.apis.app

import com.example.android.apis.R
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
//remove if not needed
import scala.collection.JavaConversions._

class AppUpdateReceiver extends BroadcastReceiver {

  override def onReceive(context: Context, intent: Intent) {
    Toast.makeText(context, R.string.app_update_received, Toast.LENGTH_SHORT)
      .show()
  }
}
