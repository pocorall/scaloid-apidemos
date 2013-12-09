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
package com.example.android.apis.view

import com.example.android.apis.R
import android.app.ListActivity
import android.content.Context
import android.graphics.PixelFormat
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import android.widget.AbsListView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

/**
 * Another variation of the list of cheeses. In this case, we use
 * {@link AbsListView#setOnScrollListener(AbsListView.OnScrollListener)
 * AbsListView#setOnItemScrollListener(AbsListView.OnItemScrollListener)} to display the
 * first letter of the visible range of cheeses.
 */
class List9 extends ListActivity with AbsListView.OnScrollListener {
  // Scaloid >>
  val LayoutParams_MATCH_PARENT = android.view.ViewGroup.LayoutParams.MATCH_PARENT
  val LayoutParams_WRAP_CONTENT = android.view.ViewGroup.LayoutParams.WRAP_CONTENT
  // Scaloid <<

  private var mRemoveWindow: RemoveWindow = new RemoveWindow
  private[view] var mHandler: Handler = new Handler
  private var mWindowManager: WindowManager = null
  private var mDialogText: TextView = null
  private var mShowing: Boolean = false
  private var mReady: Boolean = false
  private var mPrevLetter: Char = Character.MIN_VALUE
  private var mStrings: Array[String] = Cheeses.sCheeseStrings

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    mWindowManager = getSystemService(Context.WINDOW_SERVICE).asInstanceOf[WindowManager]
    setListAdapter(new ArrayAdapter[String](this, android.R.layout.simple_list_item_1, mStrings))
    getListView.setOnScrollListener(this)
    val inflate: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]
    mDialogText = inflate.inflate(R.layout.list_position, null).asInstanceOf[TextView]
    mDialogText.setVisibility(View.INVISIBLE)
    mHandler.post(new Runnable {
      def run {
        mReady = true
        val lp: WindowManager.LayoutParams = new WindowManager.LayoutParams(LayoutParams_WRAP_CONTENT, LayoutParams_WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT)
        mWindowManager.addView(mDialogText, lp)
      }
    })
  }

  protected override def onResume {
    super.onResume
    mReady = true
  }

  protected override def onPause {
    super.onPause
    removeWindow
    mReady = false
  }

  protected override def onDestroy {
    super.onDestroy
    mWindowManager.removeView(mDialogText)
    mReady = false
  }

  def onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
    if (mReady) {
      val firstLetter: Char = mStrings(firstVisibleItem).charAt(0)
      if (!mShowing && firstLetter != mPrevLetter) {
        mShowing = true
        mDialogText.setVisibility(View.VISIBLE)
      }
      mDialogText.setText((firstLetter.asInstanceOf[Character]).toString)
      mHandler.removeCallbacks(mRemoveWindow)
      mHandler.postDelayed(mRemoveWindow, 3000)
      mPrevLetter = firstLetter
    }
  }

  def onScrollStateChanged(view: AbsListView, scrollState: Int) {
  }

  private def removeWindow {
    if (mShowing) {
      mShowing = false
      mDialogText.setVisibility(View.INVISIBLE)
    }
  }

  private final class RemoveWindow extends Runnable {
    def run {
      removeWindow
    }
  }

}