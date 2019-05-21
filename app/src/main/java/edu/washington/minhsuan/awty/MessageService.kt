package edu.washington.minhsuan.awty

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast


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
                try {
                    Toast.makeText(this@MessageService, "Sending SMS Message...", Toast.LENGTH_SHORT).show()
                    val smgr = SmsManager.getDefault() as SmsManager
                    smgr.sendTextMessage(phone, null, msg, null, null)
                    Toast.makeText(this@MessageService, "SMS Sent Successfully.\n$phone:$msg",
                        Toast.LENGTH_SHORT).show()
                } catch (e: Exception){
                    Log.v(TAG, "failed")
                    Log.e(TAG, "exception", e)
                    Toast.makeText(this@MessageService, "SMS Failed to Send, Please try again",
                        Toast.LENGTH_SHORT).show()
                }
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
