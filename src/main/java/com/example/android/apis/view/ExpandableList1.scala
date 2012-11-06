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


import android.app.ExpandableListActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget._
import com.example.android.apis.R
import net.pocorall.android.common._

/**
 * Demonstrates expandable lists using a custom {@link ExpandableListAdapter}
 * from {@link BaseExpandableListAdapter}.
 */
class ExpandableList1 extends ExpandableListActivity with ContextUtil {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setListAdapter(new MyExpandableListAdapter)
    getExpandableListView.onCreateContextMenu {
      (menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) =>
        menu.headerTitle = "Sample menu"
        menu.add(0, 0, 0, R.string.expandable_list_sample_action)
    }
  }

  override def onContextItemSelected(item: MenuItem): Boolean = {
    import ExpandableListView._
    val info = item.getMenuInfo.asInstanceOf[ExpandableListContextMenuInfo]
    val title = (info.targetView.asInstanceOf[TextView]).getText.toString
    getPackedPositionType(info.packedPosition) match {
      case PACKED_POSITION_TYPE_CHILD =>
        toast(title + ": Child " + getPackedPositionChild(info.packedPosition) + " clicked in group " + getPackedPositionGroup(info.packedPosition))
      case PACKED_POSITION_TYPE_GROUP =>
        toast(title + ": Group " + getPackedPositionGroup(info.packedPosition) + " clicked")
      case _ => return false
    }
    true
  }

  /**
   * A simple adapter which maintains an ArrayList of photo resource Ids.
   * Each photo is displayed as an image. This adapter supports clearing the
   * list of photos and adding a new photo.
   *
   */
  class MyExpandableListAdapter extends BaseExpandableListAdapter {
    def getChild(groupPosition: Int, childPosition: Int): AnyRef = children(groupPosition)(childPosition)

    def getChildId(groupPosition: Int, childPosition: Int): Long = childPosition

    def getChildrenCount(groupPosition: Int): Int = children(groupPosition).length

    def getGenericView: TextView = new $TextView {
      layoutParams = (new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 64))
      gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT
      setPadding(36, 0, 0, 0)
    }

    def getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View, parent: ViewGroup): View =
      getGenericView.text = getChild(groupPosition, childPosition).toString

    def getGroup(groupPosition: Int): AnyRef = groups(groupPosition)

    def getGroupCount: Int = groups.length

    def getGroupId(groupPosition: Int): Long = groupPosition

    def getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View, parent: ViewGroup): View =
      getGenericView.text = getGroup(groupPosition).toString

    def isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true

    def hasStableIds: Boolean = true

    private val groups: Array[String] = Array("People Names", "Dog Names", "Cat Names", "Fish Names")
    private val children: Array[Array[String]] = Array(Array("Arnold", "Barry", "Chuck", "David"), Array("Ace", "Bandit", "Cha-Cha", "Deuce"), Array("Fluffy", "Snuggles"), Array("Goldy", "Bubbles"))
  }

}