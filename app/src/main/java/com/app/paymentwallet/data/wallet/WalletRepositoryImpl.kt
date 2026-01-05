package com.app.paymentwallet.data.wallet

import com.app.paymentwallet.core.data.ApiResult
import com.app.paymentwallet.core.domain.model.Money
import com.app.paymentwallet.core.domain.model.Wallet
import com.app.paymentwallet.core.network.ApiClient
import com.app.paymentwallet.core.network.ApiRoutes
import com.app.paymentwallet.core.ports.repository.WalletRepository
import com.app.paymentwallet.framework.di.Container
import org.json.JSONObject
import kotlin.getValue

class WalletRepositoryImpl() : WalletRepository {

    private val apiClient: ApiClient by Container.delegate()

    override fun get(userId: String): Result<Wallet> = runCatching {
        val walletResponse = apiClient.get(ApiRoutes.wallet(userId))

        if (walletResponse.statusCode != 200) throw ApiResult.Failure(walletResponse.statusCode)
            .toException()

        val walletJson = JSONObject(walletResponse.body!!)

        val wallet = Wallet(
            userId = walletJson.getString("userId"),
            balance = Money(walletJson.getLong("balanceCents"))
        )

        wallet
    }
}

