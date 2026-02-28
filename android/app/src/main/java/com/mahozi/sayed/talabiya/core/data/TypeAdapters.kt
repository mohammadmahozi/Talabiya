package com.mahozi.sayed.talabiya.core.data

import app.cash.sqldelight.ColumnAdapter
import java.time.Instant


object TypeAdapters {
  val instantAdapter = object : ColumnAdapter<Instant, Long> {

    override fun decode(databaseValue: Long): Instant = Instant.ofEpochSecond(databaseValue)

    override fun encode(value: Instant): Long = value.epochSecond

  }
}
