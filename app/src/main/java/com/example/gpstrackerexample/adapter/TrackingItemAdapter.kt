package com.example.gpstrackerexample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gpstrackerexample.data.TrackingItem
import com.example.gpstrackerexample.databinding.ItemTrackingBinding

class TrackingItemAdapter : RecyclerView.Adapter<TrackingItemAdapter.ViewHolder>() {

    val trackingList = ArrayList<TrackingItem>()

    lateinit var onClickItem : OnItemClickCallback

    inner class ViewHolder(val binding: ItemTrackingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTrackingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = trackingList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(trackingList[position]){
                binding.tvStartTime.text = this.startTime
                binding.tvLongitude.text = this.longitude
                binding.tvLatitude.text = this.latitude

                binding.root.setOnClickListener {
                    onClickItem.onClickItem(this)
                }
            }
        }
    }
    interface OnItemClickCallback{
        fun onClickItem(item : TrackingItem)
    }
}