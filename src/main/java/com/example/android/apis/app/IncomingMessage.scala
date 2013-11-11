package com.example.android.apis.app

import java.util.Random
import com.example.android.apis.R
import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import IncomingMessage._
import org.scaloid.common._
import android.view.Gravity
import android.view.ViewGroup.LayoutParams


object IncomingMessage {
  // Scaloid >>
  val NOTIFICATION_SERVICE = android.content.Context.NOTIFICATION_SERVICE
  // Scaloid <<

  def makeMessageIntentStack(context: Context, from: CharSequence, msg: CharSequence): Array[Intent] = {
    val intents = Array.ofDim[Intent](4)
    intents(0) = Intent.makeRestartActivityTask(new ComponentName(context, classOf[com.example.android.apis.ApiDemos]))
    intents(1) = new Intent(context, classOf[com.example.android.apis.ApiDemos])
    intents(1).putExtra("com.example.android.apis.Path", "App")
    intents(2) = new Intent(context, classOf[com.example.android.apis.ApiDemos])
    intents(2).putExtra("com.example.android.apis.Path", "App/Notification")
    intents(3) = new Intent(context, classOf[IncomingMessageView])
    intents(3).putExtra(IncomingMessageView.KEY_FROM, from)
    intents(3).putExtra(IncomingMessageView.KEY_MESSAGE, msg)
    intents
  }
}

class IncomingMessage extends SActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    contentView = new SVerticalLayout {
      STextView("Display a notification that will switch to the app in a new activity stack.")
      SButton("Show App Notification",showAppNotification())
      STextView("Display a notification that will go to a dedicated interstitial activity.")
      SButton("Show Interstitial Notification",showInterstitialNotification())
    }.padding(8 dip)
  }

  def showAppNotification() {
    val nm = getSystemService(NOTIFICATION_SERVICE).asInstanceOf[NotificationManager]
    val from = "Joe"
    var message: CharSequence = null
    (new Random().nextInt()) % 3 match {
      case 0 => message = "r u hungry?  i am starved"
      case 1 => message = "im nearby u"
      case _ => message = "kthx. meet u for dinner. cul8r"
    }
    val contentIntent = PendingIntent.getActivities(this, 0, makeMessageIntentStack(this, from, message),
      PendingIntent.FLAG_CANCEL_CURRENT)
    val tickerText = getString(R.string.imcoming_message_ticker_text, message)
    val notif = new Notification(R.drawable.stat_sample, tickerText, System.currentTimeMillis())
    notif.setLatestEventInfo(this, from, message, contentIntent)
    notif.defaults = Notification.DEFAULT_ALL
    nm.notify(R.string.imcoming_message_ticker_text, notif)
  }

  def showInterstitialNotification() {
    val nm = getSystemService(NOTIFICATION_SERVICE).asInstanceOf[NotificationManager]
    val from = "Dianne"
    var message: CharSequence = null
    (new Random().nextInt()) % 3 match {
      case 0 => message = "i am ready for some dinner"
      case 1 => message = "how about thai down the block?"
      case _ => message = "meet u soon. dont b late!"
    }
    val intent = new Intent(this, classOf[IncomingMessageInterstitial])
    intent.putExtra(IncomingMessageView.KEY_FROM, from)
    intent.putExtra(IncomingMessageView.KEY_MESSAGE, message)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
    val contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    val tickerText = getString(R.string.imcoming_message_ticker_text, message)
    val notif = new Notification(R.drawable.stat_sample, tickerText, System.currentTimeMillis())
    notif.setLatestEventInfo(this, from, message, contentIntent)
    notif.defaults = Notification.DEFAULT_ALL
    nm.notify(R.string.imcoming_message_ticker_text, notif)
  }
}
