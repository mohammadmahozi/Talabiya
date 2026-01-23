package com.mahozi.talabiya.core

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.mahozi.sayed.talabiya.core.sumOf
import com.mahozi.sayed.talabiya.core.cents
import com.mahozi.sayed.talabiya.core.money
import org.junit.Test

class MoneyTest {

  @Test
  fun creation() {
    assertThat(100L.cents).equals(1.money)
    assertThat(200.0.cents).equals(2.money)
    assertThat(3000.0000.cents).equals(30.money)
    assertFailure { 100.5.cents }
  }

  @Test
  fun operations() {
    assertThat(1.money + 2.money).equals(3.money)
    assertThat(3.money - 2.money).equals(1.money)
    assertThat(5.money > 3.money).isTrue()
    assertThat(3.money < 5.money).isTrue()
    val sum = listOf(1.money, 2.money, 3.money).sumOf { it }
    assertThat(sum).isEqualTo(6.money)
  }

  @Test
  fun formatting() {
    assertThat(1.money.format()).isEqualTo("1")
    assertThat(1.0.money.format()).isEqualTo("1")
    assertThat(1.5.money.format()).isEqualTo("1.5")
    assertThat(1.50.money.format()).isEqualTo("1.5")
    assertThat(1.57.money.format()).isEqualTo("1.57")
    assertThat(1.571.money.format()).isEqualTo("1.57")
    assertThat(1.579.money.format()).isEqualTo("1.58")
    assertThat(0.money.format()).isEqualTo("0")
    assertThat(0.5.money.format()).isEqualTo("0.5")
    assertThat(1000.money.format()).isEqualTo("1,000")
  }
}