package com.skillexchange.app.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.skillexchange.app.databinding.FragmentChatBinding
import com.skillexchange.app.util.hide
import com.skillexchange.app.util.show
import com.skillexchange.app.util.showSnackbar
import com.skillexchange.app.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChatViewModel by viewModels()
    private val args: ChatFragmentArgs by navArgs()
    private lateinit var adapter: MessageAdapter
    private var currentUserName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvChatTitle.text = args.postTitle
        binding.tvSubtitle.text = "with ${args.otherUserName}"

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        setupRecyclerView()
        setupSend()
        setupConfirmButton()
        loadData()
        observeData()
    }

    private fun setupRecyclerView() {
        adapter = MessageAdapter(viewModel.currentUserId ?: "")
        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(requireContext()).also { it.stackFromEnd = true }
            adapter = this@ChatFragment.adapter
        }
    }

    private fun setupSend() {
        binding.btnSend.setOnClickListener {
            val text = binding.etMessage.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener
            viewModel.sendMessage(args.swapId, text, currentUserName)
            binding.etMessage.setText("")
            // Animate send button
            binding.btnSend.animate().scaleX(0.85f).scaleY(0.85f).setDuration(80).withEndAction {
                binding.btnSend.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
            }.start()
        }
    }

    private fun setupConfirmButton() {
        binding.btnConfirmSwap.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Confirm Swap Complete?")
                .setMessage("This will update both parties' Trust Score and transfer Skill Points. This cannot be undone.")
                .setPositiveButton("Yes, Confirm") { _, _ ->
                    val swap = viewModel.swap.value
                    val isRequester = swap?.requesterId == viewModel.currentUserId
                    viewModel.confirmSwap(args.swapId, isRequester)
                }
                .setNegativeButton("Not yet", null)
                .show()
        }
    }

    private fun loadData() {
        viewModel.loadMessages(args.swapId)
        viewModel.observeSwap(args.swapId)
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messages.collect { messages ->
                adapter.submitList(messages) {
                    if (messages.isNotEmpty()) {
                        binding.rvMessages.smoothScrollToPosition(messages.size - 1)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.swap.collect { swap ->
                if (swap != null) {
                    if (swap.isCompleted) {
                        binding.btnConfirmSwap.hide()
                        binding.tvSwapCompleted.show()
                        binding.etMessage.isEnabled = false
                        binding.btnSend.isEnabled = false
                    } else {
                        binding.btnConfirmSwap.show()
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.confirmState.collect { state ->
                when (state) {
                    "confirmed" -> showSnackbar("Swap confirmed! 🎉 Trust Score updated!")
                    "error" -> showSnackbar("Failed to confirm. Try again.")
                    else -> {}
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
