/*
 * Copyright (C) 2010 The Android Open Source Project
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

//Need the following import to get access to the app resources, since this
//class is in a sub-package.

import com.example.android.apis.R
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * Demonstration of styled text resources.
 */
object ExternalStorage {

  private[content] class Item {
    private[content] var mRoot: View = null
    private[content] var mCreate: Button = null
    private[content] var mDelete: Button = null
  }

}

class ExternalStorage extends Activity {
  // Scaloid >>
  val LAYOUT_INFLATER_SERVICE = android.content.Context.LAYOUT_INFLATER_SERVICE
  // Scaloid <<

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.external_storage)
    mLayout = findViewById(R.id.layout).asInstanceOf[ViewGroup]
    mExternalStoragePublicPicture = createStorageControls("Picture: getExternalStoragePublicDirectory", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), new View.OnClickListener {
      def onClick(v: View) {
        createExternalStoragePublicPicture
        updateExternalStorageState
      }
    }, new View.OnClickListener {
      def onClick(v: View) {
        deleteExternalStoragePublicPicture
        updateExternalStorageState
      }
    })
    mLayout.addView(mExternalStoragePublicPicture.mRoot)
    mExternalStoragePrivatePicture = createStorageControls("Picture getExternalFilesDir", getExternalFilesDir(Environment.DIRECTORY_PICTURES), new View.OnClickListener {
      def onClick(v: View) {
        createExternalStoragePrivatePicture
        updateExternalStorageState
      }
    }, new View.OnClickListener {
      def onClick(v: View) {
        deleteExternalStoragePrivatePicture
        updateExternalStorageState
      }
    })
    mLayout.addView(mExternalStoragePrivatePicture.mRoot)
    mExternalStoragePrivateFile = createStorageControls("File getExternalFilesDir", getExternalFilesDir(null), new View.OnClickListener {
      def onClick(v: View) {
        createExternalStoragePrivateFile
        updateExternalStorageState
      }
    }, new View.OnClickListener {
      def onClick(v: View) {
        deleteExternalStoragePrivateFile
        updateExternalStorageState
      }
    })
    mLayout.addView(mExternalStoragePrivateFile.mRoot)
    startWatchingExternalStorage
  }

  protected override def onDestroy {
    super.onDestroy
    stopWatchingExternalStorage
  }

  private[content] def handleExternalStorageState(available: Boolean, writeable: Boolean) {
    var has: Boolean = hasExternalStoragePublicPicture
    mExternalStoragePublicPicture.mCreate.setEnabled(writeable && !has)
    mExternalStoragePublicPicture.mDelete.setEnabled(writeable && has)
    has = hasExternalStoragePrivatePicture
    mExternalStoragePrivatePicture.mCreate.setEnabled(writeable && !has)
    mExternalStoragePrivatePicture.mDelete.setEnabled(writeable && has)
    has = hasExternalStoragePrivateFile
    mExternalStoragePrivateFile.mCreate.setEnabled(writeable && !has)
    mExternalStoragePrivateFile.mDelete.setEnabled(writeable && has)
  }

  private[content] def updateExternalStorageState {
    val state: String = Environment.getExternalStorageState
    if (Environment.MEDIA_MOUNTED == state) {
      mExternalStorageAvailable = ({
        mExternalStorageWriteable = true; mExternalStorageWriteable
      })
    }
    else if (Environment.MEDIA_MOUNTED_READ_ONLY == state) {
      mExternalStorageAvailable = true
      mExternalStorageWriteable = false
    }
    else {
      mExternalStorageAvailable = ({
        mExternalStorageWriteable = false; mExternalStorageWriteable
      })
    }
    handleExternalStorageState(mExternalStorageAvailable, mExternalStorageWriteable)
  }

  private[content] def startWatchingExternalStorage {
    mExternalStorageReceiver = new BroadcastReceiver {
      def onReceive(context: Context, intent: Intent) {
        Log.i("test", "Storage: " + intent.getData)
        updateExternalStorageState
      }
    }
    val filter: IntentFilter = new IntentFilter
    filter.addAction(Intent.ACTION_MEDIA_MOUNTED)
    filter.addAction(Intent.ACTION_MEDIA_REMOVED)
    registerReceiver(mExternalStorageReceiver, filter)
    updateExternalStorageState
  }

  private[content] def stopWatchingExternalStorage {
    unregisterReceiver(mExternalStorageReceiver)
  }

  private[content] def createExternalStoragePublicPicture {
    val path: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val file: File = new File(path, "DemoPicture.jpg")
    try {
      path.mkdirs
      val is: InputStream = getResources.openRawResource(R.drawable.balloons)
      val os: OutputStream = new FileOutputStream(file)
      val data: Array[Byte] = new Array[Byte](is.available)
      is.read(data)
      os.write(data)
      is.close
      os.close
      MediaScannerConnection.scanFile(this, Array[String](file.toString), null, new MediaScannerConnection.OnScanCompletedListener {
        def onScanCompleted(path: String, uri: Uri) {
          Log.i("ExternalStorage", "Scanned " + path + ":")
          Log.i("ExternalStorage", "-> uri=" + uri)
        }
      })
    }
    catch {
      case e: IOException => {
        Log.w("ExternalStorage", "Error writing " + file, e)
      }
    }
  }

  private[content] def deleteExternalStoragePublicPicture {
    val path: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val file: File = new File(path, "DemoPicture.jpg")
    file.delete
  }

  private[content] def hasExternalStoragePublicPicture: Boolean = {
    val path: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val file: File = new File(path, "DemoPicture.jpg")
    return file.exists
  }

  private[content] def createExternalStoragePrivatePicture {
    val path: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val file: File = new File(path, "DemoPicture.jpg")
    try {
      val is: InputStream = getResources.openRawResource(R.drawable.balloons)
      val os: OutputStream = new FileOutputStream(file)
      val data: Array[Byte] = new Array[Byte](is.available)
      is.read(data)
      os.write(data)
      is.close
      os.close
      MediaScannerConnection.scanFile(this, Array[String](file.toString), null, new MediaScannerConnection.OnScanCompletedListener {
        def onScanCompleted(path: String, uri: Uri) {
          Log.i("ExternalStorage", "Scanned " + path + ":")
          Log.i("ExternalStorage", "-> uri=" + uri)
        }
      })
    }
    catch {
      case e: IOException => {
        Log.w("ExternalStorage", "Error writing " + file, e)
      }
    }
  }

  private[content] def deleteExternalStoragePrivatePicture {
    val path: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    if (path != null) {
      val file: File = new File(path, "DemoPicture.jpg")
      file.delete
    }
  }

  private[content] def hasExternalStoragePrivatePicture: Boolean = {
    val path: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    if (path != null) {
      val file: File = new File(path, "DemoPicture.jpg")
      return file.exists
    }
    return false
  }

  private[content] def createExternalStoragePrivateFile {
    val file: File = new File(getExternalFilesDir(null), "DemoFile.jpg")
    try {
      val is: InputStream = getResources.openRawResource(R.drawable.balloons)
      val os: OutputStream = new FileOutputStream(file)
      val data: Array[Byte] = new Array[Byte](is.available)
      is.read(data)
      os.write(data)
      is.close
      os.close
    }
    catch {
      case e: IOException => {
        Log.w("ExternalStorage", "Error writing " + file, e)
      }
    }
  }

  private[content] def deleteExternalStoragePrivateFile {
    val file: File = new File(getExternalFilesDir(null), "DemoFile.jpg")
    if (file != null) {
      file.delete
    }
  }

  private[content] def hasExternalStoragePrivateFile: Boolean = {
    val file: File = new File(getExternalFilesDir(null), "DemoFile.jpg")
    if (file != null) {
      return file.exists
    }
    return false
  }

  private[content] def createStorageControls(label: CharSequence, path: File, createClick: View.OnClickListener, deleteClick: View.OnClickListener): ExternalStorage.Item = {
    val inflater: LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]
    val item: ExternalStorage.Item = new ExternalStorage.Item
    item.mRoot = inflater.inflate(R.layout.external_storage_item, null)
    var tv: TextView = item.mRoot.findViewById(R.id.label).asInstanceOf[TextView]
    tv.setText(label)
    if (path != null) {
      tv = item.mRoot.findViewById(R.id.path).asInstanceOf[TextView]
      tv.setText(path.toString)
    }
    item.mCreate = item.mRoot.findViewById(R.id.create).asInstanceOf[Button]
    item.mCreate.setOnClickListener(createClick)
    item.mDelete = item.mRoot.findViewById(R.id.delete).asInstanceOf[Button]
    item.mDelete.setOnClickListener(deleteClick)
    return item
  }

  private[content] var mLayout: ViewGroup = null
  private[content] var mExternalStoragePublicPicture: ExternalStorage.Item = null
  private[content] var mExternalStoragePrivatePicture: ExternalStorage.Item = null
  private[content] var mExternalStoragePrivateFile: ExternalStorage.Item = null
  private[content] var mExternalStorageReceiver: BroadcastReceiver = null
  private[content] var mExternalStorageAvailable: Boolean = false
  private[content] var mExternalStorageWriteable: Boolean = false
}