package com.example.android.apis.app

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import android.database.Cursor
import android.provider.ContactsContract
import com.example.android.apis.R
import AlertDialogSamples._
import org.scaloid.common._

//remove if not needed
import scala.collection.JavaConversions._

object AlertDialogSamples {
  // Scaloid >>
  val ContactsContract_Contacts__ID = android.provider.BaseColumns._ID
  val ContactsContract_Contacts_DISPLAY_NAME = "display_name" // android.provider.ContactsContract
  val ContactsContract_Contacts_SEND_TO_VOICEMAIL = "send_to_voicemail"
  // Scaloid <<

  private val DIALOG_YES_NO_MESSAGE = 1

  private val DIALOG_YES_NO_LONG_MESSAGE = 2

  private val DIALOG_LIST = 3

  private val DIALOG_PROGRESS = 4

  private val DIALOG_SINGLE_CHOICE = 5

  private val DIALOG_MULTIPLE_CHOICE = 6

  private val DIALOG_TEXT_ENTRY = 7

  private val DIALOG_MULTIPLE_CHOICE_CURSOR = 8

  private val DIALOG_YES_NO_ULTRA_LONG_MESSAGE = 9

  private val DIALOG_YES_NO_OLD_SCHOOL_MESSAGE = 10

  private val DIALOG_YES_NO_HOLO_LIGHT_MESSAGE = 11

  private val MAX_PROGRESS = 100
}

class AlertDialogSamples extends SActivity {

  private var mProgressDialog: ProgressDialog = _

  private var mProgress: Int = _

  private var mProgressHandler: Handler = _

