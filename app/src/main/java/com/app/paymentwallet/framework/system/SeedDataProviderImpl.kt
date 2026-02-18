package com.app.paymentwallet.framework.system

import android.content.Context
import com.app.paymentwallet.core.network.ApiClient
import com.app.paymentwallet.core.network.ApiRoutes
import com.app.paymentwallet.core.ports.system.SeedDataProvider
import com.app.paymentwallet.framework.di.Container
import org.json.JSONObject

class SeedDataProviderImpl(
    private val context: Context,
    private val assetFileName: String = "mock_seed.json",
) : SeedDataProvider {

    private val apiClient: ApiClient by Container.delegate()

    override fun invoke(): Result<Unit> = runCatching {

        val jsonText = context.assets
            .open(assetFileName)
            .bufferedReader()
            .use { it.readText() }

        val root = JSONObject(jsonText)

        root.optJSONArray("credentials")?.let { credentials ->
            apiClient.post(ApiRoutes.CREDENTIALS, credentials.toString())
        }

        root.optJSONArray("users")?.let { users ->
            for (i in 0 until users.length()) {
                val user = users.getJSONObject(i)
                val id = user.getString("id")
                apiClient.post(ApiRoutes.user(id), user.toString())
            }
        }

        root.optJSONArray("wallets")?.let { wallets ->
            for (i in 0 until wallets.length()) {
                val wallet = wallets.getJSONObject(i)
                val userId = wallet.getString("userId")
                apiClient.post(ApiRoutes.wallet(userId), wallet.toString())
            }
        }

        root.optJSONArray("contacts")?.let { contacts ->
            apiClient.post(ApiRoutes.CONTACTS, contacts.toString())
        }

        root.optJSONObject("authorizeRules")?.let { rules ->
            apiClient.post(ApiRoutes.AUTHORIZE_RULES, rules.toString())
        }
    }
}