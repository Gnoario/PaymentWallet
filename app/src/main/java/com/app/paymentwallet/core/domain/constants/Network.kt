package com.app.paymentwallet.core.domain.constants

internal object Network {

    object ExceptionMessages {
        const val UNAUTHORIZED_ACCESS = "Unauthorized access. Please check your credentials."
        const val UNAUTHENTICATED = "Expired or invalid authentication token. Please log in again."
        const val CLIENT_ERROR = "Client error. Please check data sent."
        const val SERVER_ERROR = "Server error. Please try again later."
        const val UNEXPECTED_ERROR = "An unexpected error occurred. Please try again."
    }

    object ErrorMessages {
        const val UNAUTHENTICATED = "The user token has expired or is invalid. Code: '%s'."
        const val CLIENT_ERROR = "A client error occurred. Code: '%s'."
        const val SERVER_ERROR = "A server error occurred. Code: '%s'."
        const val UNEXPECTED_ERROR = "An unexpected error occurred. Code: '%s'."
        const val UNAUTHORIZED_ACCESS = "Unauthorized access attempt detected. Code: '%s'."
    }
}