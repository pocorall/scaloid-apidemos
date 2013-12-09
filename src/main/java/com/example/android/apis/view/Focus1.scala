package com.example.android.apis.view

import com.example.android.apis.R
import android.app.Activity
import android.os.Bundle
import android.webkit.WebView
import android.widget.ListView
import android.widget.ArrayAdapter
//remove if not needed
import scala.collection.JavaConversions._

class Focus1 extends Activity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.focus_1)
    val webView = findViewById(R.id.rssWebView).asInstanceOf[WebView]
    webView.loadData("<html><body>Can I focus?<br /><a href=\"#\">No I cannot!</a>.</body></html>", "text/html",
      null)
    val listView = findViewById(R.id.rssListView).asInstanceOf[ListView]
    listView.setAdapter(new ArrayAdapter[String](this, android.R.layout.simple_list_item_1, Array("Ars Technica", "Slashdot", "GameKult")))
  }
}
