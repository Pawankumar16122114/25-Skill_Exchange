package com.skillexchange.app.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skillexchange.app.databinding.ItemMessageReceivedBinding
import com.skillexchange.app.databinding.ItemMessageSentBinding
import com.skillexchange.app.model.Message
import com.skillexchange.app.util.formatTime

class MessageAdapter(private val currentUserId: String) :
    ListAdapter<Message, RecyclerView.ViewHolder>(DiffCallback) {

    companion object {
        const val VIEW_TYPE_SENT = 1
        const val VIEW_TYPE_RECEIVED = 2

        val DiffCallback = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(old: Message, new: Message) = old.id == new.id
            override fun areContentsTheSame(old: Message, new: Message) = old == new
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).senderId == currentUserId) VIEW_TYPE_SENT
        else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            SentViewHolder(
                ItemMessageSentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        } else {
            ReceivedViewHolder(
                ItemMessageReceivedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        when (holder) {
            is SentViewHolder -> holder.bind(message)
            is ReceivedViewHolder -> holder.bind(message)
        }
        // Slide-in animation
        holder.itemView.alpha = 0f
        holder.itemView.translationX = if (getItemViewType(position) == VIEW_TYPE_SENT) 40f else -40f
        holder.itemView.animate()
            .alpha(1f).translationX(0f).setDuration(200).start()
    }

    inner class SentViewHolder(private val binding: ItemMessageSentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(msg: Message) {
            binding.tvMessage.text = msg.text
            binding.tvTime.text = msg.timestamp.formatTime()
        }
    }

    inner class ReceivedViewHolder(private val binding: ItemMessageReceivedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(msg: Message) {
            binding.tvSenderName.text = msg.senderName
            binding.tvMessage.text = msg.text
            binding.tvTime.text = msg.timestamp.formatTime()
        }
    }
}
