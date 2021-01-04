package com.udacity.asteroidradar.fragment.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ListItemAsteroidBinding
import com.udacity.asteroidradar.domain.Asteroid

//code from byte application of udacity training.
class AsteroidAdapter(val onclickListener: AsteroidOnClickListener) :
    ListAdapter<Asteroid, AsteroidAdapter.ViewHolder>(AsteroidDiffCallback()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.bind(asteroid!!, onclickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

    }

    class ViewHolder private constructor(val binding: ListItemAsteroidBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            asteroid: Asteroid,
            clickOnClickListener: AsteroidOnClickListener
        ) {
            binding.asteroid = asteroid
            binding.clickListener = clickOnClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAsteroidBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class AsteroidOnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}