  protected override def onCreateDialog(id: Int): Dialog = id match {
    case DIALOG_YES_NO_MESSAGE => new AlertDialog.Builder(AlertDialogSamples.this).setIconAttribute(android.R.attr.alertDialogIcon)
      .setTitle(R.string.alert_dialog_two_buttons_title)
      .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {

      def onClick(dialog: DialogInterface, whichButton: Int) {
      }
    })
      .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {

      def onClick(dialog: DialogInterface, whichButton: Int) {
      }
    })
      .create()
    case DIALOG_YES_NO_OLD_SCHOOL_MESSAGE => new AlertDialog.Builder(AlertDialogSamples.this, AlertDialog.THEME_TRADITIONAL)
      .setIconAttribute(android.R.attr.alertDialogIcon)
      .setTitle(R.string.alert_dialog_two_buttons_title)
      .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {

      def onClick(dialog: DialogInterface, whichButton: Int) {
      }
    })
      .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {

      def onClick(dialog: DialogInterface, whichButton: Int) {
      }
    })
      .create()
    case DIALOG_YES_NO_HOLO_LIGHT_MESSAGE => new AlertDialog.Builder(AlertDialogSamples.this, AlertDialog.THEME_HOLO_LIGHT)
      .setIconAttribute(android.R.attr.alertDialogIcon)
      .setTitle(R.string.alert_dialog_two_buttons_title)
      .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {

      def onClick(dialog: DialogInterface, whichButton: Int) {
      }
    })
      .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {

      def onClick(dialog: DialogInterface, whichButton: Int) {
      }
    })
      .create()
    case DIALOG_YES_NO_LONG_MESSAGE => new AlertDialog.Builder(AlertDialogSamples.this).setIconAttribute(android.R.attr.alertDialogIcon)
      .setTitle(R.string.alert_dialog_two_buttons_msg)
      .setMessage(R.string.alert_dialog_two_buttons2_msg)
      .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {

      def onClick(dialog: DialogInterface, whichButton: Int) {
      }
    })
      .setNeutralButton(R.string.alert_dialog_something, new DialogInterface.OnClickListener() {

      def onClick(dialog: DialogInterface, whichButton: Int) {
      }
    })
      .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {

      def onClick(dialog: DialogInterface, whichButton: Int) {
      }
    })
      .create()
    case DIALOG_YES_NO_ULTRA_LONG_MESSAGE => new AlertDialog.Builder(AlertDialogSamples.this).setIconAttribute(android.R.attr.alertDialogIcon)
      .setTitle(R.string.alert_dialog_two_buttons_msg)
      .setMessage(R.string.alert_dialog_two_buttons2ultra_msg)
      .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {

      def onClick(dialog: DialogInterface, whichButton: Int) {
      }
    })
      .setNeutralButton(R.string.alert_dialog_something, new DialogInterface.OnClickListener() {

      def onClick(dialog: DialogInterface, whichButton: Int) {
      }
    })
      .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {

      def onClick(dialog: DialogInterface, whichButton: Int) {
      }
    })
      .create()
    case DIALOG_LIST => new AlertDialog.Builder(AlertDialogSamples.this).setTitle(R.string.select_dialog)
      .setItems(R.array.select_dialog_items, new DialogInterface.OnClickListener() {

      def onClick(dialog: DialogInterface, which: Int) {
        val items = getResources.getStringArray(R.array.select_dialog_items)
        new AlertDialog.Builder(AlertDialogSamples.this).setMessage("You selected: " + which + " , " + items(which))
          .show()
      }
    })
      .create()
    case DIALOG_PROGRESS =>
      mProgressDialog = new ProgressDialog(AlertDialogSamples.this)
      mProgressDialog.setIconAttribute(android.R.attr.alertDialogIcon)
      mProgressDialog.setTitle(R.string.select_dialog)
      mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
      mProgressDialog.setMax(MAX_PROGRESS)
      mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE, getText(R.string.alert_dialog_hide),
        new DialogInterface.OnClickListener() {

          def onClick(dialog: DialogInterface, whichButton: Int) {
          }
        })
      mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getText(R.string.alert_dialog_cancel),
        new DialogInterface.OnClickListener() {

          def onClick(dialog: DialogInterface, whichButton: Int) {
          }
        })
      mProgressDialog

    case DIALOG_SINGLE_CHOICE => new AlertDialog.Builder(AlertDialogSamples.this).setIconAttribute(android.R.attr.alertDialogIcon)
      .setTitle(R.string.alert_dialog_single_choice)
      .setSingleChoiceItems(R.array.select_dialog_items2, 0, new DialogInterface.OnClickListener() {

      def onClick(dialog: DialogInterface, whichButton: Int) {
      }
    })
      .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {

      def onClick(dialog: DialogInterface, whichButton: Int) {
      }
    })
      .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {

      def onClick(dialog: DialogInterface, whichButton: Int) {
      }
    })
      .create()
    case DIALOG_MULTIPLE_CHOICE => new AlertDialog.Builder(AlertDialogSamples.this).setIcon(R.drawable.ic_popup_reminder)
      .setTitle(R.string.alert_dialog_multi_choice)
      .setMultiChoiceItems(R.array.select_dialog_items3, Array(false, true, false, true, false, false, false),
      new DialogInterface.OnMultiChoiceClickListener() {

        def onClick(dialog: DialogInterface, whichButton: Int, isChecked: Boolean) {
        }
      })
      .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {

      def onClick(dialog: DialogInterface, whichButton: Int) {
      }
    })
      .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {

      def onClick(dialog: DialogInterface, whichButton: Int) {
      }
    })
      .create()
    case DIALOG_MULTIPLE_CHOICE_CURSOR =>
      val projection = Array(ContactsContract_Contacts__ID, ContactsContract_Contacts_DISPLAY_NAME, ContactsContract_Contacts_SEND_TO_VOICEMAIL)
      val cursor = managedQuery(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null)
      new AlertDialog.Builder(AlertDialogSamples.this).setIcon(R.drawable.ic_popup_reminder)
        .setTitle(R.string.alert_dialog_multi_choice_cursor)
        .setMultiChoiceItems(cursor, ContactsContract_Contacts_SEND_TO_VOICEMAIL, ContactsContract_Contacts_DISPLAY_NAME,
        new DialogInterface.OnMultiChoiceClickListener() {

          def onClick(dialog: DialogInterface, whichButton: Int, isChecked: Boolean) {
            Toast.makeText(AlertDialogSamples.this, "Readonly Demo Only - Data will not be updated", Toast.LENGTH_SHORT)
              .show()
          }
        })
        .create()

    case DIALOG_TEXT_ENTRY =>
      var factory = LayoutInflater.from(this)
      val textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null)
      new AlertDialog.Builder(AlertDialogSamples.this).setIconAttribute(android.R.attr.alertDialogIcon)
        .setTitle(R.string.alert_dialog_text_entry)
        .setView(textEntryView)
        .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {

        def onClick(dialog: DialogInterface, whichButton: Int) {
        }
      })
        .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {

        def onClick(dialog: DialogInterface, whichButton: Int) {
        }
      })
        .create()

  }

  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.alert_dialog)
    val twoButtonsTitle = findViewById(R.id.two_buttons).asInstanceOf[Button]
    twoButtonsTitle.setOnClickListener(new OnClickListener() {

      def onClick(v: View) {
        showDialog(DIALOG_YES_NO_MESSAGE)
      }
    })
    val twoButtons2Title = findViewById(R.id.two_buttons2).asInstanceOf[Button]
    twoButtons2Title.setOnClickListener(new OnClickListener() {

      def onClick(v: View) {
        showDialog(DIALOG_YES_NO_LONG_MESSAGE)
      }
    })
    val twoButtons2UltraTitle = findViewById(R.id.two_buttons2ultra).asInstanceOf[Button]
    twoButtons2UltraTitle.setOnClickListener(new OnClickListener() {

      def onClick(v: View) {
        showDialog(DIALOG_YES_NO_ULTRA_LONG_MESSAGE)
      }
    })
    val selectButton = findViewById(R.id.select_button).asInstanceOf[Button]
    selectButton.setOnClickListener(new OnClickListener() {

      def onClick(v: View) {
        showDialog(DIALOG_LIST)
      }
    })
    val progressButton = findViewById(R.id.progress_button).asInstanceOf[Button]
    progressButton.setOnClickListener(new OnClickListener() {

      def onClick(v: View) {
        showDialog(DIALOG_PROGRESS)
        mProgress = 0
        mProgressDialog.setProgress(0)
        mProgressHandler.sendEmptyMessage(0)
      }
    })
    val radioButton = findViewById(R.id.radio_button).asInstanceOf[Button]
    radioButton.setOnClickListener(new OnClickListener() {

      def onClick(v: View) {
        showDialog(DIALOG_SINGLE_CHOICE)
      }
    })
    val checkBox = findViewById(R.id.checkbox_button).asInstanceOf[Button]
    checkBox.setOnClickListener(new OnClickListener() {

      def onClick(v: View) {
        showDialog(DIALOG_MULTIPLE_CHOICE)
      }
    })
    val checkBox2 = findViewById(R.id.checkbox_button2).asInstanceOf[Button]
    checkBox2.setOnClickListener(new OnClickListener() {

      def onClick(v: View) {
        showDialog(DIALOG_MULTIPLE_CHOICE_CURSOR)
      }
    })
    val textEntry = findViewById(R.id.text_entry_button).asInstanceOf[Button]
    textEntry.setOnClickListener(new OnClickListener() {

      def onClick(v: View) {
        showDialog(DIALOG_TEXT_ENTRY)
      }
    })
    val twoButtonsOldSchoolTitle = findViewById(R.id.two_buttons_old_school).asInstanceOf[Button]
    twoButtonsOldSchoolTitle.setOnClickListener(new OnClickListener() {

      def onClick(v: View) {
        showDialog(DIALOG_YES_NO_OLD_SCHOOL_MESSAGE)
      }
    })
    val twoButtonsHoloLightTitle = findViewById(R.id.two_buttons_holo_light).asInstanceOf[Button]
    twoButtonsHoloLightTitle.setOnClickListener(new OnClickListener() {

      def onClick(v: View) {
        showDialog(DIALOG_YES_NO_HOLO_LIGHT_MESSAGE)
      }
    })
    mProgressHandler = new Handler() {

      override def handleMessage(msg: Message) {
        super.handleMessage(msg)
        if (mProgress >= MAX_PROGRESS) {
          mProgressDialog.dismiss()
        } else {
          mProgress += 1
          mProgressDialog.incrementProgressBy(1)
          mProgressHandler.sendEmptyMessageDelayed(0, 100)
        }
      }
    }
  }
}
