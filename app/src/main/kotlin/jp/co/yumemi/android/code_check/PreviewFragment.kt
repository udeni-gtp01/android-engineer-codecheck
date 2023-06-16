/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import jp.co.yumemi.android.code_check.MainActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.databinding.FragmentPreviewBinding

class PreviewFragment : Fragment(R.layout.fragment_preview) {

    private val args: PreviewFragmentArgs by navArgs()

    private var binding: FragmentPreviewBinding? = null
    private val _binding get() = binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("検索した日時", lastSearchDate.toString())

        binding = FragmentPreviewBinding.bind(view)

        var resultItem = args.repositoryResponse

        _binding.ownerIconView.load(resultItem.owner.avatarUrl)
        _binding.nameView.text = resultItem.name
        _binding.languageView.text = resultItem.language
        _binding.starsView.text = getString(R.string.stars_count, resultItem.stargazersCount)
        _binding.watchersView.text = "${resultItem.watchersCount} watchers"
        _binding.forksView.text = "${resultItem.forksCount} forks"
        _binding.openIssuesView.text = "${resultItem.openIssuesCount} open issues"
    }
}
