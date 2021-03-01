package com.example.cloneinstagram.main.grid

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cloneinstagram.R
import com.example.cloneinstagram.extensions.getGridImage
import com.example.cloneinstagram.main.alarm.AlarmRecyclerAdapter.CustomViewHolder.Companion.from
import com.example.cloneinstagram.model.ContentDTO
import com.example.cloneinstagram.model.FirebaseRepository
import kotlinx.android.synthetic.main.fragment_grid.view.*


class GridRecyclerAdapter(
    private var contentDTOs: ArrayList<ContentDTO>
) : RecyclerView.Adapter<GridRecyclerAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val width = parent.context.resources.displayMetrics.widthPixels / 3
        val imageView = ImageView(parent.context)
        imageView.layoutParams = LinearLayoutCompat.LayoutParams(width, width)
        return CustomViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(contentDTOs[position])
    }

    override fun getItemCount(): Int {
        return contentDTOs.size
    }

    class CustomViewHolder private constructor(val imageview: ImageView) :
        RecyclerView.ViewHolder(imageview) {
        private val gridImageView: ImageView = imageview
        fun bind(contentDTO: ContentDTO) {
            gridImageView.getGridImage(contentDTO.imageUrl!!)

        }

        companion object {
            fun from(parent: ViewGroup): CustomViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_grid, parent, false)
                return CustomViewHolder(view)
            }
        }
    }
}



