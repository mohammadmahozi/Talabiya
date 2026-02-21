package com.mahozi.sayed.talabiya.core.datetime

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit


fun Instant.withDate(date: LocalDate): Instant = this
  .atZone(ZoneId.systemDefault())
  .with(date)
  .toInstant()
  .truncatedTo(ChronoUnit.SECONDS)

fun Instant.withTime(time: LocalTime): Instant = this
  .atZone(ZoneId.systemDefault())
  .with(time)
  .toInstant()
  .truncatedTo(ChronoUnit.SECONDS)

