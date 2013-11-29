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
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnBufferingUpdateListener
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
import android.media.MediaPlayer.OnVideoSizeChangedListener
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast

class MediaPlayerDemo_Video extends Activity with OnBufferingUpdateListener with OnCompletionListener with OnPreparedListener with OnVideoSizeChangedListener with SurfaceHolder.Callback {
  private final val TAG: String = "MediaPlayerDemo"
  private final val MEDIA: String = "media"
  private final val LOCAL_AUDIO: Integer = 1
  private final val STREAM_AUDIO: Integer = 2
  private final val RESOURCES_AUDIO: Integer = 3
  private final val LOCAL_VIDEO: Integer = 4
  private final val STREAM_VIDEO: Integer = 5

  private var mVideoWidth: Int = 0
  private var mVideoHeight: Int = 0
  private var mMediaPlayer: MediaPlayer = null
  private var mPreview: SurfaceView = null
  private var holder: SurfaceHolder = null
  private var path: String = null
  private var extras: Bundle = null
  private var mIsVideoSizeKnown: Boolean = false
  private var mIsVideoReadyToBePlayed: Boolean = false

  /**
   *
   * Called when the activity is first created.
   */
  override def onCreate(icicle: Bundle) {
    super.onCreate(icicle)
    setContentView(R.layout.mediaplayer_2)
    mPreview = findViewById(R.id.surface).asInstanceOf[SurfaceView]
    holder = mPreview.getHolder
    holder.addCallback(this)
    holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    extras = getIntent.getExtras
  }

  private def playVideo(Media: Integer) {
    doCleanUp
    try {
      Media match {
        case LOCAL_VIDEO =>
          path = ""
          if (path eq "") {
            Toast.makeText(MediaPlayerDemo_Video.this, "Please edit MediaPlayerDemo_Video Activity, " + "and set the path variable to your media file path." + " Your media file must be stored on sdcard.", Toast.LENGTH_LONG).show
          }
        case STREAM_VIDEO =>
          path = ""
          if (path eq "") {
            Toast.makeText(MediaPlayerDemo_Video.this, "Please edit MediaPlayerDemo_Video Activity," + " and set the path variable to your media file URL.", Toast.LENGTH_LONG).show
          }
      }
      mMediaPlayer = new MediaPlayer
      mMediaPlayer.setDataSource(path)
      mMediaPlayer.setDisplay(holder)
      mMediaPlayer.prepare
      mMediaPlayer.setOnBufferingUpdateListener(this)
      mMediaPlayer.setOnCompletionListener(this)
      mMediaPlayer.setOnPreparedListener(this)
      mMediaPlayer.setOnVideoSizeChangedListener(this)
      mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
    }
    catch {
      case e: Exception => {
        Log.e(TAG, "error: " + e.getMessage, e)
      }
    }
  }

  def onBufferingUpdate(arg0: MediaPlayer, percent: Int) {
    Log.d(TAG, "onBufferingUpdate percent:" + percent)
  }

  def onCompletion(arg0: MediaPlayer) {
    Log.d(TAG, "onCompletion called")
  }

  def onVideoSizeChanged(mp: MediaPlayer, width: Int, height: Int) {
    Log.v(TAG, "onVideoSizeChanged called")
    if (width == 0 || height == 0) {
      Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")")
      return
    }
    mIsVideoSizeKnown = true
    mVideoWidth = width
    mVideoHeight = height
    if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
      startVideoPlayback
    }
  }

  def onPrepared(mediaplayer: MediaPlayer) {
    Log.d(TAG, "onPrepared called")
    mIsVideoReadyToBePlayed = true
    if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
      startVideoPlayback
    }
  }

  def surfaceChanged(surfaceholder: SurfaceHolder, i: Int, j: Int, k: Int) {
    Log.d(TAG, "surfaceChanged called")
  }

  def surfaceDestroyed(surfaceholder: SurfaceHolder) {
    Log.d(TAG, "surfaceDestroyed called")
  }

  def surfaceCreated(holder: SurfaceHolder) {
    Log.d(TAG, "surfaceCreated called")
    playVideo(extras.getInt(MEDIA))
  }

  protected override def onPause {
    super.onPause
    releaseMediaPlayer
    doCleanUp
  }

  protected override def onDestroy {
    super.onDestroy
    releaseMediaPlayer
    doCleanUp
  }

  private def releaseMediaPlayer {
    if (mMediaPlayer != null) {
      mMediaPlayer.release
      mMediaPlayer = null
    }
  }

  private def doCleanUp {
    mVideoWidth = 0
    mVideoHeight = 0
    mIsVideoReadyToBePlayed = false
    mIsVideoSizeKnown = false
  }

  private def startVideoPlayback {
    Log.v(TAG, "startVideoPlayback")
    holder.setFixedSize(mVideoWidth, mVideoHeight)
    mMediaPlayer.start
  }


}