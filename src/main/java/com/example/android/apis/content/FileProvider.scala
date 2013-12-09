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
package com.example.android.apis.content

import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import android.content.ContentProvider
import android.content.ContentValues
import android.content.ContentProvider.PipeDataWriter
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log

/**
 * A very simple content provider that can serve arbitrary asset files from
 * our .apk.
 */
class FileProvider extends ContentProvider with PipeDataWriter[InputStream] {
  def onCreate: Boolean = {
    return true
  }

  def query(uri: Uri, projection: Array[String], selection: String, selectionArgs: Array[String], sortOrder: String): Cursor = {
    return null
  }

  def insert(uri: Uri, values: ContentValues): Uri = {
    return null
  }

  def delete(uri: Uri, selection: String, selectionArgs: Array[String]): Int = {
    return 0
  }

  def update(uri: Uri, values: ContentValues, selection: String, selectionArgs: Array[String]): Int = {
    return 0
  }

  def getType(uri: Uri): String = {
    return "application/vnd.android.package-archive"
  }

  override def openAssetFile(uri: Uri, mode: String): AssetFileDescriptor = {
    try {
      val is: InputStream = getContext.getAssets.open(uri.getPath)
      return new AssetFileDescriptor(openPipeHelper(uri, null, null, is, this), 0, AssetFileDescriptor.UNKNOWN_LENGTH)
    }
    catch {
      case e: IOException => {
        val fnf: FileNotFoundException = new FileNotFoundException("Unable to open " + uri)
        throw fnf
      }
    }
  }

  def writeDataToPipe(output: ParcelFileDescriptor, uri: Uri, mimeType: String, opts: Bundle, args: InputStream) {
    val buffer: Array[Byte] = new Array[Byte](8192)
    var n: Int = 0
    val fout: FileOutputStream = new FileOutputStream(output.getFileDescriptor)
    try {
      while ((({
        n = args.read(buffer); n
      })) >= 0) {
        fout.write(buffer, 0, n)
      }
    }
    catch {
      case e: IOException => {
        Log.i("InstallApk", "Failed transferring", e)
      }
    }
    finally {
      try {
        args.close
      }
      catch {
        case e: IOException => {
        }
      }
      try {
        fout.close
      }
      catch {
        case e: IOException => {
        }
      }
    }
  }
}