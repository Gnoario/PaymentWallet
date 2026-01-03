package com.app.paymentwallet.framework.system

import com.app.paymentwallet.core.ports.system.Clock

class SystemClock : Clock {
    override fun nowEpochMillis(): Long =
        System.currentTimeMillis()
}
