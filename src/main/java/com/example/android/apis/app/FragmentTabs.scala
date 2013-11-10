package com.example.android.apis.app

import com.example.android.apis.R
import android.app.ActionBar
import android.app.ActionBar.Tab
import android.app.Activity
import android.app.Fragment
import android.app.FragmentTransaction
import android.os.Bundle
import android.widget.Toast
import FragmentTabs._
import org.scaloid.common._


object FragmentTabs {

  class TabListener[T <: Fragment](private val mActivity: Activity,
                                   private val mTag: String,
                                   private val mClass: Class[T],
                                   private val mArgs: Bundle) extends ActionBar.TabListener {

    private var mFragment: Fragment = mActivity.getFragmentManager.findFragmentByTag(mTag)

    if (mFragment != null && !mFragment.isDetached) {
      val ft = mActivity.getFragmentManager.beginTransaction()
      ft.detach(mFragment)
      ft.commit()
    }

    def this(activity: Activity, tag: String, clz: Class[T]) {
      this(activity, tag, clz, null)
    }

    def onTabSelected(tab: Tab, ft: FragmentTransaction) {
      if (mFragment == null) {
        mFragment = Fragment.instantiate(mActivity, mClass.getName, mArgs)
        ft.add(android.R.id.content, mFragment, mTag)
      } else {
        ft.attach(mFragment)
      }
    }

    def onTabUnselected(tab: Tab, ft: FragmentTransaction) {
      if (mFragment != null) {
        ft.detach(mFragment)
      }
    }

    def onTabReselected(tab: Tab, ft: FragmentTransaction) {
      Toast.makeText(mActivity, "Reselected!", Toast.LENGTH_SHORT)
        .show()
    }
  }
}

class FragmentTabs extends SActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val bar = getActionBar
    bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS)
    bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE)
    bar.addTab(bar.newTab().setText("Simple").setTabListener(new TabListener[FragmentStack.CountingFragment](this,
      "simple", classOf[FragmentStack.CountingFragment])))
    bar.addTab(bar.newTab().setText("Contacts").setTabListener(new TabListener[LoaderCursor.CursorLoaderListFragment](this,
      "contacts", classOf[LoaderCursor.CursorLoaderListFragment])))
    bar.addTab(bar.newTab().setText("Apps").setTabListener(new TabListener[LoaderCustom.AppListFragment](this,
      "apps", classOf[LoaderCustom.AppListFragment])))
    bar.addTab(bar.newTab().setText("Throttle").setTabListener(new TabListener[LoaderThrottle.ThrottledLoaderListFragment](this,
      "throttle", classOf[LoaderThrottle.ThrottledLoaderListFragment])))
    if (savedInstanceState != null) {
      bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0))
    }
  }

  protected override def onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt("tab", getActionBar.getSelectedNavigationIndex)
  }
}
