package com.app.paymentwallet.framework.network.client

import com.app.paymentwallet.core.domain.model.NetworkRequest
import com.app.paymentwallet.core.network.ApiClient
import com.app.paymentwallet.core.network.ApiInterceptor
import com.app.paymentwallet.core.network.Response

internal class InterceptableApiClient(
    private val realClient: ApiClient,
    private val interceptors: List<ApiInterceptor>
) : ApiClient {

    override fun get(url: String) =
        execute("GET", url, null)

    override fun post(url: String, body: String) =
        execute("POST", url, body)

    override fun put(url: String, body: String) =
        execute("PUT", url, body)

    override fun patch(url: String, body: String) =
        execute("PATCH", url, body)

    override fun delete(url: String) =
        execute("DELETE", url, null)

    override fun clear(): Response =
        realClient.clear()

    private fun execute(
        method: String,
        url: String,
        body: String?
    ): Response {

        val request = NetworkRequest(method, url, body)

        return RealChain(realClient, interceptors, 0)
            .proceed(request)
    }

    private class RealChain(
        private val client: ApiClient,
        private val interceptors: List<ApiInterceptor>,
        private val index: Int
    ) : ApiInterceptor.Chain {

        override fun proceed(request: NetworkRequest): Response {
            return if (index < interceptors.size) {
                interceptors[index]
                    .intercept(request, RealChain(client, interceptors, index + 1))
            } else {
                when (request.method) {
                    "GET" -> client.get(request.url)
                    "POST" -> client.post(request.url, request.body.orEmpty())
                    "PUT" -> client.put(request.url, request.body.orEmpty())
                    "PATCH" -> client.patch(request.url, request.body.orEmpty())
                    "DELETE" -> client.delete(request.url)
                    else -> Response(400, null)
                }
            }
        }
    }
}
