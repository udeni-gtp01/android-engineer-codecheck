package jp.co.yumemi.android.code_check.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.code_check.databinding.LayoutResultItemBinding
import jp.co.yumemi.android.code_check.model.RepositoryItem

/**
 * RepositoryAdapter is a RecyclerView adapter that displays a list of RepositoryItem objects.
 * It handles the creation and binding of ViewHolders for efficient recycling and updates.
 *
 * @param itemClickListener The click listener for item interactions.
 */
class RepositoryAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<RepositoryItem, RepositoryAdapter.ViewHolder>(diff_util) {

    /**
     * Interface definition for a callback to be invoked when an item is clicked.
     */
    interface OnItemClickListener {
        /**
         * Called when an item is clicked.
         *
         * @param item The clicked RepositoryItem object.
         */
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

    /**
     * ViewHolder represents an item view in the RecyclerView.
     *
     * @param binding The layout binding for the item view.
     */
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
                oldItem: RepositoryItem, newItem: RepositoryItem
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: RepositoryItem, newItem: RepositoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}