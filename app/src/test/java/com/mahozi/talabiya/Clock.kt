package com.mahozi.talabiya

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

val clock = Clock.fixed(Instant.now(), ZoneId.systemDefault())