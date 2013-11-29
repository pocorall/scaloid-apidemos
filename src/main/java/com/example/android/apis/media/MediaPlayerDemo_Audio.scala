/*
 * Copyright (C) 2009 The Android Open Source Project
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
package com.example.android.apis.media

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.android.apis.R

class MediaPlayerDemo_Audio extends Activity {
  private final val TAG: String = "MediaPlayerDemo"
  private final val MEDIA: String = "media"
  private final val LOCAL_AUDIO: Integer = 1
  private final val STREAM_AUDIO: Integer = 2
  private final val RESOURCES_AUDIO: Integer = 3
  private final val LOCAL_VIDEO: Integer = 4
  private final val STREAM_VIDEO: Integer = 5

  private var mMediaPlayer: MediaPlayer = null
  private var path: String = null
  private var tx: TextView = null

  override def onCreate(icicle: Bundle) {
    super.onCreate(icicle)
    tx = new TextView(this)
    setContentView(tx)
    val extras: Bundle = getIntent.getExtras
    playAudio(extras.getInt(MEDIA))
  }

  private def playAudio(media: Integer) {
    try {
      media match {
        case LOCAL_AUDIO =>
          path = ""
          if (path eq "") {
            Toast.makeText(MediaPlayerDemo_Audio.this, "Please edit MediaPlayer_Audio Activity, " + "and set the path variable to your audio file path." + " Your audio file must be stored on sdcard.", Toast.LENGTH_LONG).show
          }
          mMediaPlayer = new MediaPlayer
          mMediaPlayer.setDataSource(path)
          mMediaPlayer.prepare
          mMediaPlayer.start
        case RESOURCES_AUDIO =>
          mMediaPlayer = MediaPlayer.create(this, R.raw.test_cbr)
          mMediaPlayer.start
      }
      tx.setText("Playing audio...")
    }
    catch {
      case e: Exception => {
        Log.e(TAG, "error: " + e.getMessage, e)
      }
    }
  }

  protected override def onDestroy {
    super.onDestroy
    if (mMediaPlayer != null) {
      mMediaPlayer.release
      mMediaPlayer = null
    }
  }
}