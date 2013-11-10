package com.example.android.apis.app

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.util.Log
import android.widget.Toast
import java.util.ArrayList
import com.example.android.apis.R
import com.example.android.apis.app.RemoteService.Controller
import MessengerService._
import org.scaloid.common._

object MessengerService {
  // Scaloid >>
  val NOTIFICATION_SERVICE = android.content.Context.NOTIFICATION_SERVICE
  // Scaloid <<

  val MSG_REGISTER_CLIENT = 1

  val MSG_UNREGISTER_CLIENT = 2

  val MSG_SET_VALUE = 3
}

class MessengerService extends SService {

  var mNM: NotificationManager = _

  var mClients: ArrayList[Messenger] = new ArrayList[Messenger]()

  var mValue: Int = 0

  class IncomingHandler extends Handler {

    override def handleMessage(msg: Message) {
      msg.what match {
        case MSG_REGISTER_CLIENT => mClients.add(msg.replyTo)
        case MSG_UNREGISTER_CLIENT => mClients.remove(msg.replyTo)
        case MSG_SET_VALUE =>
          mValue = msg.arg1
          var i = mClients.size - 1
          while (i >= 0) {
            try {
              mClients.get(i).send(Message.obtain(null, MSG_SET_VALUE, mValue, 0))
            } catch {
              case e: RemoteException => mClients.remove(i)
            }
            i -= 1
          }

        case _ => super.handleMessage(msg)
      }
    }
  }

  val mMessenger = new Messenger(new IncomingHandler())

  override def onCreate() {
    mNM = getSystemService(NOTIFICATION_SERVICE).asInstanceOf[NotificationManager]
    showNotification()
  }

  override def onDestroy() {
    mNM.cancel(R.string.remote_service_started)
    Toast.makeText(this, R.string.remote_service_stopped, Toast.LENGTH_SHORT)
      .show()
  }

  override def onBind(intent: Intent): IBinder = mMessenger.getBinder

  private def showNotification() {
    val text = getText(R.string.remote_service_started)
    val notification = new Notification(R.drawable.stat_sample, text, System.currentTimeMillis())
    val contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, classOf[Controller]), 0)
    notification.setLatestEventInfo(this, getText(R.string.remote_service_label), text, contentIntent)
    mNM.notify(R.string.remote_service_started, notification)
  }
}
