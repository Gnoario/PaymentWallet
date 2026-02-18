package com.app.paymentwallet.framework.network.adapter

import android.content.SharedPreferences
import com.app.paymentwallet.core.network.ApiClient
import com.app.paymentwallet.core.network.Response
import androidx.core.content.edit

class SharedPreferencesAdapter(
    private val prefs: SharedPreferences
) : ApiClient {

    override fun get(url: String): Response {
        val value = prefs.getString(key(url), null)

        return if (value != null) {
            Response(200, value)
        } else {
            Response(404, null)
        }
    }

    override fun post(url: String, body: String): Response {
        prefs.edit {
            putString(key(url), body)
        }

        return Response(201, body)
    }

    override fun put(url: String, body: String): Response {
        val k = key(url)

        return if (prefs.contains(k)) {
            prefs.edit { putString(k, body) }
            Response(200, body)
        } else {
            Response(404, null)
        }
    }

    override fun patch(url: String, body: String): Response {
        prefs.edit {
            putString(key(url), body)
        }

        return Response(200, body)
    }

    override fun delete(url: String): Response {
        val k = key(url)

        return if (prefs.contains(k)) {
            prefs.edit { remove(k) }
            Response(204, null)
        } else {
            Response(404, null)
        }
    }

    override fun clear(): Response {
        prefs.edit { clear() }
        return Response(204, null)
    }

    private fun key(url: String): String =
        url.trim()
            .lowercase()
            .replace("https://", "")
            .replace("http://", "")
            .replace("/", "_")
            .replace("?", "_")
            .replace("&", "_")
            .replace("=", "_")
}