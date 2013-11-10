package com.example.android.apis.app

import com.example.android.apis.R
import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import FragmentHideShow._
import org.scaloid.common._

object FragmentHideShow {

  class FirstFragment extends Fragment {

    var mTextView: TextView = _

    override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
      val v = inflater.inflate(R.layout.labeled_text_edit, container, false)
      val tv = v.findViewById(R.id.msg)
      tv.asInstanceOf[TextView].setText("The fragment saves and restores this text.")
      mTextView = v.findViewById(R.id.saved).asInstanceOf[TextView]
      if (savedInstanceState != null) {
        mTextView.setText(savedInstanceState.getCharSequence("text"))
      }
      v
    }

    override def onSaveInstanceState(outState: Bundle) {
      super.onSaveInstanceState(outState)
      outState.putCharSequence("text", mTextView.getText)
    }
  }

  class SecondFragment extends Fragment {

    override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
      val v = inflater.inflate(R.layout.labeled_text_edit, container, false)
      val tv = v.findViewById(R.id.msg)
      tv.asInstanceOf[TextView].setText("The TextView saves and restores this text.")
      v.findViewById(R.id.saved).asInstanceOf[TextView].setSaveEnabled(true)
      v
    }
  }
}

class FragmentHideShow extends SActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.fragment_hide_show)
    val fm = getFragmentManager
    addShowHideListener(R.id.frag1hide, fm.findFragmentById(R.id.fragment1))
    addShowHideListener(R.id.frag2hide, fm.findFragmentById(R.id.fragment2))
  }

  def addShowHideListener(buttonId: Int, fragment: Fragment) {
    val button = findViewById(buttonId).asInstanceOf[Button]
    button.setOnClickListener(new OnClickListener() {

      def onClick(v: View) {
        val ft = getFragmentManager.beginTransaction()
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
        if (fragment.isHidden) {
          ft.show(fragment)
          button.setText("Hide")
        } else {
          ft.hide(fragment)
          button.setText("Show")
        }
        ft.commit()
      }
    })
  }
}
