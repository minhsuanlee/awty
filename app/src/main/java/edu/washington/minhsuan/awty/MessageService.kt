package edu.washington.minhsuan.awty

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.os.Handler
import android.util.Log
import android.widget.Toast
import java.nio.channels.InterruptedByTimeoutException


class MessageService : IntentService("MessageService") {
    private val TAG = "MessageService"

    private val mHandler = Handler()

    private var nagging = true

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Service created!")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.v(TAG, "Service being created")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleIntent(intent: Intent?) {
        var minute: Int
        var msg: String
        var phone: String
        intent?.extras.apply {
            minute = this!!.getInt("Time")
            msg = this!!.getString("Message")
            phone = this!!.getString("Phone")
        }

        while (nagging) {
            mHandler.post {
                Toast.makeText(this@MessageService, "$phone:$msg", Toast.LENGTH_LONG).show()
                Log.v(TAG, "Toasting...")
            }
            try {
                Thread.sleep((minute * 60  * 1000).toLong())
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
    }

    override fun onDestroy() {
        Log.v(TAG, "Service being destoryed")
        nagging = false
        super.onDestroy()
    }
}
