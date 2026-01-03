package com.app.paymentwallet.core.ports.repository

import com.app.paymentwallet.core.domain.model.HomeSnapshot

interface HomeRepository {
    fun invoke(): Result<HomeSnapshot>
}