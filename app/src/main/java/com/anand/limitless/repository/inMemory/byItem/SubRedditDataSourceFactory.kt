package com.anand.limitless.repository.inMemory.byItem

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.anand.limitless.api.StateApi
import com.anand.limitless.vo.StateName
import java.util.concurrent.Executor

/**
 * A simple data source factory which also provides a way to observe the last created data source.
 * This allows us to channel its network request status etc back to the UI. See the Listing creation
 * in the Repository class.
 */
class SubRedditDataSourceFactory(
    private val stateApi: StateApi,
    private val subredditName: String,
    private val retryExecutor: Executor) : DataSource.Factory<String, StateName>() {
    val sourceLiveData = MutableLiveData<ItemKeyedSubredditDataSource>()
    override fun create(): DataSource<String, StateName> {
        val source = ItemKeyedSubredditDataSource(stateApi, subredditName, retryExecutor)
        sourceLiveData.postValue(source)
        return source
    }
}
