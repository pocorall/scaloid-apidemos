package com.example.android.apis.app

import com.example.android.apis.R
import com.example.android.apis.Shakespeare
import android.app.Activity
import android.app.Fragment
import android.app.FragmentTransaction
import android.app.ListFragment
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ScrollView
import android.widget.TextView
import FragmentLayout._
import org.scaloid.common._

object FragmentLayout {
  // Scaloid >>
  val ListView_CHOICE_MODE_SINGLE = android.widget.AbsListView.CHOICE_MODE_SINGLE
  // Scaloid <<

  class DetailsActivity extends SActivity {

    protected override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      if (getResources.getConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        finish()
        return
      }
      if (savedInstanceState == null) {
        val details = new DetailsFragment()
        details.setArguments(getIntent.getExtras)
        getFragmentManager.beginTransaction().add(android.R.id.content, details)
          .commit()
      }
    }
  }

  class TitlesFragment extends ListFragment {

    var mDualPane: Boolean = _

    var mCurCheckPosition: Int = 0

    override def onActivityCreated(savedInstanceState: Bundle) {
      super.onActivityCreated(savedInstanceState)
      setListAdapter(new ArrayAdapter[String](getActivity, android.R.layout.simple_list_item_activated_1,
        Shakespeare.TITLES))
      val detailsFrame = getActivity.findViewById(R.id.details)
      mDualPane = detailsFrame != null && detailsFrame.getVisibility == View.VISIBLE
      if (savedInstanceState != null) {
        mCurCheckPosition = savedInstanceState.getInt("curChoice", 0)
      }
      if (mDualPane) {
        getListView.setChoiceMode(ListView_CHOICE_MODE_SINGLE)
        showDetails(mCurCheckPosition)
      }
    }

    override def onSaveInstanceState(outState: Bundle) {
      super.onSaveInstanceState(outState)
      outState.putInt("curChoice", mCurCheckPosition)
    }

    override def onListItemClick(l: ListView,
                                 v: View,
                                 position: Int,
                                 id: Long) {
      showDetails(position)
    }

    def showDetails(index: Int) {
      mCurCheckPosition = index
      if (mDualPane) {
        getListView.setItemChecked(index, true)
        var details = getFragmentManager.findFragmentById(R.id.details).asInstanceOf[DetailsFragment]
        if (details == null || details.getShownIndex != index) {
          details = DetailsFragment.newInstance(index)
          val ft = getFragmentManager.beginTransaction()
          ft.replace(R.id.details, details)
          ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
          ft.commit()
        }
      } else {
        val intent = new Intent()
        intent.setClass(getActivity, classOf[DetailsActivity])
        intent.putExtra("index", index)
        startActivity(intent)
      }
    }
  }

  object DetailsFragment {

    def newInstance(index: Int): DetailsFragment = {
      val f = new DetailsFragment()
      val args = new Bundle()
      args.putInt("index", index)
      f.setArguments(args)
      f
    }
  }

  class DetailsFragment extends Fragment {

    def getShownIndex(): Int = getArguments.getInt("index", 0)

    override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
      if (container == null) {
        return null
      }
      val scroller = new ScrollView(getActivity)
      val text = new TextView(getActivity)
      val padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getActivity.getResources.getDisplayMetrics).toInt
      text.setPadding(padding, padding, padding, padding)
      scroller.addView(text)
      text.setText(Shakespeare.DIALOGUE(getShownIndex))
      scroller
    }
  }
}

class FragmentLayout extends SActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.fragment_layout)
  }
}
