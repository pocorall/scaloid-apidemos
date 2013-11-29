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

import com.example.android.apis.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button

class MediaPlayerDemo extends Activity {
  private final val MEDIA: String = "media"
  private final val LOCAL_AUDIO: Int = 1
  private final val STREAM_AUDIO: Int = 2
  private final val RESOURCES_AUDIO: Int = 3
  private final val LOCAL_VIDEO: Int = 4
  private final val STREAM_VIDEO: Int = 5
  private final val RESOURCES_VIDEO: Int = 6

  protected override def onCreate(icicle: Bundle) {
    super.onCreate(icicle)
    setContentView(R.layout.mediaplayer_1)
    mlocalaudio = findViewById(R.id.localaudio).asInstanceOf[Button]
    mlocalaudio.setOnClickListener(mLocalAudioListener)
    mresourcesaudio = findViewById(R.id.resourcesaudio).asInstanceOf[Button]
    mresourcesaudio.setOnClickListener(mResourcesAudioListener)
    mlocalvideo = findViewById(R.id.localvideo).asInstanceOf[Button]
    mlocalvideo.setOnClickListener(mLocalVideoListener)
    mstreamvideo = findViewById(R.id.streamvideo).asInstanceOf[Button]
    mstreamvideo.setOnClickListener(mStreamVideoListener)
  }

  private var mlocalvideo: Button = null
  private var mresourcesvideo: Button = null
  private var mstreamvideo: Button = null
  private var mlocalaudio: Button = null
  private var mresourcesaudio: Button = null
  private var mstreamaudio: Button = null
  private var mLocalAudioListener: View.OnClickListener = new View.OnClickListener {
    def onClick(v: View) {
      val intent: Intent = new Intent(MediaPlayerDemo.this.getApplication, classOf[MediaPlayerDemo_Audio])
      intent.putExtra(MEDIA, LOCAL_AUDIO)
      startActivity(intent)
    }
  }
  private var mResourcesAudioListener: View.OnClickListener = new View.OnClickListener {
    def onClick(v: View) {
      val intent: Intent = new Intent(MediaPlayerDemo.this.getApplication, classOf[MediaPlayerDemo_Audio])
      intent.putExtra(MEDIA, RESOURCES_AUDIO)
      startActivity(intent)
    }
  }
  private var mLocalVideoListener: View.OnClickListener = new View.OnClickListener {
    def onClick(v: View) {
      val intent: Intent = new Intent(MediaPlayerDemo.this, classOf[MediaPlayerDemo_Video])
      intent.putExtra(MEDIA, LOCAL_VIDEO)
      startActivity(intent)
    }
  }
  private var mStreamVideoListener: View.OnClickListener = new View.OnClickListener {
    def onClick(v: View) {
      val intent: Intent = new Intent(MediaPlayerDemo.this, classOf[MediaPlayerDemo_Video])
      intent.putExtra(MEDIA, STREAM_VIDEO)
      startActivity(intent)
    }
  }
}