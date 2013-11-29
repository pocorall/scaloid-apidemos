package com.example.android.apis.text

import com.example.android.apis.R
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import org.scaloid.common._

//remove if not needed
import scala.collection.JavaConversions._
import android.view.ViewGroup.LayoutParams


class LogTextBox1 extends Activity {

  private var mText: LogTextBox = _

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.log_text_box_1)
    mText = findViewById(R.id.text).asInstanceOf[LogTextBox]
    val addButton = findViewById(R.id.add).asInstanceOf[Button]
    addButton.setOnClickListener(new View.OnClickListener() {

      def onClick(v: View) {
        mText.append("This is a test\n")
      }
    })
  }
}
