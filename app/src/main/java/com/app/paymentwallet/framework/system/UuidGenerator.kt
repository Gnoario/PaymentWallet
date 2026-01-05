package com.app.paymentwallet.framework.system

import com.app.paymentwallet.core.ports.system.IdGenerator
import java.util.UUID

class UuidGenerator : IdGenerator {
    override fun new(): String = UUID.randomUUID().toString()
}
