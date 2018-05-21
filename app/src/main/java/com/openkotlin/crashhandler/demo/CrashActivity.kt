package com.openkotlin.crashhandler.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import crashhandler.openkotlin.com.crashhandler.R
import java.text.SimpleDateFormat
import java.util.*

class CrashActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var tvTime: TextView
    private lateinit var tvErrorDetails: TextView
    private lateinit var btnCloseApp: Button
    private lateinit var btnDetails: Button
    private var isDetailsShowing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWidget()
        initEvent()
        initData()
    }

    override fun onResume() {
        super.onResume()
        window.decorView.systemUiVisibility = DemoApplication.ACTIVITY_VISIBILITY_OPTIONS
    }

    private fun initWidget() {
        tvTime = findViewById(R.id.tv_time)
        tvErrorDetails = findViewById(R.id.tv_details)
        btnCloseApp = findViewById(R.id.btn_close_app)
        btnDetails = findViewById(R.id.btn_details)
    }

    private fun initEvent() {
        btnCloseApp.setOnClickListener(this)
        btnDetails.setOnClickListener(this)
    }

    private fun initData() {
        tvTime.text = getCurrentTime()
        val errorInfo = intent.getStringExtra(DemoApplication.ERROR_INFO)
        tvErrorDetails.text = errorInfo
    }

    private fun getCurrentTime(): String {
        val simpleDateFormat = SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getDefault()
        return simpleDateFormat.format(Date())
    }

    override fun onClick(v: View?) {
        if (v == null) return
        when(v.id){
            R.id.btn_close_app -> finish()
            R.id.btn_details -> {
                isDetailsShowing = !isDetailsShowing
                btnDetails.setText(if(isDetailsShowing) R.string.hide_details else R.string.show_details)
                tvErrorDetails.visibility = if (isDetailsShowing) View.VISIBLE else View.GONE
            }
        }
    }

}