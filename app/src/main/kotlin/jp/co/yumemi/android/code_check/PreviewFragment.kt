/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import jp.co.yumemi.android.code_check.MainActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.databinding.FragmentPreviewBinding
import jp.co.yumemi.android.code_check.view_model.RepositoryViewModel

class PreviewFragment : Fragment() {

    private val args: PreviewFragmentArgs by navArgs()

    private lateinit var binding: FragmentPreviewBinding
    private lateinit var viewModel: RepositoryViewModel
    //private val _binding get() = binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_preview,container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("検索した日時", lastSearchDate.toString())


        viewModel= ViewModelProvider(this)[RepositoryViewModel()::class.java]
        binding.repositoryVM=viewModel
        binding.lifecycleOwner=this

        viewModel.setRepository(args.repository)

        viewModel.repository.observe(viewLifecycleOwner){
it?.let { Glide.with(this).load(it.owner.avatarUrl).into(binding.ownerIconView) }
        }


        /*_binding.ownerIconView.load(resultItem.owner.avatarUrl)
        _binding.nameView.text = resultItem.name
        _binding.languageView.text = resultItem.language
        _binding.starsView.text = getString(R.string.stars_count, resultItem.stargazersCount)
        _binding.watchersView.text = "${resultItem.watchersCount} watchers"
        _binding.forksView.text = "${resultItem.forksCount} forks"
        _binding.openIssuesView.text = "${resultItem.openIssuesCount} open issues"*/
    }
}
