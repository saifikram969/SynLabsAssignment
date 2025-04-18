package com.example.synlabsassignment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.synlabsassignment.databinding.ItemPlaceBinding
import com.example.synlabsassignment.models.Place
import com.example.synlabsassignment.R

class PlacesAdapter(private val onClick: (Place) -> Unit) :
    ListAdapter<Place, PlacesAdapter.PlaceViewHolder>(PlaceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemPlaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaceViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PlaceViewHolder(
        private val binding: ItemPlaceBinding,
        private val onClick: (Place) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(place: Place) {
            binding.placeName.text = place.name
            binding.placeDescription.text = place.description
            binding.placeRating.rating = place.rating.toFloat() / 2

            Glide.with(binding.root.context)
                .load(place.imageUrl)
                .placeholder(R.drawable.paris)
                .into(binding.placeImage)

            binding.root.setOnClickListener {
                onClick(place)
            }
        }
    }
}

class PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }
}
