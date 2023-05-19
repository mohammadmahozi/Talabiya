package com.mahozi.sayed.talabiya

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.mahozi.sayed.talabiya.core.data.TalabiyaDatabase
import com.mahozi.sayed.talabiya.core.data.TalabiyaDatabase.Companion.MIGRATION_1_2
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId


@RunWith(AndroidJUnit4::class)
class Migrations {


  private val dbName = "migration-test"

  @get:Rule
  val helper: MigrationTestHelper = MigrationTestHelper(
    InstrumentationRegistry.getInstrumentation(),
    TalabiyaDatabase::class.java,
  )

  @Test
  @Throws(IOException::class)
  fun migrate1To2() {
    var db = helper.createDatabase(dbName, 1)
    val values = arrayOf(1, "2019/10/25, Fri", "21:56", 0, false)
    db.execSQL("INSERT INTO OrderEntity (id, date, time, order_total, status) VALUES(?, ?, ?, ?, ?)", values)
    db.close()

    db = helper.runMigrationsAndValidate(dbName, 2, false, MIGRATION_1_2)

    val expectedInstant = LocalDateTime.of(2019, Month.OCTOBER, 25, 21, 56)
      .atZone(ZoneId.of("Asia/Riyadh"))
      .toInstant()
      .toEpochMilli()

    val cursor = db.query("select datetime From OrderEntity")
    cursor.moveToFirst()
    val instant = cursor.getLong(0)

    assertThat(instant, equalTo(expectedInstant))
  }
}