/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.adapter.CustomAdapter
import jp.co.yumemi.android.code_check.databinding.FragmentHomeBinding
import jp.co.yumemi.android.code_check.databinding.FragmentPreviewBinding
import jp.co.yumemi.android.code_check.databinding.LayoutResultItemBinding
import jp.co.yumemi.android.code_check.model.RepositoryItem
import jp.co.yumemi.android.code_check.view_model.RepositoryViewModel
import java.util.Date

@AndroidEntryPoint
//class HomeFragment : Fragment(R.layout.fragment_home) {
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: RepositoryViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        viewModel= ViewModelProvider(requireActivity())[RepositoryViewModel::class.java]
        binding.repositoryVM2 =viewModel
        binding.lifecycleOwner=this
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val binding = FragmentHomeBinding.bind(view)

       // val viewModel = RepositoryViewModelTemp(requireContext())

        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), layoutManager.orientation)
        val adapter = CustomAdapter(object : CustomAdapter.OnItemClickListener {
            override fun itemClick(item: RepositoryItem) {
                gotoRepositoryFragment(item)
            }
        })

        binding.searchInputText
            .setOnEditorActionListener { editText, action, _ ->
                if (action == EditorInfo.IME_ACTION_SEARCH) {
                    editText.text.toString().let {
                        MainActivity.lastSearchDate = Date()
                        viewModel.getRepositoryList(it)
                       //     .apply {
                       //     adapter.submitList(this)
                      //  }
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

binding.recyclerView.adapter=adapter

        binding.recyclerView.also {
            it.layoutManager = layoutManager
            it.addItemDecoration(dividerItemDecoration)
            it.adapter = adapter
        }
        //viewModel.repositoryList.observe(viewLifecycleOwner){
        viewModel.repositoryList.observe(requireActivity()) {
            println("******************"+ it?.size)
            adapter.submitList(it)
        }
    }

    fun gotoRepositoryFragment(repositoryItem: RepositoryItem) {
        val action = HomeFragmentDirections
            .actionHomeFragmentToPreviewFragment(repository = repositoryItem)
        findNavController().navigate(action)
    }
}

val diff_util = object : DiffUtil.ItemCallback<RepositoryItem>() {
    override fun areItemsTheSame(oldItem: RepositoryItem, newItem: RepositoryItem): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: RepositoryItem, newItem: RepositoryItem): Boolean {
        return oldItem == newItem
    }

}


