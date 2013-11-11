package com.example.android.apis.app

import com.example.android.apis.R
import android.app.Activity
import android.widget.Button
import android.os.Bundle
import android.view.View
import android.widget.Toast
import org.scaloid.common._
import android.view.ViewGroup.LayoutParams

class NotifyWithText extends SActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    contentView = new SVerticalLayout {
      SButton(R.string.notify_with_text_short_notify_text,toast(R.string.short_notification_text))
      SButton(R.string.notify_with_text_long_notify_text,longToast(R.string.long_notification_text))
    }

  }
}
