package com.openkotlin.crashhandler.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import com.openkotlin.crashhandler.R

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var btnCrash: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnCrash = findViewById(R.id.btn_crash)
        btnCrash.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        window.decorView.systemUiVisibility = DemoApplication.ACTIVITY_VISIBILITY_OPTIONS
    }

    override fun onClick(v: View?) {
        var a = 1/0
    }
}
