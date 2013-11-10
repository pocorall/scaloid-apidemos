package com.example.android.apis.app

import com.example.android.apis.R
import android.app.Activity
import android.app.NotificationManager
import android.os.Bundle
import android.widget.TextView
import IncomingMessageView._
import org.scaloid.common._

object IncomingMessageView {

  val KEY_FROM = "from"

  val KEY_MESSAGE = "message"
}

class IncomingMessageView extends SActivity {
  // Scaloid >>
  val NOTIFICATION_SERVICE = android.content.Context.NOTIFICATION_SERVICE
  // Scaloid <<

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.incoming_message_view)
    findViewById(R.id.from).asInstanceOf[TextView].setText(getIntent.getCharSequenceExtra(KEY_FROM))
    findViewById(R.id.message).asInstanceOf[TextView].setText(getIntent.getCharSequenceExtra(KEY_MESSAGE))
    val nm = getSystemService(NOTIFICATION_SERVICE).asInstanceOf[NotificationManager]
    nm.cancel(R.string.imcoming_message_ticker_text)
  }
}
