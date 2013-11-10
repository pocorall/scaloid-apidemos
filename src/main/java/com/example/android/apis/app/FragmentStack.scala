package com.example.android.apis.app

import com.example.android.apis.R
import android.app.Activity
import android.app.Fragment
import android.app.FragmentTransaction
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import FragmentStack._
import org.scaloid.common._


object FragmentStack {

  object CountingFragment {

    def newInstance(num: Int): CountingFragment = {
      val f = new CountingFragment()
      val args = new Bundle()
      args.putInt("num", num)
      f.setArguments(args)
      f
    }
  }

  class CountingFragment extends Fragment {

    var mNum: Int = _

    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      mNum = if (getArguments != null) getArguments.getInt("num") else 1
    }

    override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
      val v = inflater.inflate(R.layout.hello_world, container, false)
      val tv = v.findViewById(R.id.text)
      tv.asInstanceOf[TextView].setText("Fragment #" + mNum)
      tv.setBackgroundDrawable(getResources.getDrawable(android.R.drawable.gallery_thumb))
      v
    }
  }
}

class FragmentStack extends SActivity {

  var mStackLevel: Int = 1

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.fragment_stack)
    val button = findViewById(R.id.new_fragment).asInstanceOf[Button]
    button.setOnClickListener(new OnClickListener() {

      def onClick(v: View) {
        addFragmentToStack()
      }
    })
    if (savedInstanceState == null) {
      val newFragment = CountingFragment.newInstance(mStackLevel)
      val ft = getFragmentManager.beginTransaction()
      ft.add(R.id.simple_fragment, newFragment).commit()
    } else {
      mStackLevel = savedInstanceState.getInt("level")
    }
  }

  override def onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt("level", mStackLevel)
  }

  def addFragmentToStack() {
    mStackLevel += 1
    val newFragment = CountingFragment.newInstance(mStackLevel)
    val ft = getFragmentManager.beginTransaction()
    ft.replace(R.id.simple_fragment, newFragment)
    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
    ft.addToBackStack(null)
    ft.commit()
  }
}
