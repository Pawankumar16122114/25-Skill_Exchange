package com.skillexchange.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.skillexchange.app.R
import com.skillexchange.app.databinding.FragmentHomeBinding
import com.skillexchange.app.model.NeedPost
import com.skillexchange.app.ui.offer.OfferBottomSheet
import com.skillexchange.app.util.hide
import com.skillexchange.app.util.show
import com.skillexchange.app.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: NeedPostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFilters()
        setupSearch()
        observePosts()
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener { text ->
            viewModel.setSearchQuery(text?.toString() ?: "")
        }
    }

    private fun setupRecyclerView() {
        adapter = NeedPostAdapter(
            onOfferClick = { post -> showOfferSheet(post) },
            onChatClick = { swapId, otherName, postTitle ->
                findNavController().navigate(
                    R.id.action_home_to_chat,
                    bundleOf(
                        "swapId" to swapId,
                        "otherUserName" to otherName,
                        "postTitle" to postTitle
                    )
                )
            }
        )
        binding.rvPosts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
            setHasFixedSize(false)
        }
    }

    private fun setupFilters() {
        val skills = resources.getStringArray(R.array.skills_list).toMutableList()
        skills.add(0, "All")

        skills.forEach { skill ->
            val chip = Chip(requireContext()).apply {
                text = skill
                isCheckable = true
                isChecked = skill == "All"
                setChipBackgroundColorResource(R.color.chip_bg)
                setChipStrokeColorResource(R.color.chip_stroke)
                setTextColor(resources.getColor(R.color.chip_text, null))
                chipStrokeWidth = 2f
            }
            chip.setOnClickListener {
                // Uncheck all siblings
                for (i in 0 until binding.chipGroupFilter.childCount) {
                    (binding.chipGroupFilter.getChildAt(i) as? Chip)?.isChecked = false
                }
                chip.isChecked = true
                viewModel.setFilter(skill)
            }
            binding.chipGroupFilter.addView(chip)
        }
    }

    private fun observePosts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { loading ->
                if (loading) {
                    binding.shimmerLayout.show()
                    binding.shimmerLayout.startShimmer()
                    binding.rvPosts.hide()
                } else {
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.hide()
                    binding.rvPosts.show()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.posts.collect { posts ->
                adapter.submitList(posts)
                if (posts.isEmpty()) {
                    binding.tvEmpty.show()
                    binding.rvPosts.hide()
                } else {
                    binding.tvEmpty.hide()
                    binding.rvPosts.show()
                }
            }
        }
    }

    private fun showOfferSheet(post: NeedPost) {
        OfferBottomSheet.newInstance(post).show(childFragmentManager, "offer")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
