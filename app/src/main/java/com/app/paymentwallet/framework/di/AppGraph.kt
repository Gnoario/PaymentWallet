package com.app.paymentwallet.framework.di

import android.content.Context
import com.app.paymentwallet.core.domain.usecase.auth.LoginUseCase
import com.app.paymentwallet.core.domain.usecase.auth.LoginUseCaseImpl
import com.app.paymentwallet.core.domain.usecase.bootstrap.SeedServiceUseCase
import com.app.paymentwallet.core.domain.usecase.bootstrap.SeedServiceUseCaseImpl
import com.app.paymentwallet.core.domain.usecase.home.GetHomeSnapshotUseCase
import com.app.paymentwallet.core.domain.usecase.home.GetHomeSnapshotUseCaseImpl
import com.app.paymentwallet.core.domain.usecase.transfer.SendTransferUseCase
import com.app.paymentwallet.core.domain.usecase.transfer.SendTransferUseCaseImpl
import com.app.paymentwallet.core.network.ApiClient
import com.app.paymentwallet.core.ports.repository.AuthRepository
import com.app.paymentwallet.core.ports.repository.HomeSnapshotRepository
import com.app.paymentwallet.core.ports.repository.TransferRepository
import com.app.paymentwallet.core.ports.repository.WalletRepository
import com.app.paymentwallet.core.ports.storage.SeedFlagStore
import com.app.paymentwallet.core.ports.system.Clock
import com.app.paymentwallet.core.ports.system.IdGenerator
import com.app.paymentwallet.core.ports.system.SeedDataProvider
import com.app.paymentwallet.data.auth.AuthRepositoryImpl
import com.app.paymentwallet.data.home.HomeSnapshotRepositoryImpl
import com.app.paymentwallet.data.transfer.TransferRepositoryImpl
import com.app.paymentwallet.data.wallet.WalletRepositoryImpl
import com.app.paymentwallet.framework.network.adapter.SharedPreferencesAdapter
import com.app.paymentwallet.framework.network.client.InterceptableApiClient
import com.app.paymentwallet.framework.network.interceptors.AuthorizeInterceptor
import com.app.paymentwallet.framework.storage.SharedPrefsSeedFlagStore
import com.app.paymentwallet.framework.system.SeedDataProviderImpl
import com.app.paymentwallet.framework.system.SystemClock
import com.app.paymentwallet.framework.system.UuidGenerator

object AppGraph {

    fun init(context: Context) {
        val prefs = context.getSharedPreferences("payment_wallet_db", Context.MODE_PRIVATE)
        val storageClient: ApiClient = SharedPreferencesAdapter(prefs)
        val authorizeInterceptor = AuthorizeInterceptor(storageClient)
        val apiClient: ApiClient = InterceptableApiClient(
            realClient = storageClient,
            interceptors = listOf(authorizeInterceptor)
        )

        Container.register<ApiClient>(apiClient)

        val seedProvider: SeedDataProvider = SeedDataProviderImpl(context)
        val seedFlagStorage: SeedFlagStore = SharedPrefsSeedFlagStore(prefs)
        val clock: Clock = SystemClock()
        val idGenerator: IdGenerator = UuidGenerator()
        val authRepository: AuthRepository = AuthRepositoryImpl()
        val homeSnapshotRepository: HomeSnapshotRepository = HomeSnapshotRepositoryImpl()
        val transferRepository: TransferRepository = TransferRepositoryImpl()
        val walletRepository: WalletRepository = WalletRepositoryImpl()

        Container.register<SeedDataProvider>(seedProvider)
        Container.register<SeedFlagStore>(seedFlagStorage)
        Container.register<Clock>(clock)
        Container.register<IdGenerator>(idGenerator)
        Container.register<AuthRepository>(authRepository)
        Container.register<HomeSnapshotRepository>(homeSnapshotRepository)
        Container.register<TransferRepository>(transferRepository)
        Container.register<WalletRepository>(walletRepository)

        val seedServiceUseCase: SeedServiceUseCase = SeedServiceUseCaseImpl()
        val loginUseCase: LoginUseCase = LoginUseCaseImpl()
        val getHomeSnapshotUseCase: GetHomeSnapshotUseCase = GetHomeSnapshotUseCaseImpl()
        val sendTransferUseCase: SendTransferUseCase = SendTransferUseCaseImpl()

        Container.register<SeedServiceUseCase>(seedServiceUseCase)
        Container.register<LoginUseCase>(loginUseCase)
        Container.register<GetHomeSnapshotUseCase>(getHomeSnapshotUseCase)
        Container.register<SendTransferUseCase>(sendTransferUseCase)

        seedServiceUseCase()
    }
}