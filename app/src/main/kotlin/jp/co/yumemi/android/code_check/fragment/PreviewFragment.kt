/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.databinding.FragmentPreviewBinding
import jp.co.yumemi.android.code_check.view_model.RepositoryViewModel

/**
 * Fragment that displays a preview of a repository.
 */
@AndroidEntryPoint
class PreviewFragment : Fragment() {
    private val args: PreviewFragmentArgs by navArgs()
    private lateinit var binding: FragmentPreviewBinding
    private lateinit var viewModel: RepositoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this)[RepositoryViewModel::class.java]

        // Set the ViewModel for data binding.
        binding.repositoryVM = viewModel

        // Set the lifecycle owner for observing LiveData.
        binding.lifecycleOwner = viewLifecycleOwner

        // Set the repository data in the ViewModel.
        viewModel.setRepository(args.repository)
        Log.d("udeni",args.repository.toString())

        // Observe changes in the repository data and load the owner's avatar using Glide.
        viewModel.repository.observe(viewLifecycleOwner) {
            it?.let { Glide.with(this).load(it.owner.avatarUrl).into(binding.ownerIconView) }
        }
    }
}
