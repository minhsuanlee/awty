package edu.washington.minhsuan.awty

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.text.InputFilter



class MainActivity : AppCompatActivity() {
    private val TAG = "Main"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}
