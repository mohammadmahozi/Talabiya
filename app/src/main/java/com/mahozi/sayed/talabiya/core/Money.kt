package com.mahozi.sayed.talabiya.core

import java.math.BigDecimal
import java.text.DecimalFormat


data class Money constructor(private val amount: BigDecimal){

  private val format = DecimalFormat("#,###.00")

  operator fun plus(money: Money): Money = (this.amount + money.amount).money

  operator fun minus(money: Money): Money = (this.amount - money.amount).money

  operator fun times(money: Money): Money = (this.amount * money.amount).money

  operator fun div(money: Money): Money = (this.amount / money.amount).money

  fun format(): String = format.format(amount)

  fun toLong(): Long = (amount * BigDecimal.valueOf(100)).longValueExact()
}

val Number.money: Money get() = Money(this.toDouble().toBigDecimal())

val String.money: Money get() = Money(this.toBigDecimal())

val Cent.money: Money get() {
  val centsInBigDecimal = cents.toBigDecimal()
  val money = centsInBigDecimal / BigDecimal.valueOf(100)
  return Money(money)
}