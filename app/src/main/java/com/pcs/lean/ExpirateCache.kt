package com.pcs.lean

import java.util.concurrent.TimeUnit

class ExpirableCache(private val flushInterval: Long = TimeUnit.MINUTES.toMillis(1)){
    private var lastFlushTime = System.nanoTime()

    private val cache = HashMap<Any, Any>()
    val size: Int
        get() = cache.size

    fun set(key: Any, value: Any) {
        this.cache[key] = value
    }

    fun remove(key: Any){
        recycle()
        this.cache.remove(key)
    }

    fun get(key: Any): Any?{
        recycle()
        return this.cache[key]
    }

    fun clear() = this.cache.clear()

    private fun recycle() {
        val shouldRecycle = System.nanoTime() - lastFlushTime >= TimeUnit.MILLISECONDS.toNanos(flushInterval)
        if (!shouldRecycle) return
        clear()
    }
}