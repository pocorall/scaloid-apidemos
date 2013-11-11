package com.example.android.apis.app

import com.example.android.apis.R
import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ContextMenu.ContextMenuInfo
import FragmentContextMenu._
import org.scaloid.common._


object FragmentContextMenu {

  class ContextMenuFragment extends Fragment {

    override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
      val root = inflater.inflate(R.layout.fragment_context_menu, container, false)
      registerForContextMenu(root.findViewById(R.id.long_press))
      root
    }

    override def onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo) {
      super.onCreateContextMenu(menu, v, menuInfo)
      menu.add(Menu.NONE, R.id.a_item, Menu.NONE, "Menu A")
      menu.add(Menu.NONE, R.id.b_item, Menu.NONE, "Menu B")
    }

    override def onContextItemSelected(item: MenuItem): Boolean = item.getItemId match {
      case R.id.a_item =>
        Log.i("ContextMenu", "Item 1a was chosen")
        true

      case R.id.b_item =>
        Log.i("ContextMenu", "Item 1b was chosen")
        true

    }
  }
}

class FragmentContextMenu extends SActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val content = new ContextMenuFragment()
    getFragmentManager.beginTransaction().add(android.R.id.content, content)
      .commit()
  }
}
