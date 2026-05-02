package com.skillexchange.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillexchange.app.model.NeedPost
import com.skillexchange.app.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val postRepo = PostRepository()

    private val _allPosts = MutableStateFlow<List<NeedPost>>(emptyList())
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _posts = MutableStateFlow<List<NeedPost>>(emptyList())
    val posts: StateFlow<List<NeedPost>> = _posts

    init {
        // Observe ALL posts
        viewModelScope.launch {
            postRepo.observePosts(null)
                .catch { _isLoading.value = false }
                .collect { posts ->
                    _allPosts.value = posts
                    applyFilters()
                    _isLoading.value = false
                }
        }
    }

    fun setFilter(skill: String) {
        _selectedFilter.value = skill
        applyFilters()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    private fun applyFilters() {
        val filter = _selectedFilter.value
        val query = _searchQuery.value.lowercase().trim()

        var filtered = _allPosts.value

        if (filter != "All") {
            filtered = filtered.filter { it.skillRequired == filter }
        }

        if (query.isNotEmpty()) {
            filtered = filtered.filter {
                it.title.lowercase().contains(query) || 
                it.description.lowercase().contains(query) ||
                it.skillRequired.lowercase().contains(query)
            }
        }

        _posts.value = filtered
    }
}
