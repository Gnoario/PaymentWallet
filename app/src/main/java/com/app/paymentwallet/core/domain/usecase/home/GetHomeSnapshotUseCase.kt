package com.app.paymentwallet.core.domain.usecase.home

import com.app.paymentwallet.core.domain.model.HomeSnapshot

interface GetHomeSnapshotUseCase {
    operator fun invoke(): Result<HomeSnapshot>
}