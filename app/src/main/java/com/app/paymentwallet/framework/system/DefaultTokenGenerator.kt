package com.app.paymentwallet.framework.system

import com.app.paymentwallet.core.ports.system.TokenGenerator
import java.util.UUID

class DefaultTokenGenerator : TokenGenerator {
    override fun generate(): String =
        UUID.randomUUID().toString()
}
