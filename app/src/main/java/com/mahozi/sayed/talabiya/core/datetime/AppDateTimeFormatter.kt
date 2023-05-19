package com.mahozi.sayed.talabiya.core.datetime

import android.app.Activity
import androidx.compose.runtime.staticCompositionLocalOf
import com.mahozi.sayed.talabiya.core.extensions.locale
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import javax.inject.Inject

val LocalDateTimeFormatter = staticCompositionLocalOf<AppDateTimeFormatter> {
  error("AppLocalDateTimeFormatter is not provider")
}

class AppDateTimeFormatter @Inject constructor(
  private val context: Activity
) {

  private val zoneId = ZoneId.systemDefault()

  private val shortDateTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern(" yyyy/MM/dd - hh:mm a", context.locale)
      .withZone(zoneId)

  private val shortDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy/MM/dd", context.locale)
      .withZone(zoneId)

  private val shortDateWithDayFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("EEEE yyyy/MM/dd", context.locale)
      .withZone(zoneId)

  private val timeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("hh:mm a", context.locale)
      .withZone(zoneId)

  /**
   * Format date to 2022/01/24
   */
  fun formatShortDate(temporal: TemporalAccessor): String = shortDateFormatter.format(temporal)

  /**
   * Format date to Sunday 2022/01/24
   */
  fun formatShortDateWithDay(temporal: TemporalAccessor): String = shortDateWithDayFormatter.format(temporal)

  /**
   * Format time to 02:30 PM
   */
  fun formatTime(temporal: TemporalAccessor): String = timeFormatter.format(temporal)

  /**
   * Format date time to 2022/01/24 - 12:00 PM
   */
  fun formatShortDateTime(temporal: TemporalAccessor): String = shortDateTimeFormatter.format(temporal)

  /**
   * Format date and time to 2022/01/24 - 12:00 PM
   */
  fun formatShortDateTime(date: TemporalAccessor, time: TemporalAccessor): String =
    "${formatShortDate(date)} - ${formatTime(time)}"
}