package com.anand.limitless.repository.inDb

import androidx.annotation.MainThread
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import com.anand.limitless.api.StateApi
import com.anand.limitless.util.createStatusLiveData
import com.anand.limitless.vo.StateName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

/**
 * This boundary callback gets notified when user reaches to the edges of the list such that the
 * database cannot provide any more data.
 * <p>
 * The boundary callback might be called multiple times for the same direction so it does its own
 * rate limiting using the PagingRequestHelper class.
 */
class SubredditBoundaryCallback(
    private val subredditName: String,
    private val webservice: StateApi,
    private val handleResponse: (String, List<StateName>?) -> Unit,
    private val ioExecutor: Executor,
    private val networkPageSize: Int)
    : PagedList.BoundaryCallback<StateName>() {

    val helper = PagingRequestHelper(ioExecutor)
    val networkState = helper.createStatusLiveData()

    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    @MainThread
    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            webservice.getTop(
                    limit = networkPageSize)
                    .enqueue(createWebserviceCallback(it))
        }
    }

    /**
     * User reached to the end of the list.
     */
    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: StateName) {
        // ignored for demo purpose
    }

    /**
     * every time it gets new items, boundary callback simply inserts them into the database and
     * paging library takes care of refreshing the list if necessary.
     */
    private fun insertItemsIntoDb(
        response: Response<List<StateName>>,
        it: PagingRequestHelper.Request.Callback) {
        ioExecutor.execute {
            handleResponse(subredditName, response.body())
            it.recordSuccess()
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: StateName) {
        // ignored, since we only ever append to what's in the DB
    }

    private fun createWebserviceCallback(it: PagingRequestHelper.Request.Callback)
            : Callback<List<StateName>> {
        return object : Callback<List<StateName>> {
            override fun onFailure(
                    call: Call<List<StateName>>,
                    t: Throwable) {
                it.recordFailure(t)
            }

            override fun onResponse(
                    call: Call<List<StateName>>,
                    response: Response<List<StateName>>) {
                insertItemsIntoDb(response, it)
            }
        }
    }
}