package com.example.android.apis.app

import com.example.android.apis.R
import android.app.Activity
import android.app.DialogFragment
import android.app.Fragment
import android.app.FragmentTransaction
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import FragmentDialog._
import org.scaloid.common._


object FragmentDialog {

  def getNameForNum(num: Int): String = (num - 1) % 6 match {
    case 1 => "STYLE_NO_TITLE"
    case 2 => "STYLE_NO_FRAME"
    case 3 => "STYLE_NO_INPUT (this window can't receive input, so " +
      "you will need to press the bottom show button)"
    case 4 => "STYLE_NORMAL with dark fullscreen theme"
    case 5 => "STYLE_NORMAL with light theme"
    case 6 => "STYLE_NO_TITLE with light theme"
    case 7 => "STYLE_NO_FRAME with light theme"
    case 8 => "STYLE_NORMAL with light fullscreen theme"
  }

  object MyDialogFragment {

    def newInstance(num: Int): MyDialogFragment = {
      val f = new MyDialogFragment()
      val args = new Bundle()
      args.putInt("num", num)
      f.setArguments(args)
      f
    }
  }

  class MyDialogFragment extends DialogFragment {

    var mNum: Int = _

    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      mNum = getArguments.getInt("num")
      var style = DialogFragment.STYLE_NORMAL
      var theme = 0
      (mNum - 1) % 6 match {
        case 1 => style = DialogFragment.STYLE_NO_TITLE
        case 2 => style = DialogFragment.STYLE_NO_FRAME
        case 3 => style = DialogFragment.STYLE_NO_INPUT
        case 4 => style = DialogFragment.STYLE_NORMAL
        case 5 => style = DialogFragment.STYLE_NORMAL
        case 6 => style = DialogFragment.STYLE_NO_TITLE
        case 7 => style = DialogFragment.STYLE_NO_FRAME
        case 8 => style = DialogFragment.STYLE_NORMAL
      }
      (mNum - 1) % 6 match {
        case 4 => theme = android.R.style.Theme_Holo
        case 5 => theme = android.R.style.Theme_Holo_Light_Dialog
        case 6 => theme = android.R.style.Theme_Holo_Light
        case 7 => theme = android.R.style.Theme_Holo_Light_Panel
        case 8 => theme = android.R.style.Theme_Holo_Light
      }
      setStyle(style, theme)
    }

    override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
      val v = inflater.inflate(R.layout.fragment_dialog, container, false)
      val tv = v.findViewById(R.id.text)
      tv.asInstanceOf[TextView].setText("Dialog #" + mNum + ": using style " + getNameForNum(mNum))
      val button = v.findViewById(R.id.show).asInstanceOf[Button]
      button.setOnClickListener(new OnClickListener() {

        def onClick(v: View) {
          getActivity.asInstanceOf[FragmentDialog].showDialog()
        }
      })
      v
    }
  }
}

class FragmentDialog extends SActivity {

  var mStackLevel: Int = 0

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.fragment_dialog)
    val tv = findViewById(R.id.text)
    tv.asInstanceOf[TextView].setText("Example of displaying dialogs with a DialogFragment.  " +
      "Press the show button below to see the first dialog; pressing " +
      "successive show buttons will display other dialog styles as a " +
      "stack, with dismissing or back going to the previous dialog.")
    val button = findViewById(R.id.show).asInstanceOf[Button]
    button.setOnClickListener(new OnClickListener() {

      def onClick(v: View) {
        showDialog()
      }
    })
    if (savedInstanceState != null) {
      mStackLevel = savedInstanceState.getInt("level")
    }
  }

  override def onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt("level", mStackLevel)
  }

  def showDialog() {
    mStackLevel += 1
    val ft = getFragmentManager.beginTransaction()
    val prev = getFragmentManager.findFragmentByTag("dialog")
    if (prev != null) {
      ft.remove(prev)
    }
    ft.addToBackStack(null)
    val newFragment = MyDialogFragment.newInstance(mStackLevel)
    newFragment.show(ft, "dialog")
  }
}
