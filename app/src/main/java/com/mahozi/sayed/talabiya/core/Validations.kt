package com.mahozi.sayed.talabiya.core

import java.util.regex.Pattern

val decimalPattern = Pattern.compile("^([0-9]+\\.?[0-9]*)?$")

fun String.isDecimal(): Boolean = decimalPattern.matcher(this).matches()
