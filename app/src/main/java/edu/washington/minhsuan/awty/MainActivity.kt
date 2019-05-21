package edu.washington.minhsuan.awty

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.Manifest.permission.SEND_SMS
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager


class MainActivity : AppCompatActivity() {
    private val TAG = "Main"
    private val MY_PERMISSIONS_REQUEST_SEND_SMS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkForSmsPermission()

        val editMsg = findViewById<EditText>(R.id.etxtMsg)
        val editPhone = findViewById<EditText>(R.id.etxtPhone)
        val editNag = findViewById<EditText>(R.id.etxtNag)
        val btn = findViewById<Button>(R.id.btnStart)
        val btnClear = findViewById<Button>(R.id.btnClear)

        btn.setOnClickListener {
            var error = ""
            if (etxtMsg.text.isEmpty()) { error = "$error Please enter a Message;\n"}
            if (etxtNag.text.isEmpty()) { error = "$error Please enter a nag time;\n"}
            if (etxtPhone.text.isEmpty() || etxtPhone.text.length < 10) {
                error = "$error Please enter a valid phone number;"
            }

            if (error.isNotEmpty()) {
                Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
            } else {
                if (btn.text == "Start") {
                    btn.text = "Stop"
                    val time = Integer.parseInt(editNag.text.toString())
                    val msg = editMsg.text.toString()
                    var phone = editPhone.text.toString()
                    phone = formatPhoneNumber(phone)
                    handleStart(time, msg, phone)
                } else {
                    btn.text = "Start"
                    handleStop()
                }
            }
        }

        btnClear.setOnClickListener {
            editMsg.setText("")
            editNag.setText("")
            editPhone.setText("")
        }
    }

    private fun handleStart(minutes: Int, msg: String, phone: String) {
        Log.i(TAG, "Start pressed")
        btnClear.isEnabled = false
        val intent = Intent(this@MainActivity, MessageService::class.java)
        intent.putExtra("Time", minutes)
        intent.putExtra("Message", msg)
        intent.putExtra("Phone", phone)
        startService(intent)
    }

    private fun handleStop() {
        Log.i(TAG, "Stop pressed")
        btnClear.isEnabled = true
        stopService(Intent(this@MainActivity, MessageService::class.java))
    }

    private fun formatPhoneNumber(phone: String): String {
        return "(${phone.substring(0..2)}) ${phone.substring(3..5)}-${phone.substring(6)}"
    }

    private fun checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this, SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, getString(R.string.permission_not_granted))
            ActivityCompat.requestPermissions(this, arrayOf(SEND_SMS), MY_PERMISSIONS_REQUEST_SEND_SMS)
        }
    }
}
