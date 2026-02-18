package com.app.paymentwallet.data.auth

import com.app.paymentwallet.core.data.ApiResult
import com.app.paymentwallet.core.domain.model.AuthToken
import com.app.paymentwallet.core.domain.model.PersonInfo
import com.app.paymentwallet.core.domain.model.User
import com.app.paymentwallet.core.domain.model.UserId
import com.app.paymentwallet.core.network.ApiClient
import com.app.paymentwallet.core.network.ApiRoutes
import com.app.paymentwallet.core.ports.repository.AuthRepository
import com.app.paymentwallet.core.ports.system.Clock
import com.app.paymentwallet.core.ports.system.IdGenerator
import com.app.paymentwallet.framework.di.Container
import org.json.JSONObject
import kotlin.getValue

class AuthRepositoryImpl() : AuthRepository {

    private val apiClient: ApiClient by Container.delegate()
    private val idGenerator: IdGenerator by Container.delegate()
    private val clock: Clock by Container.delegate()

    override fun login(email: String, password: String): Result<User> = runCatching {

        val body = JSONObject().put("email", email).put("password", password).toString()

        val response = apiClient.post(ApiRoutes.LOGIN, body)

        when (response.statusCode) {
            200, 201 -> {
                val o = JSONObject(response.body!!)
                val userId = UserId(o.getString("id"))
                val personInfo = PersonInfo(
                    id = userId.value, name = o.getString("name"), email = o.getString("email")
                )
                val token = AuthToken(
                    value = idGenerator.new(),
                    createdAtEpochMillis = clock.nowEpochMillis(),
                )
                User(
                    id = userId, info = personInfo, authToken = token
                )
            }

            else -> throw ApiResult.Failure(response.statusCode).toException()
        }
    }

    override fun getSession(): Result<User> {
        TODO("Not yet implemented")
    }

    override fun logout(): Result<Unit> {
        TODO("Not yet implemented")
    }
}
