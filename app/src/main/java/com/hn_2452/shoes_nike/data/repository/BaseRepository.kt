package com.hn_2452.shoes_nike.data.repository

import com.hn_2452.shoes_nike.utility.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

open class BaseRepository(open val mDispatcher: CoroutineDispatcher) {
    suspend fun <T> run(block: suspend () -> T): Result<T> = withContext(mDispatcher) {
        return@withContext try {
            Result(true, block(), null)
        } catch (e: Exception) {
            Result(false, null, e)
        }
    }
}