package com.anand.limitless.repository.inDb

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import com.anand.limitless.api.StateApi
import com.anand.limitless.db.StateDb
import com.anand.limitless.repository.Listing
import com.anand.limitless.repository.NetworkState
import com.anand.limitless.vo.StateName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

/**
 * Repository implementation that uses a database PagedList + a boundary callback to return a
 * listing that loads in pages.
 */
class DbStatePostRepository(
    val db: StateDb,
    private val stateApi: StateApi,
    private val ioExecutor: Executor,
    private val networkPageSize: Int = DEFAULT_NETWORK_PAGE_SIZE) : StatePostRepository {
    companion object {
        private const val DEFAULT_NETWORK_PAGE_SIZE = 10
    }

    /**
     * Inserts the response into the database while also assigning position indices to items.
     */
    private fun insertResultIntoDb(subredditName: String, body: List<StateName>?) {
        db.runInTransaction {
            // val start = db.posts().getNextIndexInSubreddit(subredditName)
            body?.let { db.posts().insert(it) }
        }
    }

    /**
     * When refresh is called, we simply run a fresh network request and when it arrives, clear
     * the database table and insert all new items in a transaction.
     * <p>
     * Since the PagedList already uses a database bound data source, it will automatically be
     * updated after the database transaction is finished.
     */
    @MainThread
    private fun refresh(subredditName: String): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING
        stateApi.getTop(networkPageSize).enqueue(
            object : Callback<List<StateName>> {
                override fun onFailure(call: Call<List<StateName>>, t: Throwable) {
                    // retrofit calls this on main thread so safe to call set value
                    networkState.value = NetworkState.error(t.message)
                }

                override fun onResponse(
                    call: Call<List<StateName>>,
                    response: Response<List<StateName>>) {
                    ioExecutor.execute {
                        db.runInTransaction {
                            insertResultIntoDb(subredditName, response.body())
                        }
                        // since we are in bg thread now, post the result.
                        networkState.postValue(NetworkState.LOADED)
                    }
                }
            }
        )
        return networkState
    }

    /**
     * Returns a Listing for the given subreddit.
     */
    @MainThread
    override fun postsOfSubreddit(subReddit: String, pageSize: Int): Listing<StateName> {
        // create a boundary callback which will observe when the user reaches to the edges of
        // the list and update the database with extra data.
        val boundaryCallback = SubredditBoundaryCallback(
            webservice = stateApi,
            subredditName = subReddit,
            handleResponse = this::insertResultIntoDb,
            ioExecutor = ioExecutor,
            networkPageSize = networkPageSize)
        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            refresh(subReddit)
        }

        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
        val livePagedList = db.posts().postsByState().toLiveData(
            pageSize = pageSize,
            boundaryCallback = boundaryCallback)

        return Listing(
            pagedList = livePagedList,
            networkState = boundaryCallback.networkState,
            retry = {
                boundaryCallback.helper.retryAllFailed()
            },
            refresh = {
                refreshTrigger.value = null
            },
            refreshState = refreshState
        )
    }
}

