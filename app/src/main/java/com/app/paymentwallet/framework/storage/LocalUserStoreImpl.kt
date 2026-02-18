package com.app.paymentwallet.framework.storage

import android.content.SharedPreferences
import androidx.core.content.edit
import com.app.paymentwallet.core.domain.model.AuthToken
import com.app.paymentwallet.core.domain.model.PersonInfo
import com.app.paymentwallet.core.domain.model.User
import com.app.paymentwallet.core.domain.model.UserId
import com.app.paymentwallet.core.ports.storage.LocalUserStore
import org.json.JSONObject

class LocalUserStoreImpl(private val prefs: SharedPreferences) : LocalUserStore {
    override fun save(user: User): Result<Unit> = runCatching {
        val json = user.toJson()
        prefs.edit { putString(KEY_LOCAL_USER, json) }
    }

    override fun get(): Result<User?> = runCatching {
        val raw = prefs.getString(KEY_LOCAL_USER, null) ?: return@runCatching null
        raw.toUser()
    }

    override fun clear(): Result<Unit> = runCatching {
        prefs.edit { remove(KEY_LOCAL_USER) }
    }

    private fun User.toJson(): String {
        return JSONObject().put("id", id.value).put(
                "info",
                JSONObject().put("id", info.id).put("name", info.name).put("email", info.email)
            ).put(
                "authToken",
                JSONObject().put("value", authToken.value)
                    .put("createdAtEpochMillis", authToken.createdAtEpochMillis)
            ).toString()
    }

    private fun String.toUser(): User {
        val root = JSONObject(this)

        val id = root.getString("id")

        val infoObj = root.getJSONObject("info")
        val tokenObj = root.getJSONObject("authToken")

        return User(
            id = UserId(id), info = PersonInfo(
                id = infoObj.getString("id"),
                name = infoObj.getString("name"),
                email = infoObj.getString("email")
            ), authToken = AuthToken(
                value = tokenObj.getString("value"),
                createdAtEpochMillis = tokenObj.getLong("createdAtEpochMillis")
            )
        )
    }

    private companion object {
        const val KEY_LOCAL_USER = "local_user"
    }
}