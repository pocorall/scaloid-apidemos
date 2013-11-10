package com.example.android.apis.app

import com.example.android.apis.R
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.Parcel
import android.os.RemoteException
import android.widget.Toast
import org.scaloid.common._

//remove if not needed
import scala.collection.JavaConversions._

class AlarmService_Service extends Service {
  // Scaloid >>
  val NOTIFICATION_SERVICE = android.content.Context.NOTIFICATION_SERVICE
  // Scaloid <<

  var mNM: NotificationManager = _

  override def onCreate() {
    mNM = getSystemService(NOTIFICATION_SERVICE).asInstanceOf[NotificationManager]
    showNotification()
    val thr = new Thread(null, mTask, "AlarmService_Service")
    thr.start()
  }

  override def onDestroy() {
    mNM.cancel(R.string.alarm_service_started)
    Toast.makeText(this, R.string.alarm_service_finished, Toast.LENGTH_SHORT)
      .show()
  }

  var mTask: Runnable = new Runnable() {

    def run() {
      val endTime = System.currentTimeMillis() + 15 * 1000
      while (System.currentTimeMillis() < endTime) {
        synchronized {
          try {
            mBinder.wait(endTime - System.currentTimeMillis())
          } catch {
            case e: Exception =>
          }
        }
      }
      AlarmService_Service.this.stopSelf()
    }
  }

  override def onBind(intent: Intent): IBinder = mBinder

  private def showNotification() {
    val text = getText(R.string.alarm_service_started)
    val notification = new Notification(R.drawable.stat_sample, text, System.currentTimeMillis())
    val contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, classOf[AlarmService]), 0)
    notification.setLatestEventInfo(this, getText(R.string.alarm_service_label), text, contentIntent)
    mNM.notify(R.string.alarm_service_started, notification)
  }

  private val mBinder:IBinder = new Binder() {

    protected override def onTransact(code: Int,
                                      data: Parcel,
                                      reply: Parcel,
                                      flags: Int): Boolean = {
      super.onTransact(code, data, reply, flags)
    }
  }
}
