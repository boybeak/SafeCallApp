package com.hikingman.safecallapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.github.boybeak.safecall.enqueueSafetyWith

class MainActivity : AppCompatActivity() {

    private companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun go(v: View) {
        api().getHotTopics(0, 15)
            .enqueueSafetyWith(this)
            .onSuccess { call, response ->

            }
            .onFailure { call, t ->

            }
    }
}