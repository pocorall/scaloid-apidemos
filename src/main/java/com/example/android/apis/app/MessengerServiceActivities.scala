package com.example.android.apis.app

import com.example.android.apis.R
import com.example.android.apis.app.LocalServiceActivities.Binding
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
//remove if not needed
import scala.collection.JavaConversions._

object MessengerServiceActivities {

  class Binding extends Activity {

    var mService: Messenger = null

    var mIsBound: Boolean = _

    var mCallbackText: TextView = _

    class IncomingHandler extends Handler {

      override def handleMessage(msg: Message) {
        msg.what match {
          case MessengerService.MSG_SET_VALUE => mCallbackText.setText("Received from service: " + msg.arg1)
          case _ => super.handleMessage(msg)
        }
      }

    }

    val mMessenger = new Messenger(new IncomingHandler())

    private var mConnection: ServiceConnection = new ServiceConnection() {

      def onServiceConnected(className: ComponentName, service: IBinder) {
        mService = new Messenger(service)
        mCallbackText.setText("Attached.")
        try {
          var msg = Message.obtain(null, MessengerService.MSG_REGISTER_CLIENT)
          msg.replyTo = mMessenger
          mService.send(msg)
          msg = Message.obtain(null, MessengerService.MSG_SET_VALUE, this.hashCode, 0)
          mService.send(msg)
        } catch {
          case e: RemoteException =>
        }
        Toast.makeText(Binding.this, R.string.remote_service_connected, Toast.LENGTH_SHORT)
          .show()
      }

      def onServiceDisconnected(className: ComponentName) {
        mService = null
        mCallbackText.setText("Disconnected.")
        Toast.makeText(Binding.this, R.string.remote_service_disconnected, Toast.LENGTH_SHORT)
          .show()
      }
    }

    def doBindService() {
      bindService(new Intent(Binding.this, classOf[MessengerService]), mConnection, Context.BIND_AUTO_CREATE)
      mIsBound = true
      mCallbackText.setText("Binding.")
    }

    def doUnbindService() {
      if (mIsBound) {
        if (mService != null) {
          try {
            val msg = Message.obtain(null, MessengerService.MSG_UNREGISTER_CLIENT)
            msg.replyTo = mMessenger
            mService.send(msg)
          } catch {
            case e: RemoteException =>
          }
        }
        unbindService(mConnection)
        mIsBound = false
        mCallbackText.setText("Unbinding.")
      }
    }

    protected override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.messenger_service_binding)
      var button = findViewById(R.id.bind).asInstanceOf[Button]
      button.setOnClickListener(mBindListener)
      button = findViewById(R.id.unbind).asInstanceOf[Button]
      button.setOnClickListener(mUnbindListener)
      mCallbackText = findViewById(R.id.callback).asInstanceOf[TextView]
      mCallbackText.setText("Not attached.")
    }

    private var mBindListener: OnClickListener = new OnClickListener() {

      def onClick(v: View) {
        doBindService()
      }
    }

    private var mUnbindListener: OnClickListener = new OnClickListener() {

      def onClick(v: View) {
        doUnbindService()
      }
    }
  }
}
