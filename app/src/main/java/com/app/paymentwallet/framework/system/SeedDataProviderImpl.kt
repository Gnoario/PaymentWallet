package com.app.paymentwallet.framework.system

import android.content.Context
import com.app.paymentwallet.core.domain.model.Contact
import com.app.paymentwallet.core.domain.model.Money
import com.app.paymentwallet.core.domain.model.SeedData
import com.app.paymentwallet.core.domain.model.User
import com.app.paymentwallet.core.domain.model.Wallet
import com.app.paymentwallet.core.ports.system.SeedDataProvider
import org.json.JSONArray
import org.json.JSONObject

class SeedDataProviderImpl(
    private val context: Context,
    private val assetFileName: String = "mock_seed.json"
) : SeedDataProvider {

    override fun load(): Result<SeedData> = runCatching {

        val jsonText = context.assets.open(assetFileName).bufferedReader().use { it.readText() }
        val root = JSONObject(jsonText)

        val users = root.optJSONArray("users").toUsers()
        val wallets = root.optJSONArray("wallets").toWallets()
        val contacts = root.optJSONArray("contacts").toContacts()

        SeedData(
            users = users,
            wallets = wallets,
            contacts = contacts
        )
    }

    private fun JSONArray?.toUsers(): List<User> {
        if (this == null) return emptyList()
        return (0 until length()).map { i ->
            val o = getJSONObject(i)
            User(
                id = o.getString("id"),
                name = o.getString("name"),
                email = o.getString("email")
            )
        }
    }

    private fun JSONArray?.toWallets(): List<Wallet> {
        if (this == null) return emptyList()
        return (0 until length()).map { i ->
            val o = getJSONObject(i)
            Wallet(
                userId = o.getString("userId"),
                balance = Money(o.getLong("balanceCents"))
            )
        }
    }

    private fun JSONArray?.toContacts(): List<Contact> {
        if (this == null) return emptyList()
        return (0 until length()).map { i ->
            val o = getJSONObject(i)
            Contact(
                id = o.getString("id"),
                name = o.getString("name"),
                email = o.getString("email")
            )
        }
    }
}