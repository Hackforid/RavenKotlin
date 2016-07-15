package com.smilehacker.raven.util

/**
 * Created by kleist on 16/7/15.
 */
interface Callback<T> {
    fun onResult(result: T? = null)
    fun onError(e: Exception)
}
