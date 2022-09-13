package com.mahozi.sayed.talabiya.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface RootRoute: Parcelable {

    @Parcelize object OrderRoot: RootRoute
}