package com.example.android.apis.app

import com.example.android.apis.R
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.ConditionVariable
import android.os.IBinder
import android.os.Parcel
import android.os.RemoteException
//remove if not needed
import scala.collection.JavaConversions._

class NotifyingService extends Service {
  // Scaloid >>
  val NOTIFICATION_SERVICE = android.content.Context.NOTIFICATION_SERVICE
  // Scaloid <<

  private var MOOD_NOTIFICATIONS: Int = R.layout.status_bar_notifications

  private var mNM: NotificationManager = _
  private var mCondition: ConditionVariable = _

  override def onCreate() {
    mNM = getSystemService(NOTIFICATION_SERVICE).asInstanceOf[NotificationManager]
    val notifyingThread = new Thread(null, mTask, "NotifyingService")
    mCondition = new ConditionVariable(false)
    notifyingThread.start()
  }

  override def onDestroy() {
    mNM.cancel(MOOD_NOTIFICATIONS)
    mCondition.open()
  }

  private var mTask: Runnable = new Runnable() {

    def run() {
      for (i <- 0 until 4) {
        showNotification(R.drawable.stat_happy, R.string.status_bar_notifications_happy_message)
        if (mCondition.block(5 * 1000)) {//break
          showNotification(R.drawable.stat_neutral, R.string.status_bar_notifications_ok_message)
          if (mCondition.block(5 * 1000)) {
            showNotification(R.drawable.stat_sad, R.string.status_bar_notifications_sad_message)
            if (mCondition.block(5 * 1000)) {                }
          }
        }
      }
      NotifyingService.this.stopSelf()
    }
  }

  override def onBind(intent: Intent): IBinder = mBinder

  private def showNotification(moodId: Int, textId: Int) {
    val text = getText(textId)
    val notification = new Notification(moodId, null, System.currentTimeMillis())
    val contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, classOf[NotifyingController]),
      0)
    notification.setLatestEventInfo(this, getText(R.string.status_bar_notifications_mood_title), text,
      contentIntent)
    mNM.notify(MOOD_NOTIFICATIONS, notification)
  }

  private val mBinder = new Binder() {

    protected override def onTransact(code: Int,
                                      data: Parcel,
                                      reply: Parcel,
                                      flags: Int): Boolean = {
      super.onTransact(code, data, reply, flags)
    }
  }


}
