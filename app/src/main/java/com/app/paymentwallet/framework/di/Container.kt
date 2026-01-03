package com.app.paymentwallet.framework.di

import com.app.paymentwallet.core.domain.constants.Constants
import com.app.paymentwallet.core.domain.exception.ContainerException
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.cast

internal object Container {
    private val container: ConcurrentHashMap<KClass<*>, Any> = ConcurrentHashMap()

    private fun <T : Any> initialize(clazz: KClass<T>): T {
        val instance = container[clazz]
            ?: throw ContainerException(Constants.Messages.CONTAINER_NOT_FOUND.format(clazz.simpleName))
        return clazz.cast(instance)
    }

    inline fun <reified T : Any> delegate() = lazy { initialize(T::class) }

    inline fun <reified T : Any> register(instance: T) {
        container[T::class] = instance
    }
}