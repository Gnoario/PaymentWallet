package com.app.paymentwallet.framework.storage

import android.content.SharedPreferences
import androidx.core.content.edit
import com.app.paymentwallet.core.domain.model.AuthToken
import com.app.paymentwallet.core.ports.storage.TokenStore

class TokenStoreImpl(
    private val prefs: SharedPreferences
) : TokenStore {

    override fun save(token: AuthToken): Result<Unit> = runCatching {
        prefs.edit {
            putString(KEY_TOKEN_VALUE, token.value)
            putLong(KEY_TOKEN_CREATED_AT, token.createdAtEpochMillis)
        }
    }

    override fun get(): Result<AuthToken?> = runCatching {
        val value = prefs.getString(KEY_TOKEN_VALUE, null) ?: return@runCatching null
        val createdAt = prefs.getLong(KEY_TOKEN_CREATED_AT, 0L)
        AuthToken(value = value, createdAtEpochMillis = createdAt)
    }

    override fun clear(): Result<Unit> = runCatching {
        prefs.edit {
            remove(KEY_TOKEN_VALUE)
            remove(KEY_TOKEN_CREATED_AT)
        }
    }

    private companion object {
        const val KEY_TOKEN_VALUE = "auth_token_value"
        const val KEY_TOKEN_CREATED_AT = "auth_token_created_at"
    }
}