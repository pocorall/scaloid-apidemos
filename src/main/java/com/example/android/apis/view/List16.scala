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
package com.example.android.apis.view

import com.example.android.apis.R
import android.app.ListActivity
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

/**
 * This demo illustrates the use of CHOICE_MODE_MULTIPLE_MODAL, a.k.a. selection mode on ListView
 * couple with the new simple_list_item_activated_1 which uses a highlighted border for selected
 * items.
 */
class List16 extends ListActivity {
  // Scaloid >>
  val ListView_CHOICE_MODE_MULTIPLE_MODAL = android.widget.AbsListView.CHOICE_MODE_MULTIPLE_MODAL
  // Scaloid <<


  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val lv: ListView = getListView
    lv.setChoiceMode(ListView_CHOICE_MODE_MULTIPLE_MODAL)
    lv.setMultiChoiceModeListener(new ModeCallback)
    setListAdapter(new ArrayAdapter[String](this, android.R.layout.simple_list_item_activated_1, Cheeses.sCheeseStrings))
  }

  protected override def onPostCreate(savedInstanceState: Bundle) {
    super.onPostCreate(savedInstanceState)
    getActionBar.setSubtitle("Long press to start selection")
  }

  private class ModeCallback extends android.widget.AbsListView.MultiChoiceModeListener {
    def onCreateActionMode(mode: ActionMode, menu: Menu): Boolean = {
      val inflater: MenuInflater = getMenuInflater
      inflater.inflate(R.menu.list_select_menu, menu)
      mode.setTitle("Select Items")
      return true
    }

    def onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean = {
      return true
    }

    def onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean = {
      item.getItemId match {
        case R.id.share =>
          Toast.makeText(List16.this, "Shared " + getListView.getCheckedItemCount + " items", Toast.LENGTH_SHORT).show
          mode.finish
        case _ =>
          Toast.makeText(List16.this, "Clicked " + item.getTitle, Toast.LENGTH_SHORT).show
      }
      return true
    }

    def onDestroyActionMode(mode: ActionMode) {
    }

    def onItemCheckedStateChanged(mode: ActionMode, position: Int, id: Long, checked: Boolean) {
      val checkedCount: Int = getListView.getCheckedItemCount
      checkedCount match {
        case 0 =>
          mode.setSubtitle(null)
        case 1 =>
          mode.setSubtitle("One item selected")
        case _ =>
          mode.setSubtitle("" + checkedCount + " items selected")
      }
    }
  }

}