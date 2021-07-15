package com.weight_plugin.weight_plugin.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private val handler = Handler(Looper.getMainLooper())
private val coreSize = Runtime.getRuntime().availableProcessors() + 1

private val fix: ExecutorService = Executors.newFixedThreadPool(coreSize)
private val cache: ExecutorService = Executors.newCachedThreadPool()
private val single: ExecutorService = Executors.newSingleThreadExecutor()
private val scheduled: ExecutorService = Executors.newScheduledThreadPool(coreSize)


/**
 * 切换到主线程
 */
fun <T> T.ktxRunOnUi(block: T.() -> Unit) {
    handler.post {
        block()
    }
}

/**
 * 延迟delayMillis后切换到主线程
 */
fun <T> T.ktxRunOnUiDelay(delayMillis: Long, block: T.() -> Unit) {
    handler.postDelayed({
        block()
    }, delayMillis)
}

/**
 * SingleThreadPool
 */
fun <T> T.ktxRunOnBgSingle(block: T.() -> Unit) {
    single.execute {
        block()
    }
}

/**
 * FixedThreadPool
 */
fun <T> T.ktxRunOnBgFix(block: T.() -> Unit) {
    fix.execute {
        block()
    }
}

/**
 * CachedThreadPool
 */
fun <T> T.ktxRunOnBgCache(block: T.() -> Unit) {
    cache.execute {
        block()
    }
}