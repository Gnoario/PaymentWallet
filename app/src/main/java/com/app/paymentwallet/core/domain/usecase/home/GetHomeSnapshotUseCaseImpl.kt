package com.app.paymentwallet.core.domain.usecase.home

import com.app.paymentwallet.core.domain.model.DomainError
import com.app.paymentwallet.core.domain.model.HomeSnapshot
import com.app.paymentwallet.core.ports.repository.HomeSnapshotRepository
import com.app.paymentwallet.core.ports.storage.LocalUserStore
import com.app.paymentwallet.framework.di.Container
import kotlin.getValue

class GetHomeSnapshotUseCaseImpl() : GetHomeSnapshotUseCase {

    private val homeSnapshotRepository: HomeSnapshotRepository by Container.delegate()
    private val localUserStore: LocalUserStore by Container.delegate()

    override operator fun invoke(): Result<HomeSnapshot> = runCatching {

        val currentUser = localUserStore.get().getOrNull()

        val userId = currentUser?.id ?: throw DomainError.NotAuthenticated

        val result = homeSnapshotRepository.get(userId.value).getOrElse { throw it }

        result
    }
}
