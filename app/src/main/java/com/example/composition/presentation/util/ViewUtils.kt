package com.example.composition.presentation.util

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

class ViewUtils {
    companion object {
        inline fun <reified T : Parcelable> getParcelable(bundle: Bundle, key: String): T? {
            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                -> bundle.getParcelable(key, T::class.java)

                else -> {
                    @Suppress("DEPRECATION") bundle.getParcelable(key) as? T
                }
            }
        }
    }
}
