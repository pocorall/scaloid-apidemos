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
package com.example.android.apis.app

import android.app.Activity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.android.apis.R
import java.util.Locale
import java.util.Random

/**
 * <p>Demonstrates text-to-speech (TTS). Please note the following steps:</p>
 *
 * <ol>
 * <li>Construct the TextToSpeech object.</li>
 * <li>Handle initialization callback in the onInit method.
 * The activity implements TextToSpeech.OnInitListener for this purpose.</li>
 * <li>Call TextToSpeech.speak to synthesize speech.</li>
 * <li>Shutdown TextToSpeech in onDestroy.</li>
 * </ol>
 *
 * <p>Documentation:
 * http://developer.android.com/reference/android/speech/tts/package-summary.html
 * </p>
 * <ul>
 */
class TextToSpeechActivity extends Activity with TextToSpeech.OnInitListener {
  private final val TAG: String = "TextToSpeechDemo"
  private final val RANDOM: Random = new Random
  private final val HELLOS: Array[String] = Array("Hello", "Salutations", "Greetings", "Howdy", "What's crack-a-lackin?", "That explains the stench!")

  private var mTts: TextToSpeech = null
  private var mAgainButton: Button = null

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.text_to_speech)
    mTts = new TextToSpeech(this, this)
    mAgainButton = findViewById(R.id.again_button).asInstanceOf[Button]
    mAgainButton.setOnClickListener(new View.OnClickListener {
      def onClick(v: View) {
        sayHello
      }
    })
  }

  override def onDestroy {
    if (mTts != null) {
      mTts.stop
      mTts.shutdown
    }
    super.onDestroy
  }

  def onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      val result: Int = mTts.setLanguage(Locale.US)
      if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
        Log.e(TAG, "Language is not available.")
      }
      else {
        mAgainButton.setEnabled(true)
        sayHello
      }
    }
    else {
      Log.e(TAG, "Could not initialize TextToSpeech.")
    }
  }

  private def sayHello {
    val helloLength: Int = HELLOS.length
    val hello: String = HELLOS(RANDOM.nextInt(helloLength))
    mTts.speak(hello, TextToSpeech.QUEUE_FLUSH, null)
  }
}