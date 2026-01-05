package com.app.paymentwallet.framework.network.interceptors

import com.app.paymentwallet.core.domain.model.ApiRequest
import com.app.paymentwallet.core.network.ApiClient
import com.app.paymentwallet.core.network.ApiInterceptor
import com.app.paymentwallet.core.network.ApiRoutes
import com.app.paymentwallet.core.network.Response
import com.app.paymentwallet.framework.di.Container
import org.json.JSONArray
import org.json.JSONObject

internal class AuthorizeInterceptor(val apiClient: ApiClient) : ApiInterceptor {
    override fun invoke(request: ApiRequest, chain: ApiInterceptor.Chain): Response {
        return when {
            request.method == "POST" && request.url == ApiRoutes.LOGIN -> handleLogin(request)
            request.method == "POST" && request.url == ApiRoutes.AUTHORIZE -> handleAuthorize(request)
            else -> chain.proceed(request)
        }
    }

    private fun handleLogin(req: ApiRequest): Response {
        val body = JSONObject(req.body.orEmpty())
        val email = body.optString("email")
        val password = body.optString("password")

        val credsResponse = apiClient.get(ApiRoutes.CREDENTIALS)
        if (credsResponse.statusCode != 200 || credsResponse.body.isNullOrBlank()) {
            return Response(500, JSONObject().put("message", "Credentials not seeded").toString())
        }

        val credsArr = JSONArray(credsResponse.body)
        val match = (0 until credsArr.length())
            .map { credsArr.getJSONObject(it) }
            .firstOrNull {
                it.optString("email") == email && it.optString("password") == password
            }
            ?: return Response(401, JSONObject().put("message", "Invalid credentials").toString())

        val userId = match.optString("userId", "")
        if (userId.isBlank()) {
            return Response(500, JSONObject().put("message", "Credential missing userId").toString())
        }

        val userResponse = apiClient.get(ApiRoutes.user(userId))
        if (userResponse.statusCode != 200 || userResponse.body.isNullOrBlank()) {
            return Response(404, JSONObject().put("message", "User not found").toString())
        }

        return Response(200, userResponse.body)
    }

    private fun handleAuthorize(req: ApiRequest): Response {
        val body = JSONObject(req.body.orEmpty())
        val valueCents = body.optLong("valueCents", -1L)

        val rulesResponse = apiClient.get(ApiRoutes.AUTHORIZE_RULES)
        if (rulesResponse.statusCode != 200 || rulesResponse.body.isNullOrBlank()) {
            return Response(200, JSONObject().put("authorized", true).toString())
        }

        val rules = JSONObject(rulesResponse.body)
        val denyArr = rules.optJSONArray("denyWhenAmountEqualsCents") ?: JSONArray()
        val denyReason = rules.optString("denyReason", "Operation denied")

        val isDenied = (0 until denyArr.length())
            .any { denyArr.optLong(it) == valueCents }

        return if (isDenied) {
            Response(403, JSONObject().put("message", denyReason).toString())
        } else {
            Response(200, JSONObject().put("authorized", true).toString())
        }
    }
}