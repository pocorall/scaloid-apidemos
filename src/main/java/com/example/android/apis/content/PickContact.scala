package com.example.android.apis.content

import com.example.android.apis.R
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.SystemClock
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.ContactsContract
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import java.util.Calendar
import org.scaloid.common._

//remove if not needed
import scala.collection.JavaConversions._

class PickContact extends SActivity {

  var mToast: Toast = _

  var mPendingResult: ResultDisplayer = _

  class ResultDisplayer(var mMsg: String, var mMimeType: String) extends OnClickListener {

    def onClick(v: View) {
      val intent = new Intent(Intent.ACTION_GET_CONTENT)
      intent.setType(mMimeType)
      mPendingResult = this
      startActivityForResult(intent, 1)
    }
  }

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.pick_contact)
    findViewById(R.id.pick_contact).asInstanceOf[Button]
      .setOnClickListener(new ResultDisplayer("Selected contact", ContactsContract.Contacts.CONTENT_ITEM_TYPE))
    findViewById(R.id.pick_person).asInstanceOf[Button]
      .setOnClickListener(new ResultDisplayer("Selected person", "vnd.android.cursor.item/person"))
    findViewById(R.id.pick_phone).asInstanceOf[Button].setOnClickListener(new ResultDisplayer("Selected phone",
      ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE))
    findViewById(R.id.pick_address).asInstanceOf[Button]
      .setOnClickListener(new ResultDisplayer("Selected address", ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE))
  }

  protected override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    if (data != null) {
      val uri = data.getData
      if (uri != null) {
        var c: Cursor = null
        try {
          c = getContentResolver.query(uri, Array(BaseColumns._ID), null, null, null)
          if (c != null && c.moveToFirst()) {
            val id = c.getInt(0)
            if (mToast != null) {
              mToast.cancel()
            }
            val txt = mPendingResult.mMsg + ":\n" + uri + "\nid: " + id
            mToast = Toast.makeText(this, txt, Toast.LENGTH_LONG)
            mToast.show()
          }
        } finally {
          if (c != null) {
            c.close()
          }
        }
      }
    }
  }
}
