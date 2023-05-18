package com.mahozi.sayed.talabiya.core.data

import androidx.room.TypeConverter
import java.time.Instant


class InstantConverter {

  @TypeConverter
  fun longToInstant(value: Long?): Instant? = value?.let { Instant.ofEpochMilli(value) }

  @TypeConverter
  fun instantToLong(instant: Instant?): Long? = instant?.toEpochMilli()
}