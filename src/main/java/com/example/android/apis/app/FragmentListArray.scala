package com.example.android.apis.app

import com.example.android.apis.Shakespeare
import android.app.Activity
import android.app.ListFragment
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import FragmentListArray._
import org.scaloid.common._

object FragmentListArray {

  class ArrayListFragment extends ListFragment {

    override def onActivityCreated(savedInstanceState: Bundle) {
      super.onActivityCreated(savedInstanceState)
      setListAdapter(new ArrayAdapter[String](getActivity, android.R.layout.simple_list_item_1, Shakespeare.TITLES))
    }

    override def onListItemClick(l: ListView,
                                 v: View,
                                 position: Int,
                                 id: Long) {
      Log.i("FragmentList", "Item clicked: " + id)
    }
  }
}

class FragmentListArray extends SActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    if (getFragmentManager.findFragmentById(android.R.id.content) ==
      null) {
      val list = new ArrayListFragment()
      getFragmentManager.beginTransaction().add(android.R.id.content, list)
        .commit()
    }
  }
}
