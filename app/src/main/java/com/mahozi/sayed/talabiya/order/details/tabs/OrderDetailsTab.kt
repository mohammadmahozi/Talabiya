package com.mahozi.sayed.talabiya.order.details.tabs

import java.lang.IllegalArgumentException

enum class OrderDetailsTab {
    INFO,
    SUBORDERS,
    FULL;

    companion object {
        fun tab(index: Int) =
            when(index) {
                0 -> INFO
                1 -> SUBORDERS
                2 -> FULL
                else -> throw IllegalArgumentException("Max tab index is 2 but got $index")
            }
    }

}