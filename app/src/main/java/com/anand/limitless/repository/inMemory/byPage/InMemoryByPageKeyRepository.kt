package com.anand.limitless.repository.inMemory.byPage

import androidx.lifecycle.Transformations
import androidx.annotation.MainThread
import androidx.paging.toLiveData
import com.anand.limitless.api.StateApi
import com.anand.limitless.repository.Listing
import com.anand.limitless.repository.inDb.StatePostRepository
import com.anand.limitless.vo.StateName
import java.util.concurrent.Executor

/**
 * Repository implementation that returns a Listing that loads data directly from network by using
 * the previous / next page keys returned in the query.
 */
class InMemoryByPageKeyRepository(private val stateApi: StateApi,
                                  private val networkExecutor: Executor) : StatePostRepository {
    @MainThread
    override fun postsOfSubreddit(subReddit: String, pageSize: Int): Listing<StateName> {
        val sourceFactory = SubRedditDataSourceFactory(stateApi, subReddit, networkExecutor)

        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
        val livePagedList = sourceFactory.toLiveData(
                pageSize = pageSize,
                // provide custom executor for network requests, otherwise it will default to
                // Arch Components' IO pool which is also used for disk access
                fetchExecutor = networkExecutor)

        val refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
            it.initialLoad
        }
        return Listing(
                pagedList = livePagedList,
                networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                  it.networkState
                },
                retry = {
                    sourceFactory.sourceLiveData.value?.retryAllFailed()
                },
                refresh = {
                    sourceFactory.sourceLiveData.value?.invalidate()
                },
                refreshState = refreshState
        )
    }
}

