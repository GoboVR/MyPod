package com.gobovr.mypod.ui.screens

import android.annotation.SuppressLint
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.gobovr.mypod.auth.YouTubeAuthStore
import com.metrolist.innertube.YouTube
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Signs the user into YouTube Music via a real Google sign-in WebView, the
 * same mechanism Meld/Metrolist use: after sign-in completes, we read the
 * session cookie the WebView received for music.youtube.com, and pass it to
 * InnerTube's YouTube.cookie -- from then on, YouTube.* calls are
 * authenticated as that account (library, playlists, streaming).
 *
 * There's no official YouTube Music login API for third-party apps to use,
 * so this reuses the same account cookie your browser would get -- it's not
 * an OAuth token, and Google could change this flow at any time.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YouTubeLoginScreen(onLoginComplete: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var visitorData by remember { mutableStateOf<String?>(null) }
    var dataSyncId by remember { mutableStateOf<String?>(null) }
    val hasCompletedLogin = remember { mutableStateOf(false) }
    val isValidating = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { webViewContext ->
                WebView(webViewContext).apply {
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView, url: String?) {
                            loadUrl("javascript:Android.onRetrieveVisitorData(window.yt.config_.VISITOR_DATA)")
                            loadUrl("javascript:Android.onRetrieveDataSyncId(window.yt.config_.DATASYNC_ID)")

                            if (url?.startsWith("https://music.youtube.com") == true && !hasCompletedLogin.value) {
                                val innerTubeCookie = CookieManager.getInstance().getCookie(url)
                                if (innerTubeCookie.isNullOrEmpty()) return

                                hasCompletedLogin.value = true
                                isValidating.value = true

                                coroutineScope.launch {
                                    delay(300)

                                    YouTube.cookie = innerTubeCookie
                                    YouTube.dataSyncId = dataSyncId
                                    YouTube.visitorData = visitorData

                                    YouTube.accountInfo()
                                        .onSuccess { account ->
                                            YouTubeAuthStore.saveLogin(
                                                context = context,
                                                cookie = innerTubeCookie,
                                                visitorData = visitorData,
                                                dataSyncId = dataSyncId,
                                                accountName = account.name,
                                                accountEmail = account.email,
                                            )
                                            onLoginComplete()
                                        }
                                        .onFailure { error ->
                                            Timber.e(error, "YouTube Music login: account validation failed")
                                            hasCompletedLogin.value = false
                                            isValidating.value = false
                                        }
                                }
                            }
                        }
                    }
                    CookieManager.getInstance().setAcceptCookie(true)
                    CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        setSupportZoom(true)
                        builtInZoomControls = true
                        displayZoomControls = false
                    }
                    addJavascriptInterface(object {
                        @JavascriptInterface
                        fun onRetrieveVisitorData(newVisitorData: String?) {
                            if (!newVisitorData.isNullOrEmpty()) visitorData = newVisitorData
                        }
                        @JavascriptInterface
                        fun onRetrieveDataSyncId(newDataSyncId: String?) {
                            if (!newDataSyncId.isNullOrEmpty()) dataSyncId = newDataSyncId.substringBefore("||")
                        }
                    }, "Android")
                    loadUrl("https://accounts.google.com/ServiceLogin?continue=https%3A%2F%2Fmusic.youtube.com")
                }
            }
        )

        if (isValidating.value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
