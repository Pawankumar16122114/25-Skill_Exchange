package com.skillexchange.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skillexchange.app.databinding.ItemNeedPostBinding
import com.skillexchange.app.model.NeedPost
import com.skillexchange.app.model.PostStatus
import com.skillexchange.app.util.formatRelative
import com.skillexchange.app.util.toInitials
import com.skillexchange.app.R

class NeedPostAdapter(
    private val onOfferClick: (NeedPost) -> Unit,
    private val onChatClick: (swapId: String, otherName: String, postTitle: String) -> Unit
) : ListAdapter<NeedPost, NeedPostAdapter.PostViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemNeedPostBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
        // Stagger animation
        holder.itemView.alpha = 0f
        holder.itemView.translationY = 40f
        holder.itemView.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(300)
            .setStartDelay((position * 60L).coerceAtMost(300L))
            .start()
    }

    inner class PostViewHolder(private val binding: ItemNeedPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: NeedPost) {
            binding.tvTitle.text = post.title
            binding.tvDescription.text = post.description
            binding.tvAuthorName.text = post.authorName
            binding.tvTime.text = post.timestamp.formatRelative()
            binding.tvSkillRequired.text = post.skillRequired
            binding.tvOfferCount.text = "${post.offerCount} offers"

            // Avatar initials
            binding.tvAvatarInitials.text = post.authorName.toInitials()

            // Status chip color
            val statusColor = when (post.status) {
                PostStatus.OPEN.name -> R.color.status_open
                PostStatus.IN_PROGRESS.name -> R.color.status_in_progress
                PostStatus.COMPLETED.name -> R.color.status_completed
                else -> R.color.status_cancelled
            }
            binding.chipStatus.setChipBackgroundColorResource(statusColor)
            binding.chipStatus.text = post.status.replace("_", " ")

            // Only show offer button for open posts
            binding.btnMakeOffer.isEnabled = post.status == PostStatus.OPEN.name

            binding.btnMakeOffer.setOnClickListener {
                // Button pulse animation
                it.animate().scaleX(0.95f).scaleY(0.95f).setDuration(80).withEndAction {
                    it.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
                }.start()
                onOfferClick(post)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<NeedPost>() {
        override fun areItemsTheSame(old: NeedPost, new: NeedPost) = old.id == new.id
        override fun areContentsTheSame(old: NeedPost, new: NeedPost) = old == new
    }
}
