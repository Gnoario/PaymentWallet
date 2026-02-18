package com.app.paymentwallet.framework.storage

import android.content.SharedPreferences
import com.app.paymentwallet.core.ports.storage.SeedFlagStore
import androidx.core.content.edit

class SharedPrefsSeedFlagStore(
    private val prefs: SharedPreferences
) : SeedFlagStore {

    override fun isSeeded(): Boolean =
        prefs.getBoolean(KEY, false)

    override fun markSeeded() {
        prefs.edit { putBoolean(KEY, true) }
    }

    private companion object {
        const val KEY = "seed_done"
    }
}
