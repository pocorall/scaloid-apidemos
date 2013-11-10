package com.example.android.apis.app

import com.example.android.apis.R
import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.CheckBox
import FragmentMenu._
import org.scaloid.common._

object FragmentMenu {

  class MenuFragment extends Fragment {

    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      setHasOptionsMenu(true)
    }

    override def onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
      menu.add("Menu 1a").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
      menu.add("Menu 1b").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    }
  }

  class Menu2Fragment extends Fragment {

    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      setHasOptionsMenu(true)
    }

    override def onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
      menu.add("Menu 2").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    }
  }
}

class FragmentMenu extends SActivity {

  var mFragment1: Fragment = _

  var mFragment2: Fragment = _

  var mCheckBox1: CheckBox = _

  var mCheckBox2: CheckBox = _

  val mClickListener = new OnClickListener() {

    def onClick(v: View) {
      updateFragmentVisibility()
    }
  }

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.fragment_menu)
    val fm = getFragmentManager
    val ft = fm.beginTransaction()
    mFragment1 = fm.findFragmentByTag("f1")
    if (mFragment1 == null) {
      mFragment1 = new MenuFragment()
      ft.add(mFragment1, "f1")
    }
    mFragment2 = fm.findFragmentByTag("f2")
    if (mFragment2 == null) {
      mFragment2 = new Menu2Fragment()
      ft.add(mFragment2, "f2")
    }
    ft.commit()
    mCheckBox1 = findViewById(R.id.menu1).asInstanceOf[CheckBox]
    mCheckBox1.setOnClickListener(mClickListener)
    mCheckBox2 = findViewById(R.id.menu2).asInstanceOf[CheckBox]
    mCheckBox2.setOnClickListener(mClickListener)
    updateFragmentVisibility()
  }

  protected override def onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    updateFragmentVisibility()
  }

  def updateFragmentVisibility() {
    val ft = getFragmentManager.beginTransaction()
    if (mCheckBox1.isChecked) ft.show(mFragment1) else ft.hide(mFragment1)
    if (mCheckBox2.isChecked) ft.show(mFragment2) else ft.hide(mFragment2)
    ft.commit()
  }
}
