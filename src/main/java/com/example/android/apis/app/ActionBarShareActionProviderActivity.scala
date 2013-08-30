/*
 * Copyright (C) 2011 The Android Open Source Project
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

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.{MenuItem, Menu}
import android.widget.ShareActionProvider
import com.example.android.apis.R
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import org.scaloid.common._

/**
 * This activity demonstrates how to use an {@link android.view.ActionProvider}
 * for adding functionality to the Action Bar. In particular this demo is adding
 * a menu item with ShareActionProvider as its action provider. The
 * ShareActionProvider is responsible for managing the UI for sharing actions.
 */
class ActionBarShareActionProviderActivity extends SActivity {
  onCreate(copyPrivateRawResuorceToPubliclyAccessibleFile)
  override def onCreateOptionsMenu(menu: Menu) = {
    getMenuInflater.inflate(R.menu.action_bar_share_action_provider, menu)
    def setMenu(menuItem: MenuItem) = {
      val provider = menuItem.getActionProvider.asInstanceOf[ShareActionProvider]
      provider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME)
      provider.setShareIntent(createShareIntent)
    }
    setMenu(menu.findItem(R.id.menu_item_share_action_provider_action_bar))
    setMenu(menu.findItem(R.id.menu_item_share_action_provider_overflow))
    true
  }
  /**
   * Creates a sharing {@link Intent}.
   * @return The sharing intent.
   */
  private def createShareIntent: Intent = {
    val shareIntent = new Intent(Intent.ACTION_SEND)
    shareIntent.setType("image/*")
    val uri = Uri.fromFile(getFileStreamPath("shared.png"))
    shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
    shareIntent
  }
  private val SHARED_FILE_NAME: String = "shared.png"
  /**
   * Copies a private raw resource content to a publicly readable
   * file such that the latter can be shared with other applications.
   */
  private def copyPrivateRawResuorceToPubliclyAccessibleFile() {
    var inputStream: InputStream = null
    var outputStream: FileOutputStream = null
    try {
      inputStream = getResources.openRawResource(R.raw.robot)
      outputStream = openFileOutput(SHARED_FILE_NAME, Context.MODE_WORLD_READABLE | Context.MODE_APPEND)
      val buffer = new Array[Byte](1024)
      var length: Int = 0
      try {
        while (({
          length = inputStream.read(buffer);
          length }) > 0)
          outputStream.write(buffer, 0, length)
      } catch {
        case ioe: IOException => {}
      }
    } catch {
      case fnfe: FileNotFoundException => {}
    }
    finally {
      try {
        inputStream.close
      } catch {
        case ioe: IOException => {}
      }
      try {
        outputStream.close
      } catch {
        case ioe: IOException => {}
      }
    }
  }
}