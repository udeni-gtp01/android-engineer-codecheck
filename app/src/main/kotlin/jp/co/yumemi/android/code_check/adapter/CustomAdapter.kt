package jp.co.yumemi.android.code_check.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.code_check.databinding.LayoutResultItemBinding
import jp.co.yumemi.android.code_check.model.RepositoryItem

class CustomAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<RepositoryItem, CustomAdapter.ViewHolder>(diff_util) {
    interface OnItemClickListener {
        fun itemClick(item: RepositoryItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutResultItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repositoryItem = getItem(position)
        holder.bind(repositoryItem)
    }

    inner class ViewHolder(private val binding: LayoutResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val repositoryItem = getItem(position)
                    itemClickListener.itemClick(repositoryItem)
                }
            }
        }

        fun bind(repositoryItem: RepositoryItem) {
            binding.repositoryNameView.text = repositoryItem.name
        }
    }

    companion object {
        val diff_util = object : DiffUtil.ItemCallback<RepositoryItem>() {
            override fun areItemsTheSame(
                oldItem: RepositoryItem,
                newItem: RepositoryItem
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: RepositoryItem,
                newItem: RepositoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
