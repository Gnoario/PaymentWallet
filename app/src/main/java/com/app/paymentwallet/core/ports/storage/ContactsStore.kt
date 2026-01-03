package com.app.paymentwallet.core.ports.storage

import com.app.paymentwallet.core.domain.model.Contact

interface ContactsStore {
    fun getContacts(): Result<List<Contact>>
    fun saveAll(contacts: List<Contact>): Result<Unit>
}
