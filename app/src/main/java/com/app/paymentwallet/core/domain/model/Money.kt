package com.app.paymentwallet.core.domain.model

@JvmInline
value class Money(val cents: Long) {
    init { require(cents >= 0) { "Money cannot be negative" } }

    operator fun plus(other: Money) = Money(this.cents + other.cents)
    operator fun minus(other: Money) = Money(this.cents - other.cents)

    fun isZeroOrLess(): Boolean = cents <= 0
    fun isLessThan(other: Money): Boolean = cents < other.cents
}
