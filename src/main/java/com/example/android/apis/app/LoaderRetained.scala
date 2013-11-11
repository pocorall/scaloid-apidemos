package com.example.android.apis.app

import android.app.Activity
import android.app.FragmentManager
import android.app.ListFragment
import android.app.LoaderManager
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.Contacts
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import android.widget.SearchView.OnQueryTextListener
import LoaderRetained._
import org.scaloid.common._


object LoaderRetained {
  // Scaloid >>
  val Contacts__ID = android.provider.BaseColumns._ID
  val Contacts_DISPLAY_NAME = "display_name" // android.provider.ContactsContract.ContactsColumns.DISPLAY_NAME
  val Contacts_CONTACT_STATUS = "contact_status" // android.provider.ContactsContract.ContactStatusColumns.CONTACT_STATUS
  val Contacts_CONTACT_PRESENCE = "contact_presence" // android.provider.ContactsContract.ContactStatusColumns.CONTACT_PRESENCE
  val Contacts_PHOTO_ID = "photo_id" // android.provider.ContactsContract.ContactsColumns.PHOTO_ID
  val Contacts_LOOKUP_KEY = "lookup" // android.provider.ContactsContract.ContactsColumns.LOOKUP_KEY
  val Contacts_HAS_PHONE_NUMBER = "has_phone_number" // android.provider.ContactsContract.ContactsColumns.HAS_PHONE_NUMBER
  // Scaloid <<

  val CONTACTS_SUMMARY_PROJECTION = Array(Contacts__ID, Contacts_DISPLAY_NAME, Contacts_CONTACT_STATUS, Contacts_CONTACT_PRESENCE, Contacts_PHOTO_ID, Contacts_LOOKUP_KEY)

  class CursorLoaderListFragment extends ListFragment with OnQueryTextListener with LoaderManager.LoaderCallbacks[Cursor] {

    var mAdapter: SimpleCursorAdapter = _

    var mCurFilter: String = _

    override def onActivityCreated(savedInstanceState: Bundle) {
      super.onActivityCreated(savedInstanceState)
      setRetainInstance(true)
      setEmptyText("No phone numbers")
      setHasOptionsMenu(true)
      mAdapter = new SimpleCursorAdapter(getActivity, android.R.layout.simple_list_item_2, null, Array(Contacts_DISPLAY_NAME, Contacts_CONTACT_STATUS),
        Array(android.R.id.text1, android.R.id.text2), 0)
      setListAdapter(mAdapter)
      setListShown(false)
      getLoaderManager.initLoader(0, null, this)
    }

    override def onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
      val item = menu.add("Search")
      item.setIcon(android.R.drawable.ic_menu_search)
      item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
      val sv = new SearchView(getActivity)
      sv.setOnQueryTextListener(this)
      item.setActionView(sv)
    }

    def onQueryTextChange(newText: String): Boolean = {
      val newFilter = if (!TextUtils.isEmpty(newText)) newText else null
      if (mCurFilter == null && newFilter == null) {
        return true
      }
      if (mCurFilter != null && mCurFilter == newFilter) {
        return true
      }
      mCurFilter = newFilter
      getLoaderManager.restartLoader(0, null, this)
      true
    }

    override def onQueryTextSubmit(query: String): Boolean = true

    override def onListItemClick(l: ListView,
                                 v: View,
                                 position: Int,
                                 id: Long) {
      Log.i("FragmentComplexList", "Item clicked: " + id)
    }

    def onCreateLoader(id: Int, args: Bundle): Loader[Cursor] = {
      var baseUri: Uri = null
      baseUri = if (mCurFilter != null) Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI, Uri.encode(mCurFilter)) else Contacts.CONTENT_URI
      val select = "((" + Contacts_DISPLAY_NAME + " NOTNULL) AND (" + Contacts_HAS_PHONE_NUMBER +
        "=1) AND (" +
        Contacts_DISPLAY_NAME +
        " != '' ))"
      new CursorLoader(getActivity, baseUri, CONTACTS_SUMMARY_PROJECTION, select, null, Contacts_DISPLAY_NAME + " COLLATE LOCALIZED ASC")
    }

    def onLoadFinished(loader: Loader[Cursor], data: Cursor) {
      mAdapter.swapCursor(data)
      if (isResumed) {
        setListShown(true)
      } else {
        setListShownNoAnimation(true)
      }
    }

    def onLoaderReset(loader: Loader[Cursor]) {
      mAdapter.swapCursor(null)
    }
  }
}

class LoaderRetained extends SActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val fm = getFragmentManager
    if (fm.findFragmentById(android.R.id.content) == null) {
      val list = new CursorLoaderListFragment()
      fm.beginTransaction().add(android.R.id.content, list)
        .commit()
    }
  }
}
