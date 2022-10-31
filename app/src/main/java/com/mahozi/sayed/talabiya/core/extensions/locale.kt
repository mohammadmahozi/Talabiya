package com.mahozi.sayed.talabiya.core.extensions

import android.content.Context
import android.os.Build
import java.util.*

@Suppress("DEPRECATION")
val Context.locale: Locale
    get() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales.get(0)
        } else {
            resources.configuration.locale
        }