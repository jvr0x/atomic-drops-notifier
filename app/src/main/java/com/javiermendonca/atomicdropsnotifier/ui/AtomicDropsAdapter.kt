package com.javiermendonca.atomicdropsnotifier.ui

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.javiermendonca.atomicdropsnotifier.R
import com.javiermendonca.atomicdropsnotifier.core.extensions.bind
import com.javiermendonca.atomicdropsnotifier.data.repository.models.AtomicDropItem
import com.javiermendonca.atomicdropsnotifier.databinding.AtomicDropItemBinding


class AtomicDropsAdapter : RecyclerView.Adapter<AtomicDropsAdapter.AtomicDropViewHolder>() {

    private var data: List<AtomicDropItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AtomicDropViewHolder(parent.bind(R.layout.atomic_drop_item))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: AtomicDropViewHolder, position: Int) =
        holder.bind(data[position])

    fun setAtomicDrops(rows: List<AtomicDropItem>) {
        data = rows
        notifyDataSetChanged()
    }

    class AtomicDropViewHolder(private val binding: AtomicDropItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(atomicDropItem: AtomicDropItem) = with(itemView.context) {
            binding.atomicDropItem = atomicDropItem
            binding.atomicDropContainer.setOnClickListener {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        getString(
                            R.string.atomichub_drop_link,
                            atomicDropItem.dropId
                        )
                    )
                )
                startActivity(browserIntent)
            }
        }
    }
}