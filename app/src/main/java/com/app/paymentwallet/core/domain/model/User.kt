package com.app.paymentwallet.core.domain.model

data class User(
    val authToken: AuthToken,
    val id: UserId,
    val info: PersonInfo
)
