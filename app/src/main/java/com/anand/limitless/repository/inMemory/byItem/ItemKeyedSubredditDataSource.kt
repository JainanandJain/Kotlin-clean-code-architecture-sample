package com.anand.limitless.repository.inMemory.byItem

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import com.anand.limitless.api.StateApi
import com.anand.limitless.repository.NetworkState
import com.anand.limitless.vo.StateName
import java.io.IOException
import java.util.concurrent.Executor

/**
 * A data source that uses the "name" field of posts as the key for next/prev pages.
 * <p>
 * Note that this is not the correct consumption of the Reddit API but rather shown here as an
 * alternative implementation which might be more suitable for your backend.
 * see PageKeyedSubredditDataSource for the other sample.
 */
class ItemKeyedSubredditDataSource(
    private val stateApi: StateApi,
    private val subredditName: String,
    private val retryExecutor: Executor)
    : ItemKeyedDataSource<String, StateName>() {
    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter and we don't support loadBefore
     * in this example.
     * <p>
     * See BoundaryCallback example for a more complete example on syncing multiple network states.
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

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<StateName>) {
        // ignored, since we only ever append to our initial load
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<StateName>) {
        // ignored, only for demo purpose
    }

    /**
     * The name field is a unique identifier for post items.
     * (no it is not the title of the post :) )
     * https://www.reddit.com/dev/api
     */
    override fun getKey(item: StateName): String = item.name!!

    override fun loadInitial(
            params: LoadInitialParams<String>,
            callback: LoadInitialCallback<StateName>) {
        val request = stateApi.getTop(
                limit = params.requestedLoadSize
        )
        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        // triggered by a refresh, we better execute sync
        try {
            val response = request.execute()

            val items = response.body()?.map { it } ?: emptyList()
            retry = null
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            callback.onResult(items)
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