package com.example.android.apis.view

import android.app.ExpandableListActivity
import android.os.Bundle
import android.widget.ExpandableListAdapter
import android.widget.SimpleExpandableListAdapter
import java.util.ArrayList
import java.util.HashMap
import java.util.List
import java.util.Map
//remove if not needed
import scala.collection.JavaConversions._

class ExpandableList3 extends ExpandableListActivity {
  private val NAME = "NAME"

  private val IS_EVEN = "IS_EVEN"

  private var mAdapter: ExpandableListAdapter = _

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val groupData = new ArrayList[Map[String, String]]()
    val childData = new ArrayList[List[Map[String, String]]]()
    for (i <- 0 until 20) {
      val curGroupMap = new HashMap[String, String]()
      groupData.add(curGroupMap)
      curGroupMap.put(NAME, "Group " + i)
      curGroupMap.put(IS_EVEN, if ((i % 2 == 0)) "This group is even" else "This group is odd")
      val children = new ArrayList[Map[String, String]]()
      for (j <- 0 until 15) {
        val curChildMap = new HashMap[String, String]()
        children.add(curChildMap)
        curChildMap.put(NAME, "Child " + j)
        curChildMap.put(IS_EVEN, if ((j % 2 == 0)) "This child is even" else "This child is odd")
      }
      childData.add(children)
    }
    mAdapter = new SimpleExpandableListAdapter(this, groupData, android.R.layout.simple_expandable_list_item_1,
      Array(NAME, IS_EVEN), Array(android.R.id.text1, android.R.id.text2), childData, android.R.layout.simple_expandable_list_item_2,
      Array(NAME, IS_EVEN), Array(android.R.id.text1, android.R.id.text2))
    setListAdapter(mAdapter)
  }
}
