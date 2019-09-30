package com.anand.limitless.repository.inMemory.byPage

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.anand.limitless.api.StateApi
import com.anand.limitless.repository.NetworkState
import com.anand.limitless.vo.StateName
import java.io.IOException
import java.util.concurrent.Executor

/**
 * A data source that uses the before/after keys returned in page requests.
 * <p>
 * See ItemKeyedSubredditDataSource
 */
class PageKeyedSubredditDataSource(
    private val stateApi: StateApi,
    private val subredditName: String,
    private val retryExecutor: Executor) : PageKeyedDataSource<String, StateName>() {

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter.
     */
    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadBefore(
            params: LoadParams<String>,
            callback: LoadCallback<String, StateName>) {
        // ignored, since we only ever append to our initial load
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, StateName>) {
        // ignored for demo purpose
    }

    override fun loadInitial(
            params: LoadInitialParams<String>,
            callback: LoadInitialCallback<String, StateName>) {
        val request = stateApi.getTop(
                limit = params.requestedLoadSize
        )
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        // triggered by a refresh, we better execute sync
        try {
            val response = request.execute()

            val data = response.body()
            val items = data?.map { it } ?: emptyList()
            retry = null
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            callback.onResult(items, "0", "10")
        } catch (ioException: IOException) {
            retry = {
                loadInitial(params, callback)
            }
            val error = NetworkState.error(ioException.message ?: "unknown error")
            networkState.postValue(error)
            initialLoad.postValue(error)
        }
    }
}