/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.adapter.CustomAdapter
import jp.co.yumemi.android.code_check.databinding.FragmentHomeBinding
import jp.co.yumemi.android.code_check.model.RepositoryItem
import jp.co.yumemi.android.code_check.view_model.RepositoryListViewModel

/**
 * Fragment representing the home screen of the app where repository search and listing are performed.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: RepositoryListViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this)[RepositoryListViewModel::class.java]

        // Set the ViewModel for data binding.
        binding.repositoryListVM = viewModel

        // Set the lifecycle owner for observing LiveData.
        binding.lifecycleOwner = viewLifecycleOwner

        // Set up RecyclerView
        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), layoutManager.orientation)
        val adapter = CustomAdapter(object : CustomAdapter.OnItemClickListener {
            override fun itemClick(item: RepositoryItem) {
                gotoRepositoryFragment(item)
            }
        })
        binding.recyclerView.also {
            it.layoutManager = layoutManager
            it.addItemDecoration(dividerItemDecoration)
            it.adapter = adapter
        }

        // Set up search functionality
        binding.searchInputText.setOnEditorActionListener { editText, action, _ ->
            if (action == EditorInfo.IME_ACTION_SEARCH) {

                val searchQuery = editText.text.toString()
                if (searchQuery.isNotEmpty()) {
                    viewModel.getRepositoryList(searchQuery)
                } else {
                    // clear existing search result and display error message when search query is empty
                    viewModel.clearRepositoryList()
                    Toast.makeText(
                        requireContext(),
                        R.string.info_empty_search,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        // Observe search result and update the adapter
        viewModel.repositoryList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    /**
     * Navigate to the [PreviewFragment] and pass the selected [repositoryItem] as an argument
     * to view the repository details
     */
    fun gotoRepositoryFragment(repositoryItem: RepositoryItem) {
        val action =
            HomeFragmentDirections.actionHomeFragmentToPreviewFragment(repository = repositoryItem)
        findNavController().navigate(action)
    }
}




