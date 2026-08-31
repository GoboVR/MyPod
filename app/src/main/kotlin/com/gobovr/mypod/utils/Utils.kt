package com.gobovr.mypod.utils

import timber.log.Timber

/**
 * MyPod: simplified from Meld's version, which reported to a crash-analytics
 * service we haven't set up. This just logs -- swap in real crash reporting
 * later if wanted.
 */
fun reportException(throwable: Throwable) {
    Timber.e(throwable)
}
