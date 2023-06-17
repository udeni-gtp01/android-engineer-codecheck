package jp.co.yumemi.android.code_check.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.code_check.databinding.LayoutResultItemBinding
import jp.co.yumemi.android.code_check.diff_util
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
        val gitHubAccountItem = getItem(position)

        holder.binding.repositoryNameView.text = gitHubAccountItem.name
    }

    inner class ViewHolder(val binding: LayoutResultItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                itemClickListener.itemClick(getItem(absoluteAdapterPosition))
            }
        }
    }
   /* class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface OnItemClickListener {
        fun itemClick(item: RepositoryItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // .inflate(R.layout.layout_result_item, parent, false)
        val binding = LayoutResultItemBinding.inflate(inflater, parent, false)
        return customViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resultItem = getItem(position)
        //(holder.itemView.findViewById<View>(R.id.repositoryNameView) as TextView).text =
        //resultItem.name
        holder.binding.repositoryNameView.text = gitHubAccountItem.name

    }
    inner class ViewHolder(val binding: LayoutResultItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            /*itemView.setOnClickListener {
                itemClickListener.itemClick(getItem(absoluteAdapterPosition))
            }*/
            holder.itemView.setOnClickListener {
                itemClickListener.itemClick(resultItem)
            }
        }
    }*/
}