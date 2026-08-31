package com.gobovr.mypod.auth

import android.content.Context
import android.content.SharedPreferences

/**
 * Persists YouTube Music login state across app restarts.
 *
 * Kept deliberately simple (plain SharedPreferences, not DataStore) since
 * this is the only preference storage MyPod needs so far. The cookie stored
 * here is the same InnerTube auth cookie Meld's LoginScreen captures from a
 * WebView sign-in -- this is what YouTube.cookie is set to, everywhere in
 * the app, to make authenticated requests (library, playlists, streaming).
 */
object YouTubeAuthStore {
    private const val PREFS_NAME = "mypod_youtube_auth"

    private fun prefs(context: Context): SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isLoggedIn(context: Context): Boolean =
        !prefs(context).getString(KEY_COOKIE, null).isNullOrEmpty()

    fun cookie(context: Context): String? = prefs(context).getString(KEY_COOKIE, null)
    fun visitorData(context: Context): String? = prefs(context).getString(KEY_VISITOR_DATA, null)
    fun dataSyncId(context: Context): String? = prefs(context).getString(KEY_DATA_SYNC_ID, null)
    fun accountName(context: Context): String? = prefs(context).getString(KEY_ACCOUNT_NAME, null)
    fun accountEmail(context: Context): String? = prefs(context).getString(KEY_ACCOUNT_EMAIL, null)

    fun saveLogin(
        context: Context,
        cookie: String,
        visitorData: String?,
        dataSyncId: String?,
        accountName: String,
        accountEmail: String?,
    ) {
        prefs(context).edit()
            .putString(KEY_COOKIE, cookie)
            .putString(KEY_VISITOR_DATA, visitorData)
            .putString(KEY_DATA_SYNC_ID, dataSyncId)
            .putString(KEY_ACCOUNT_NAME, accountName)
            .putString(KEY_ACCOUNT_EMAIL, accountEmail)
            .apply()
    }

    fun logOut(context: Context) {
        prefs(context).edit().clear().apply()
    }

    private const val KEY_COOKIE = "cookie"
    private const val KEY_VISITOR_DATA = "visitor_data"
    private const val KEY_DATA_SYNC_ID = "data_sync_id"
    private const val KEY_ACCOUNT_NAME = "account_name"
    private const val KEY_ACCOUNT_EMAIL = "account_email"
}
