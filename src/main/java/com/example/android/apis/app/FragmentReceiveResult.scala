package com.example.android.apis.app

import com.example.android.apis.R
import android.app.Activity
import android.app.Fragment
import android.app.FragmentTransaction
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import FragmentReceiveResult._
import org.scaloid.common._


object FragmentReceiveResult {
  // Scaloid >>
  val RESULT_CANCELED = android.app.Activity.RESULT_CANCELED
  // Scaloid <<


  val GET_CODE = 0

  class ReceiveResultFragment extends Fragment {

    private var mResults: TextView = _

    private var mGetListener: OnClickListener = new OnClickListener() {

      def onClick(v: View) {
        val intent = new Intent(getActivity, classOf[SendResult])
        startActivityForResult(intent, GET_CODE)
      }
    }

    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
    }

    override def onSaveInstanceState(outState: Bundle) {
      super.onSaveInstanceState(outState)
    }

    override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
      val v = inflater.inflate(R.layout.receive_result, container, false)
      mResults = v.findViewById(R.id.results).asInstanceOf[TextView]
      mResults.setText(mResults.getText, TextView.BufferType.EDITABLE)
      val getButton = v.findViewById(R.id.get).asInstanceOf[Button]
      getButton.setOnClickListener(mGetListener)
      v
    }

    override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
      if (requestCode == GET_CODE) {
        val text = mResults.getText.asInstanceOf[Editable]
        if (resultCode == RESULT_CANCELED) {
          text.append("(cancelled)")
        } else {
          text.append("(okay ")
          text.append(java.lang.Integer.toString(resultCode))
          text.append(") ")
          if (data != null) {
            text.append(data.getAction)
          }
        }
        text.append("\n")
      }
    }
  }
}

class FragmentReceiveResult extends SActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    val frame = new FrameLayout(this)
    frame.setId(R.id.simple_fragment)
    setContentView(frame, lp)
    if (savedInstanceState == null) {
      val newFragment = new ReceiveResultFragment()
      val ft = getFragmentManager.beginTransaction()
      ft.add(R.id.simple_fragment, newFragment).commit()
    }
  }
}
