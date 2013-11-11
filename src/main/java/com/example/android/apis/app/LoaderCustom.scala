package com.example.android.apis.app

import com.example.android.apis.R
import java.io.File
import java.text.Collator
import java.util.ArrayList
import java.util.Collections
import java.util.Comparator
import java.util.List
import android.app.Activity
import android.app.FragmentManager
import android.app.ListFragment
import android.app.LoaderManager
import android.content.AsyncTaskLoader
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.Loader
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import android.widget.SearchView.OnQueryTextListener
import LoaderCustom._
import org.scaloid.common._


object LoaderCustom {

  class AppEntry(private val mLoader: AppListLoader, private val mInfo: ApplicationInfo)
  {

    def getApplicationInfo(): ApplicationInfo = mInfo

    def getLabel(): String = mLabel

    def getIcon(): Drawable = {
      if (mIcon == null) {
        if (mApkFile.exists()) {
          mIcon = mInfo.loadIcon(mLoader.mPm)
          return mIcon
        } else {
          mMounted = false
        }
      } else if (!mMounted) {
        if (mApkFile.exists()) {
          mMounted = true
          mIcon = mInfo.loadIcon(mLoader.mPm)
          return mIcon
        }
      } else {
        return mIcon
      }
      mLoader.getContext.getResources.getDrawable(android.R.drawable.sym_def_app_icon)
    }

    override def toString(): String = mLabel

    def loadLabel(context: Context) {
      if (mLabel == null || !mMounted) {
        if (!mApkFile.exists()) {
          mMounted = false
          mLabel = mInfo.packageName
        } else {
          mMounted = true
          val label = mInfo.loadLabel(context.getPackageManager)
          mLabel = if (label != null) label.toString else mInfo.packageName
        }
      }
    }

    private val mApkFile = new File(mInfo.sourceDir)

    private var mLabel: String = _

    private var mIcon: Drawable = _

    private var mMounted: Boolean = _
  }

  val ALPHA_COMPARATOR = new Comparator[AppEntry]() {

    private val sCollator = Collator.getInstance

    override def compare(object1: AppEntry, object2: AppEntry): Int = {
      sCollator.compare(object1.getLabel, object2.getLabel)
    }
  }

  class InterestingConfigChanges {

    val mLastConfiguration = new Configuration()

    var mLastDensity: Int = _

    def applyNewConfig(res: Resources): Boolean = {
      val configChanges = mLastConfiguration.updateFrom(res.getConfiguration)
      val densityChanged = mLastDensity != res.getDisplayMetrics.densityDpi
      if (densityChanged ||
        (configChanges &
          (ActivityInfo.CONFIG_LOCALE | ActivityInfo.CONFIG_UI_MODE |
            ActivityInfo.CONFIG_SCREEN_LAYOUT)) !=
          0) {
        mLastDensity = res.getDisplayMetrics.densityDpi
        return true
      }
      false
    }
  }

  class PackageIntentReceiver(val mLoader: AppListLoader) extends BroadcastReceiver {

    val filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED)

    filter.addAction(Intent.ACTION_PACKAGE_REMOVED)

    filter.addAction(Intent.ACTION_PACKAGE_CHANGED)

    filter.addDataScheme("package")

    mLoader.getContext.registerReceiver(this, filter)

    val sdFilter = new IntentFilter()

    sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE)

    sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE)

    mLoader.getContext.registerReceiver(this, sdFilter)

    override def onReceive(context: Context, intent: Intent) {
      mLoader.onContentChanged()
    }
  }

  class AppListLoader(context: Context) extends AsyncTaskLoader[List[AppEntry]](context) {

    val mLastConfig = new InterestingConfigChanges()

    val mPm = getContext.getPackageManager

    var mApps: List[AppEntry] = _

    var mPackageObserver: PackageIntentReceiver = _

    override def loadInBackground(): List[AppEntry] = {
      var apps = mPm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_DISABLED_COMPONENTS)
      if (apps == null) {
        apps = new ArrayList[ApplicationInfo]()
      }
      val context = getContext
      val entries = new ArrayList[AppEntry](apps.size)
      for (i <- 0 until apps.size) {
        val entry = new AppEntry(this, apps.get(i))
        entry.loadLabel(context)
        entries.add(entry)
      }
      Collections.sort(entries, ALPHA_COMPARATOR)
      entries
    }

    override def deliverResult(apps: List[AppEntry]) {
      if (isReset) {
        if (apps != null) {
          onReleaseResources(apps)
        }
      }
      val oldApps = apps
      mApps = apps
      if (isStarted) {
        super.deliverResult(apps)
      }
      if (oldApps != null) {
        onReleaseResources(oldApps)
      }
    }

    protected override def onStartLoading() {
      if (mApps != null) {
        deliverResult(mApps)
      }
      if (mPackageObserver == null) {
        mPackageObserver = new PackageIntentReceiver(this)
      }
      val configChange = mLastConfig.applyNewConfig(getContext.getResources)
      if (takeContentChanged() || mApps == null || configChange) {
        forceLoad()
      }
    }

    protected override def onStopLoading() {
      cancelLoad()
    }

    override def onCanceled(apps: List[AppEntry]) {
      super.onCanceled(apps)
      onReleaseResources(apps)
    }

    protected override def onReset() {
      super.onReset()
      onStopLoading()
      if (mApps != null) {
        onReleaseResources(mApps)
        mApps = null
      }
      if (mPackageObserver != null) {
        getContext.unregisterReceiver(mPackageObserver)
        mPackageObserver = null
      }
    }

    protected def onReleaseResources(apps: List[AppEntry]) {
    }
  }

  class AppListAdapter(context: Context) extends ArrayAdapter[AppEntry](context, android.R.layout.simple_list_item_2) {

    private val mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]

    def setData(data: List[AppEntry]) {
      clear()
      if (data != null) {
        addAll(data)
      }
    }

    override def getView(position: Int, convertView: View, parent: ViewGroup): View = {
      var view: View = null
      view = if (convertView == null) mInflater.inflate(R.layout.list_item_icon_text, parent, false) else convertView
      val item = getItem(position)
      view.findViewById(R.id.icon).asInstanceOf[ImageView]
        .setImageDrawable(item.getIcon)
      view.findViewById(R.id.text).asInstanceOf[TextView]
        .setText(item.getLabel)
      view
    }
  }

  class AppListFragment extends ListFragment with OnQueryTextListener with LoaderManager.LoaderCallbacks[List[AppEntry]] {

    var mAdapter: AppListAdapter = _

    var mCurFilter: String = _

    override def onActivityCreated(savedInstanceState: Bundle) {
      super.onActivityCreated(savedInstanceState)
      setEmptyText("No applications")
      setHasOptionsMenu(true)
      mAdapter = new AppListAdapter(getActivity)
      setListAdapter(mAdapter)
      setListShown(false)
      getLoaderManager.initLoader(0, null, this)
    }

    override def onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
      val item = menu.add("Search")
      item.setIcon(android.R.drawable.ic_menu_search)
      item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
      val sv = new SearchView(getActivity)
      sv.setOnQueryTextListener(this)
      item.setActionView(sv)
    }

    override def onQueryTextChange(newText: String): Boolean = {
      mCurFilter = if (!TextUtils.isEmpty(newText)) newText else null
      mAdapter.getFilter.filter(mCurFilter)
      true
    }

    override def onQueryTextSubmit(query: String): Boolean = true

    override def onListItemClick(l: ListView,
                                 v: View,
                                 position: Int,
                                 id: Long) {
      Log.i("LoaderCustom", "Item clicked: " + id)
    }

    override def onCreateLoader(id: Int, args: Bundle): Loader[List[AppEntry]] = new AppListLoader(getActivity)

    override def onLoadFinished(loader: Loader[List[AppEntry]], data: List[AppEntry]) {
      mAdapter.setData(data)
      if (isResumed) {
        setListShown(true)
      } else {
        setListShownNoAnimation(true)
      }
    }

    override def onLoaderReset(loader: Loader[List[AppEntry]]) {
      mAdapter.setData(null)
    }
  }
}

class LoaderCustom extends SActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    val fm = getFragmentManager
    if (fm.findFragmentById(android.R.id.content) == null) {
      val list = new AppListFragment()
      fm.beginTransaction().add(android.R.id.content, list)
        .commit()
    }
  }
}
