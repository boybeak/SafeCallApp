package com.github.boybeak.safecall

import androidx.lifecycle.LifecycleOwner
import retrofit2.Call

fun <T> Call<T>.safeWith(owner: LifecycleOwner): SafeCall<T> {
    val safeCall = SafeCall(this)
    safeCall.observe(owner)
    return safeCall
}