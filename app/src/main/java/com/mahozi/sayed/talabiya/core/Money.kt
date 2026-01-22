package com.mahozi.sayed.talabiya.core

import java.math.BigDecimal
import java.text.DecimalFormat


data class Money constructor(private val amount: BigDecimal){

  private val format = DecimalFormat("#,###.00")

  operator fun plus(money: Money): Money = (this.amount + money.amount).money

  operator fun minus(money: Money): Money = (this.amount - money.amount).money

  operator fun compareTo(money: Money) = this.amount.compareTo(money.amount)

  fun format(): String = format.format(amount)

  fun toCents(): Long = (amount.movePointRight(2)).longValueExact()
}

val BigDecimal.money: Money get() = Money(this)
val Int.money: Money get() = Money(this.toBigDecimal())
val Double.money: Money get() = Money(this.toBigDecimal())
val String.money: Money get() = Money(this.toBigDecimal())

val Long.cents: Money get() = Money(BigDecimal.valueOf(this, 2))
val Double.cents: Money get() {
  val bigDecimal = this.toBigDecimal().stripTrailingZeros()
  if (bigDecimal.scale() > 0) {
   throw IllegalArgumentException("Invalid cents value ($this). Fractional cents are not allowed")
  }
  return Money(bigDecimal.movePointLeft(2))
}

inline fun <T> Iterable<T>.sumOf(selector: (T) -> Money): Money {
  var sum = 0.money
  for (element in this) {
    sum += selector(element)
  }
  return sum
}