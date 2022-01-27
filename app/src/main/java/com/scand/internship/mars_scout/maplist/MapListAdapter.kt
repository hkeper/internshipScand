package com.scand.internship.mars_scout.maplist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.scand.internship.mars_scout.databinding.ViewholderMapListBinding
import com.scand.internship.mars_scout.models.GameMap


class MapListAdapter() : ListAdapter<GameMap,
        MapListAdapter.MapListViewHolder>(MapListDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapListViewHolder {
        return MapListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MapListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object MapListDiffCallback : DiffUtil.ItemCallback<GameMap>() {
        override fun areItemsTheSame(oldItem: GameMap, newItem: GameMap): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GameMap, newItem: GameMap): Boolean {
            return oldItem == newItem
        }
    }

    class MapListListener(val clickListener: (map: GameMap) -> Unit) {
        fun onClick(map: GameMap) = clickListener(map)
    }

    class MapListViewHolder(val binding: ViewholderMapListBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup) = MapListViewHolder(
                ViewholderMapListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        fun bind(map: GameMap) {
            binding.map = map
            binding.executePendingBindings()
        }
    }

}