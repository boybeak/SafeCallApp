package com.hikingman.safecallapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {

    private companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun go(v: View) {
        api().getHotTopics(0, 15).onSuccess { _, response ->
//            Log.v(TAG, "response.size=${response.body()!!.size}")
        }.observe(this)
    }
}