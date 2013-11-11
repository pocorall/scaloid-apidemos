package com.example.android.apis.app

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.example.android.apis.R
import org.scaloid.common._


class LocalService extends SService {
  // Scaloid >>
  val NOTIFICATION_SERVICE = android.content.Context.NOTIFICATION_SERVICE
  val START_STICKY = android.app.Service.START_STICKY
  // Scaloid <<

  private var mNM: NotificationManager = _

  private var NOTIFICATION: Int = R.string.local_service_started

  class LocalBinder extends Binder {

    def getService(): LocalService = LocalService.this
  }

  override def onCreate() {
    mNM = getSystemService(NOTIFICATION_SERVICE).asInstanceOf[NotificationManager]
    showNotification()
  }

  override def onStartCommand(intent: Intent, flags: Int, startId: Int): Int = {
    Log.i("LocalService", "Received start id " + startId + ": " + intent)
    START_STICKY
  }

  override def onDestroy() {
    mNM.cancel(NOTIFICATION)
    Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT)
      .show()
  }

  override def onBind(intent: Intent): IBinder = mBinder

  private val mBinder = new LocalBinder()

  private def showNotification() {
    val text = getText(R.string.local_service_started)
    val notification = new Notification(R.drawable.stat_sample, text, System.currentTimeMillis())
    val contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, classOf[LocalServiceActivities.Controller]),
      0)
    notification.setLatestEventInfo(this, getText(R.string.local_service_label), text, contentIntent)
    mNM.notify(NOTIFICATION, notification)
  }
}
