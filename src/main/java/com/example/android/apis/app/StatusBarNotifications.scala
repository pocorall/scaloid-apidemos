/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.apis.app

import com.example.android.apis.{ApiDemos, R}
import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RemoteViews

/**
 * Demonstrates adding notifications to the status bar
 */
class StatusBarNotifications extends Activity {
  // Scaloid >>
  val NOTIFICATION_SERVICE = android.content.Context.NOTIFICATION_SERVICE
  // Scaloid <<

  private var MOOD_NOTIFICATIONS: Int = R.layout.status_bar_notifications

  private var mNotificationManager: NotificationManager = null

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.status_bar_notifications)
    var button: Button = null
    mNotificationManager = getSystemService(NOTIFICATION_SERVICE).asInstanceOf[NotificationManager]
    button = findViewById(R.id.happy).asInstanceOf[Button]
    button.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        setMood(R.drawable.stat_happy, R.string.status_bar_notifications_happy_message, false)
      }
    })
    button = findViewById(R.id.neutral).asInstanceOf[Button]
    button.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        setMood(R.drawable.stat_neutral, R.string.status_bar_notifications_ok_message, false)
      }
    })
    button = findViewById(R.id.sad).asInstanceOf[Button]
    button.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        setMood(R.drawable.stat_sad, R.string.status_bar_notifications_sad_message, false)
      }
    })
    button = findViewById(R.id.happyMarquee).asInstanceOf[Button]
    button.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        setMood(R.drawable.stat_happy, R.string.status_bar_notifications_happy_message, true)
      }
    })
    button = findViewById(R.id.neutralMarquee).asInstanceOf[Button]
    button.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        setMood(R.drawable.stat_neutral, R.string.status_bar_notifications_ok_message, true)
      }
    })
    button = findViewById(R.id.sadMarquee).asInstanceOf[Button]
    button.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        setMood(R.drawable.stat_sad, R.string.status_bar_notifications_sad_message, true)
      }
    })
    button = findViewById(R.id.happyViews).asInstanceOf[Button]
    button.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        setMoodView(R.drawable.stat_happy, R.string.status_bar_notifications_happy_message)
      }
    })
    button = findViewById(R.id.neutralViews).asInstanceOf[Button]
    button.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        setMoodView(R.drawable.stat_neutral, R.string.status_bar_notifications_ok_message)
      }
    })
    button = findViewById(R.id.sadViews).asInstanceOf[Button]
    button.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        setMoodView(R.drawable.stat_sad, R.string.status_bar_notifications_sad_message)
      }
    })
    button = findViewById(R.id.defaultSound).asInstanceOf[Button]
    button.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        setDefault(Notification.DEFAULT_SOUND)
      }
    })
    button = findViewById(R.id.defaultVibrate).asInstanceOf[Button]
    button.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        setDefault(Notification.DEFAULT_VIBRATE)
      }
    })
    button = findViewById(R.id.defaultAll).asInstanceOf[Button]
    button.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        setDefault(Notification.DEFAULT_ALL)
      }
    })
    button = findViewById(R.id.clear).asInstanceOf[Button]
    button.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        mNotificationManager.cancel(R.layout.status_bar_notifications)
      }
    })
  }

  private def makeMoodIntent(moodId: Int): PendingIntent = {
    val contentIntent: PendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, classOf[NotificationDisplay]).putExtra("moodimg", moodId), PendingIntent.FLAG_UPDATE_CURRENT)
    return contentIntent
  }

  private def makeDefaultIntent: PendingIntent = {
    val intents: Array[Intent] = new Array[Intent](4)
    intents(0) = Intent.makeRestartActivityTask(new ComponentName(this, classOf[ApiDemos]))
    intents(1) = new Intent(this, classOf[ApiDemos])
    intents(1).putExtra("com.example.android.apis.Path", "App")
    intents(2) = new Intent(this, classOf[ApiDemos])
    intents(2).putExtra("com.example.android.apis.Path", "App/Notification")
    intents(3) = new Intent(this, classOf[StatusBarNotifications])
    val contentIntent: PendingIntent = PendingIntent.getActivities(this, 0, intents, PendingIntent.FLAG_UPDATE_CURRENT)
    return contentIntent
  }

  private def setMood(moodId: Int, textId: Int, showTicker: Boolean) {
    val text: CharSequence = getText(textId)
    val tickerText: String = if (showTicker) getString(textId) else null
    val notification: Notification = new Notification(moodId, tickerText, System.currentTimeMillis)
    notification.setLatestEventInfo(this, getText(R.string.status_bar_notifications_mood_title), text, makeMoodIntent(moodId))
    mNotificationManager.notify(MOOD_NOTIFICATIONS, notification)
  }

  private def setMoodView(moodId: Int, textId: Int) {
    val notif: Notification = new Notification
    notif.contentIntent = makeMoodIntent(moodId)
    val text: CharSequence = getText(textId)
    notif.tickerText = text
    notif.icon = moodId
    val contentView: RemoteViews = new RemoteViews(getPackageName, R.layout.status_bar_balloon)
    contentView.setTextViewText(R.id.text, text)
    contentView.setImageViewResource(R.id.icon, moodId)
    notif.contentView = contentView
    mNotificationManager.notify(MOOD_NOTIFICATIONS, notif)
  }

  private def setDefault(defaults: Int) {
    val contentIntent: PendingIntent = makeDefaultIntent
    val text: CharSequence = getText(R.string.status_bar_notifications_happy_message)
    val notification: Notification = new Notification(R.drawable.stat_happy, text, System.currentTimeMillis)
    notification.setLatestEventInfo(this, getText(R.string.status_bar_notifications_mood_title), text, contentIntent)
    notification.defaults = defaults
    mNotificationManager.notify(MOOD_NOTIFICATIONS, notification)
  }


}