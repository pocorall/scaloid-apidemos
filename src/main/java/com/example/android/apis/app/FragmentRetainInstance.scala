package com.example.android.apis.app

import com.example.android.apis.R
import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ProgressBar
import FragmentRetainInstance._
import org.scaloid.common._


object FragmentRetainInstance {

  class UiFragment extends Fragment {

    var mWorkFragment: RetainedFragment = _

    override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
      val v = inflater.inflate(R.layout.fragment_retain_instance, container, false)
      val button = v.findViewById(R.id.restart).asInstanceOf[Button]
      button.setOnClickListener(new OnClickListener() {

        def onClick(v: View) {
          mWorkFragment.restart()
        }
      })
      v
    }

    override def onActivityCreated(savedInstanceState: Bundle) {
      super.onActivityCreated(savedInstanceState)
      val fm = getFragmentManager
      mWorkFragment = fm.findFragmentByTag("work").asInstanceOf[RetainedFragment]
      if (mWorkFragment == null) {
        mWorkFragment = new RetainedFragment()
        mWorkFragment.setTargetFragment(this, 0)
        fm.beginTransaction().add(mWorkFragment, "work").commit()
      }
    }
  }

  class RetainedFragment extends Fragment {

    var mProgressBar: ProgressBar = _

    var mPosition: Int = _

    var mReady: Boolean = false

    var mQuiting: Boolean = false

    val mThread = new Thread() {

      override def run() {
        var max = 10000
        while (true) {
          synchronized {
            while (!mReady || mPosition >= max) {
              if (mQuiting) {
                return
              }
              try {
                wait()
              } catch {
                case e: InterruptedException =>
              }
            }
            mPosition += 1
            max = mProgressBar.getMax
            mProgressBar.setProgress(mPosition)
          }
          synchronized {
            try {
              wait(50)
            } catch {
              case e: InterruptedException =>
            }
          }
        }
      }
    }

    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      setRetainInstance(true)
      mThread.start()
    }

    override def onActivityCreated(savedInstanceState: Bundle) {
      super.onActivityCreated(savedInstanceState)
      mProgressBar = getTargetFragment.getView.findViewById(R.id.progress_horizontal).asInstanceOf[ProgressBar]
      synchronized {
        mReady = true
        mThread.notify()
      }
    }

    override def onDestroy() {
      synchronized {
        mReady = false
        mQuiting = true
        mThread.notify()
      }
      super.onDestroy()
    }

    override def onDetach() {
      synchronized {
        mProgressBar = null
        mReady = false
        mThread.notify()
      }
      super.onDetach()
    }

    def restart() {
      synchronized {
        mPosition = 0
        mThread.notify()
      }
    }
  }
}

class FragmentRetainInstance extends SActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState == null) {
      getFragmentManager.beginTransaction().add(android.R.id.content, new UiFragment())
        .commit()
    }
  }
}
