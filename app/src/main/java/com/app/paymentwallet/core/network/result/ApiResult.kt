package com.app.paymentwallet.core.network.result

import com.app.paymentwallet.core.domain.constants.Network
import com.app.paymentwallet.framework.network.exception.ClientErrorException
import com.app.paymentwallet.framework.network.exception.ServerErrorException
import com.app.paymentwallet.framework.network.exception.UnauthenticatedException
import com.app.paymentwallet.framework.network.exception.UnexpectedResponseException

internal sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Empty(val code: Int) : ApiResult<Nothing>()
    data class Failure(val code: Int) : ApiResult<Nothing>() {
        fun toException(): Exception {
            return when (code) {
                401 -> UnauthenticatedException(
                    Network.ExceptionMessages.UNAUTHENTICATED,
                    Network.ErrorMessages.UNAUTHENTICATED.format(code)
                )

                403 -> UnauthenticatedException(
                    Network.ExceptionMessages.UNAUTHORIZED_ACCESS,
                    Network.ErrorMessages.UNAUTHORIZED_ACCESS.format(code)
                )

                in 404..499 -> ClientErrorException(
                    Network.ErrorMessages.CLIENT_ERROR.format(code),
                    Network.ExceptionMessages.CLIENT_ERROR.format(code)
                )

                in 500..599 -> ServerErrorException(
                    Network.ExceptionMessages.SERVER_ERROR,
                    Network.ErrorMessages.SERVER_ERROR.format(code)
                )

                else -> UnexpectedResponseException(
                    Network.ExceptionMessages.UNEXPECTED_ERROR,
                    Network.ErrorMessages.UNEXPECTED_ERROR.format(code)
                )
            }
        }
    }
}