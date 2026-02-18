package com.app.paymentwallet.core.ports.repository

import com.app.paymentwallet.core.domain.model.HomeSnapshot

interface HomeSnapshotRepository {
    fun get(userid: String): Result<HomeSnapshot>
}