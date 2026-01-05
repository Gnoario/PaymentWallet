package com.app.paymentwallet.core.network

object ApiRoutes {
    const val CREDENTIALS = "/credentials"
    const val CONTACTS = "/contacts"
    const val AUTHORIZE = "/authorize"
    const val AUTHORIZE_RULES = "/authorizeRules"
    const val LOGIN = "/login"

    fun user(userId: String) = "/users/$userId"
    fun wallet(userId: String) = "/wallets/$userId"
    fun transfer(transferId: String) = "/transfers/$transferId"
}
