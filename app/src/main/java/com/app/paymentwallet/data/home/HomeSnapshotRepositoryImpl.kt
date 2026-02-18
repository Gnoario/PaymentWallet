package com.app.paymentwallet.data.home

import com.app.paymentwallet.core.data.ApiResult
import com.app.paymentwallet.core.domain.model.Contact
import com.app.paymentwallet.core.domain.model.ContactId
import com.app.paymentwallet.core.domain.model.HomeSnapshot
import com.app.paymentwallet.core.domain.model.Money
import com.app.paymentwallet.core.domain.model.PersonInfo
import com.app.paymentwallet.core.domain.model.Wallet
import com.app.paymentwallet.core.network.ApiClient
import com.app.paymentwallet.core.network.ApiRoutes
import com.app.paymentwallet.core.ports.repository.HomeSnapshotRepository
import com.app.paymentwallet.framework.di.Container
import org.json.JSONArray
import org.json.JSONObject

class HomeSnapshotRepositoryImpl() : HomeSnapshotRepository {

    private val apiClient: ApiClient by Container.delegate()

    override fun get(userid: String): Result<HomeSnapshot> = runCatching {

        val walletResponse = apiClient.get(ApiRoutes.wallet(userid))
        val contactsResponse = apiClient.get(ApiRoutes.CONTACTS)

        if (walletResponse.statusCode != 200) throw ApiResult.Failure(walletResponse.statusCode)
            .toException()

        if (contactsResponse.statusCode != 200) throw ApiResult.Failure(contactsResponse.statusCode)
            .toException()

        val walletJson = JSONObject(walletResponse.body!!)
        val contactsJson = JSONArray(contactsResponse.body!!)

        val wallet = Wallet(
            userId = walletJson.getString("userId"),
            balance = Money(walletJson.getLong("balanceCents"))
        )

        val contacts = (0 until contactsJson.length()).map { i ->
            val o = contactsJson.getJSONObject(i)
            Contact(
                id = ContactId(o.getString("id")),
                info = PersonInfo(
                    id = o.getString("id"), name = o.getString("name"), email = o.getString("email")
                ),
            )
        }

        HomeSnapshot(
            wallet = wallet, contacts = contacts
        )
    }
}
