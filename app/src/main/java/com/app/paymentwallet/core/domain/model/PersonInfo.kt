package com.app.paymentwallet.core.domain.model

data class PersonInfo(
    val id: String,
    val name: String,
    val email: String
)

@JvmInline
value class UserId(val value: String)

@JvmInline
value class ContactId(val value: String)