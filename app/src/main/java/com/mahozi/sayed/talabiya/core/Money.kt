package com.mahozi.sayed.talabiya.core

import java.math.BigDecimal
import java.text.DecimalFormat


data class Money constructor(private val amount: BigDecimal){

  private val format = DecimalFormat("#,###.00")

  operator fun plus(money: Money): Money = (this.amount + money.amount).money

  operator fun minus(money: Money): Money = (this.amount - money.amount).money

  operator fun times(money: Money): Money = (this.amount * money.amount).money

  operator fun div(money: Money): Money = (this.amount / money.amount).money

  operator fun compareTo(money: Money) = this.amount.compareTo(money.amount)

  fun format(): String = format.format(amount)

  fun toLong(): Long = (amount * BigDecimal.valueOf(100)).longValueExact()
}

val Number.money: Money get() = Money(this.toDouble().toBigDecimal())

val String.money: Money get() = Money(this.toBigDecimal())

inline fun <T> Iterable<T>.sumOf(selector: (T) -> Money): Money {
  var sum = 0.money
  for (element in this) {
    sum += selector(element)
  }
  return sum
}