package com.example.cloneinstagram.main.grid


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cloneinstagram.R
import com.example.cloneinstagram.model.ContentDTO

class GridRecyclerAdapter(
    private val contentDTOs: ArrayList<ContentDTO>
) : RecyclerView.Adapter<GridRecyclerAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(contentDTOs)
    }

    override fun getItemCount(): Int {
        return contentDTOs.size
    }

    class CustomViewHolder private constructor(holder: View) : RecyclerView.ViewHolder(holder) {
        private val context = itemView.context
        private val mainImageView = ImageView(context)
        private var width: Int = context.resources.displayMetrics.widthPixels / 3

        fun bind(contentDTOs: ArrayList<ContentDTO>) {
            mainImageView.layoutParams = LinearLayoutCompat.LayoutParams(width, width)
            Glide.with(context).load(contentDTOs[adapterPosition].imageUrl)
                .apply(RequestOptions().centerCrop())
                .into(mainImageView)
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




