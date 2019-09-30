package com.anand.limitless

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import com.anand.limitless.api.StateApi
import com.anand.limitless.db.StateDb
import com.anand.limitless.repository.inDb.DbStatePostRepository
import com.anand.limitless.repository.inDb.StatePostRepository
import com.anand.limitless.repository.inMemory.byItem.InMemoryByItemRepository
import com.anand.limitless.repository.inMemory.byPage.InMemoryByPageKeyRepository
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Super simplified service locator implementation to allow us to replace default implementations
 * for testing.
 */
interface ServiceLocator {
    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null
        fun instance(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator(
                            app = context.applicationContext as Application,
                            useInMemoryDb = false)
                }
                return instance!!
            }
        }

        /**
         * Allows tests to replace the default implementations.
         */
        @VisibleForTesting
        fun swap(locator: ServiceLocator) {
            instance = locator
        }
    }

    fun getRepository(type: StatePostRepository.Type): StatePostRepository

    fun getNetworkExecutor(): Executor

    fun getDiskIOExecutor(): Executor

    fun getRedditApi(): StateApi
}

/**
 * default implementation of ServiceLocator that uses production endpoints.
 */
open class DefaultServiceLocator(val app: Application, val useInMemoryDb: Boolean) : ServiceLocator {
    // thread pool used for disk access
    @Suppress("PrivatePropertyName")
    private val DISK_IO = Executors.newSingleThreadExecutor()

    // thread pool used for network requests
    @Suppress("PrivatePropertyName")
    private val NETWORK_IO = Executors.newFixedThreadPool(5)

    private val db by lazy {
        StateDb.create(app, useInMemoryDb)
    }

    private val api by lazy {
        StateApi.create()
    }

    override fun getRepository(type: StatePostRepository.Type): StatePostRepository {
        return when (type) {
            StatePostRepository.Type.IN_MEMORY_BY_ITEM -> InMemoryByItemRepository(
                    stateApi = getRedditApi(),
                    networkExecutor = getNetworkExecutor())
            StatePostRepository.Type.IN_MEMORY_BY_PAGE -> InMemoryByPageKeyRepository(
                    stateApi = getRedditApi(),
                    networkExecutor = getNetworkExecutor())
            StatePostRepository.Type.DB -> DbStatePostRepository(
                    db = db,
                    stateApi = getRedditApi(),
                    ioExecutor = getDiskIOExecutor())
        }
    }

    override fun getNetworkExecutor(): Executor = NETWORK_IO

    override fun getDiskIOExecutor(): Executor = DISK_IO

    override fun getRedditApi(): StateApi = api
}