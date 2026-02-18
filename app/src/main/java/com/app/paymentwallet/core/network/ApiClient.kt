package com.app.paymentwallet.core.network

interface ApiClient {
    fun get(url: String): Response

    fun post(url: String, body: String): Response

    fun put(url: String, body: String): Response

    fun patch(url: String, body: String): Response

    fun delete(url: String): Response

    fun clear(): Response
}