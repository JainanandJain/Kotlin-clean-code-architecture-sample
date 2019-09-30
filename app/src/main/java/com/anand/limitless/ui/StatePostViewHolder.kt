package com.anand.limitless.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anand.limitless.GlideRequests
import com.anand.limitless.R
import com.anand.limitless.vo.StateName


/**
 * A RecyclerView ViewHolder that displays a state post.
 */
class StatePostViewHolder(view: View, private val glide: GlideRequests)
    : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.title)
    private val subtitle: TextView = view.findViewById(R.id.subtitle)
    private val score: TextView = view.findViewById(R.id.score)
    private val thumbnail : ImageView = view.findViewById(R.id.thumbnail)
    private var post : StateName? = null


    fun bind(post: StateName?) {
        this.post = post
        title.text = post?.name ?: "loading"
        subtitle.text = itemView.context.resources.getString(R.string.post_subtitle,
                post?.capital ?: "unknown")
        if (post?.flag?.startsWith("http") == true) {
            thumbnail.visibility = View.VISIBLE
            glide.load(post.flag)
                    .centerCrop()
                    .placeholder(R.drawable.ic_insert_photo_black_48dp)
                    .into(thumbnail)
        } else {
            thumbnail.visibility = View.GONE
            glide.clear(thumbnail)
        }
    }

    companion object {
        fun create(parent: ViewGroup, glide: GlideRequests): StatePostViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.state_post_item, parent, false)
            return StatePostViewHolder(view, glide)
        }
    }

    fun updateScore(item: StateName?) {
        post = item
        score.text = "${item?.score ?: 0}"
    }
}