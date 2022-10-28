package com.bismark.core.common.network.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val jpgDispatcher: JPGDispatchers)

enum class JPGDispatchers {
    IO
}
