package com.app.paymentwallet.core.domain.model

sealed class DomainError(message: String) : RuntimeException(message) {
    data object InvalidAmount : DomainError("O valor deve ser maior que zero.")
    data object SamePayerAndPayee : DomainError("Você não pode transferir para você mesmo.")
    data object InsufficientBalance : DomainError("Saldo insuficiente para realizar a transferência.")
    data object NotAuthenticated : DomainError("Sessão expirada. Faça login novamente.")
    data class AuthorizationDenied(val reason: String?) :
        DomainError(reason ?: "Operação não autorizada.")
    data object InvalidCredentials : DomainError("Usuário ou senha inválidos.")
}
