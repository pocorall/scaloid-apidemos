package com.example.android.apis.app

import com.example.android.apis.R
import android.app.Activity
import android.app.DialogFragment
import android.app.Fragment
import android.app.FragmentTransaction
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import FragmentDialogOrActivity._
import org.scaloid.common._

object FragmentDialogOrActivity {

  object MyDialogFragment {

    def newInstance(): MyDialogFragment = new MyDialogFragment()
  }

  class MyDialogFragment extends DialogFragment {

    override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
      val v = inflater.inflate(R.layout.hello_world, container, false)
      val tv = v.findViewById(R.id.text)
      tv.asInstanceOf[TextView].setText("This is an instance of MyDialogFragment")
      v
    }
  }
}

class FragmentDialogOrActivity extends SActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.fragment_dialog_or_activity)
    if (savedInstanceState == null) {
      val ft = getFragmentManager.beginTransaction()
      val newFragment = MyDialogFragment.newInstance()
      ft.add(R.id.embedded, newFragment)
      ft.commit()
    }
    val button = findViewById(R.id.show_dialog).asInstanceOf[Button]
    button.setOnClickListener(new OnClickListener() {

      def onClick(v: View) {
        showDialog()
      }
    })
  }

  def showDialog() {
    val newFragment = MyDialogFragment.newInstance()
    newFragment.show(getFragmentManager, "dialog")
  }
}
