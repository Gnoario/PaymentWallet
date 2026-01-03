package com.app.paymentwallet.core.ports.system

interface IdGenerator {
    fun newId(): String
}
