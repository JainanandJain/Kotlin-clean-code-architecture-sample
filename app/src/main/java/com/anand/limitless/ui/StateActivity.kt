package com.anand.limitless.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.anand.limitless.GlideApp
import com.anand.limitless.R
import com.anand.limitless.ServiceLocator
import com.anand.limitless.repository.NetworkState
import com.anand.limitless.repository.inDb.StatePostRepository
import com.anand.limitless.vo.StateName
import kotlinx.android.synthetic.main.activity_state.*


/**
 * A list activity that shows reddit posts in the given sub-reddit.
 * <p>
 * The intent arguments can be modified to make it use a different repository (see MainActivity).
 */
class StateActivity : AppCompatActivity() {
    companion object {
        const val KEY_SUBREDDIT = "subreddit"
        const val DEFAULT_SUBREDDIT = "androiddev"
        const val KEY_REPOSITORY_TYPE = "repository_type"
        fun intentFor(context: Context, type: StatePostRepository.Type): Intent {
            val intent = Intent(context, StateActivity::class.java)
            intent.putExtra(KEY_REPOSITORY_TYPE, type.ordinal)
            return intent
        }
    }

    private val model: SubStateViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val repoTypeParam = intent.getIntExtra(KEY_REPOSITORY_TYPE, 0)
                val repoType = StatePostRepository.Type.values()[repoTypeParam]
                val repo = ServiceLocator.instance(this@StateActivity)
                        .getRepository(repoType)
                @Suppress("UNCHECKED_CAST")
                return SubStateViewModel(repo) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state)
        initAdapter()
        initSwipeToRefresh()
        initSearch()
        val subreddit = savedInstanceState?.getString(KEY_SUBREDDIT) ?: DEFAULT_SUBREDDIT
        model.showSubreddit(subreddit)
    }

    private fun initAdapter() {
        val glide = GlideApp.with(this)
        val adapter = PostsAdapter(glide) {
            model.retry()
        }
        list.adapter = adapter
        model.posts.observe(this, Observer<PagedList<StateName>> {
            adapter.submitList(it)
        })
        model.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })
    }

    private fun initSwipeToRefresh() {
        model.refreshState.observe(this, Observer {
            swipe_refresh.isRefreshing = it == NetworkState.LOADING
        })
        swipe_refresh.setOnRefreshListener {
            model.refresh()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SUBREDDIT, model.currentSubreddit())
    }

    private fun initSearch() {
        /*input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updatedSubredditFromInput()
                true
            } else {
                false
            }
        }
        input.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updatedSubredditFromInput()
                true
            } else {
                false
            }
        }*/
    }

    private fun updatedSubredditFromInput() {
        /*input.text.trim().toString().let {
            if (it.isNotEmpty()) {
                if (model.showSubreddit(it)) {
                    list.scrollToPosition(0)
                    (list.adapter as? PostsAdapter)?.submitList(null)
                }
            }
        }*/
    }
}
