package com.example.android.apis.text

import com.example.android.apis.R
import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.widget.TextView
import org.scaloid.common._


class Link extends SActivity {

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.link)
    val t2 = findViewById(R.id.text2).asInstanceOf[TextView]
    t2.setMovementMethod(LinkMovementMethod.getInstance)
    val t3 = findViewById(R.id.text3).asInstanceOf[TextView]
    t3.setText(Html.fromHtml("<b>text3: Constructed from HTML programmatically.</b>  Text with a " +
      "<a href=\"http://www.google.com\">link</a> " +
      "created in the Java source code using HTML."))
    t3.setMovementMethod(LinkMovementMethod.getInstance)
    val ss = new SpannableString("text4: Manually created spans. Click here to dial the phone.")
    ss.setSpan(new StyleSpan(Typeface.BOLD), 0, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    ss.setSpan(new URLSpan("tel:4155551212"), 31 + 6, 31 + 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    val t4 = findViewById(R.id.text4).asInstanceOf[TextView]
    t4.setText(ss)
    t4.setMovementMethod(LinkMovementMethod.getInstance)
  }
}
