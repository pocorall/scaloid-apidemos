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
package com.example.android.apis.app

import com.example.android.apis.R
import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import android.util.AttributeSet
import android.view.{Gravity, LayoutInflater, View, ViewGroup}
import android.widget.TextView
import org.scaloid.common._
/**
 * Demonstrates a fragment that can be configured through both Bundle arguments
 * and layout attributes.
 */
object FragmentArguments {


  object MyFragment {
    /**
     * Create a new instance of MyFragment that will be initialized
     * with the given arguments.
     */
    private[app] def newInstance(label: CharSequence): FragmentArguments.MyFragment = {
      val f = new FragmentArguments.MyFragment
      val b = new Bundle
      b.putCharSequence("label", label)
      f.setArguments(b)
      return f
    }
  }
  class MyFragment extends Fragment {
    /**
     * Parse attributes during inflation from a view hierarchy into the
     * arguments we handle.
     */
    override def onInflate(activity: Activity, attrs: AttributeSet, savedInstanceState: Bundle) {
      super.onInflate(activity, attrs, savedInstanceState)
      val a = activity.obtainStyledAttributes(attrs, R.styleable.FragmentArguments)
      mLabel = a.getText(R.styleable.FragmentArguments_android_label)
      a.recycle
    }
    /**
     * During creation, if arguments have been supplied to the fragment
     * then parse those out.
     */
    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      val args = getArguments
      if (args != null) {
        mLabel = args.getCharSequence("label", mLabel)
      }
    }
    /**
     * Create the view for this fragment, using the arguments given to it.
     */
    override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
      val v = inflater.inflate(R.layout.hello_world, container, false)
      val tv = v.findViewById(R.id.text)
      (tv.asInstanceOf[TextView]).setText(if (mLabel != null) mLabel else "(no label)")
      tv.setBackgroundDrawable(getResources.getDrawable(android.R.drawable.gallery_thumb))
      return v
    }
    private[app] var mLabel: CharSequence = null
  }
}
import FragmentArguments._
class FragmentArguments extends SActivity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.fragment_arguments)
//    contentView = new SVerticalLayout {
//      STextView(R.string.fragment_arguments_msg).padding(4 dip).Gravity(Gravity.TOP|Gravity.CENTER_VERTICAL).Weight(0).wrap.>>.setTextAppearance(context, android.R.attr.textAppearanceMedium)
//      this += new SLinearLayout {
////        Fragment(R.string.fragment_arguments_embedded).<<(0, WRAP_CONTENT).Weight(1)
//        this += new SFrameLayout().<<(0, WRAP_CONTENT).Weight(1).>>
//      }.padding(4 dip)
////      Fragment(R.string.fragment_arguments_embedded_land).<<(MATCH_PARENT, WRAP_CONTENT)
//    }.padding(4 dip).gravity(Gravity.CENTER_HORIZONTAL)
    if (savedInstanceState == null) {
      val ft = getFragmentManager.beginTransaction
      import FragmentArguments._
      val newFragment = MyFragment.newInstance("From Arguments")
      ft.add(R.id.created, newFragment)
      ft.commit
    }
  }
}