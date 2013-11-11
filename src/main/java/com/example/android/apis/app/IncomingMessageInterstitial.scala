package com.example.android.apis.app

import com.example.android.apis.R
import android.app.Activity
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import org.scaloid.common._
import android.view.Gravity
import android.view.ViewGroup.LayoutParams


class IncomingMessageInterstitial extends SActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    contentView = new SVerticalLayout {
      STextView("This would be where you would see a summary of the information related to the notification.  Instead, we'll just give you a button.")
      SButton("Switch To App",switchToApp())
    }

  }

  def switchToApp() {
    val from = getIntent.getCharSequenceExtra(IncomingMessageView.KEY_FROM)
    val msg = getIntent.getCharSequenceExtra(IncomingMessageView.KEY_MESSAGE)
    val stack = IncomingMessage.makeMessageIntentStack(this, from, msg)
    startActivities(stack)
    finish()
  }
}
