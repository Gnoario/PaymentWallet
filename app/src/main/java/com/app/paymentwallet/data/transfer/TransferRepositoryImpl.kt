package com.app.paymentwallet.data.transfer

import com.app.paymentwallet.core.data.ApiResult
import com.app.paymentwallet.core.domain.model.Money
import com.app.paymentwallet.core.domain.model.Transfer
import com.app.paymentwallet.core.network.ApiClient
import com.app.paymentwallet.core.network.ApiRoutes
import com.app.paymentwallet.core.ports.repository.TransferRepository
import com.app.paymentwallet.framework.di.Container
import org.json.JSONObject
import java.util.UUID

class TransferRepositoryImpl() : TransferRepository {

    private val apiClient: ApiClient by Container.delegate()

    override fun send(
        transfer: Transfer
    ): Result<Transfer> = runCatching {
        val authorizeBody =
            JSONObject().put("payerId", transfer.payerId).put("payeeId", transfer.payeeId)
                .put("valueCents", transfer.amount.cents).toString()

        val authorizeResponse = apiClient.post(ApiRoutes.AUTHORIZE, authorizeBody)
        if (authorizeResponse.statusCode != 200) {
            throw ApiResult.Failure(authorizeResponse.statusCode).toException()
        }

        val walletResponse = apiClient.get(ApiRoutes.wallet(transfer.payerId))
        if (walletResponse.statusCode != 200) {
            throw ApiResult.Failure(walletResponse.statusCode).toException()
        }

        val walletJson = JSONObject(walletResponse.body!!)
        val currentBalance = walletJson.getLong("balanceCents")
        val newBalance = currentBalance - transfer.amount.cents

        val updatedWallet =
            JSONObject().put("userId", transfer.payerId).put("balanceCents", newBalance).toString()

        val putWallet = apiClient.put(ApiRoutes.wallet(transfer.payerId), updatedWallet)
        if (putWallet.statusCode !in listOf(200, 201)) {
            throw ApiResult.Failure(putWallet.statusCode).toException()
        }

        val transferJson = JSONObject().put("id", transfer.id).put("payerId", transfer.payerId)
            .put("payeeId", transfer.payeeId).put("amountCents", transfer.amount.cents)
            .put("createdAt", transfer.createdAtEpochMillis).toString()

        val postTransfer = apiClient.post(ApiRoutes.transfer(transfer.id), transferJson)
        if (postTransfer.statusCode !in listOf(200, 201)) {
            throw ApiResult.Failure(postTransfer.statusCode).toException()
        }

        transfer
    }
}
