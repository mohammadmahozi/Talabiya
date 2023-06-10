package com.mahozi.sayed.talabiya.core.data

import androidx.room.TypeConverter
import app.cash.sqldelight.ColumnAdapter
import java.time.Instant


class InstantConverter {

  @TypeConverter
  fun longToInstant(value: Long?): Instant? = value?.let { Instant.ofEpochMilli(value) }

  @TypeConverter
  fun instantToLong(instant: Instant?): Long? = instant?.toEpochMilli()
}

object TypeAdapters {
  val instantAdapter = object : ColumnAdapter<Instant, Long> {

    override fun decode(databaseValue: Long): Instant = Instant.ofEpochSecond(databaseValue)

    override fun encode(value: Instant): Long = value.epochSecond

  }
}
