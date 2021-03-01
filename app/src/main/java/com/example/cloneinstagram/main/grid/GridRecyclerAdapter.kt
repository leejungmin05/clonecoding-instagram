package com.example.cloneinstagram.main.grid


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
        val width = parent.context.resources.displayMetrics.widthPixels / 3
        val mainImageView = ImageView(parent.context)
        mainImageView.layoutParams = LinearLayoutCompat.LayoutParams(width, width)
        return CustomViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(contentDTOs[position])
    }

    override fun getItemCount(): Int {
        return contentDTOs.size
    }

    class CustomViewHolder private constructor(holder: View) : RecyclerView.ViewHolder(holder) {
        fun bind(contentDTO: ContentDTO) {
            val context = itemView.context

            Glide.with(context).load(contentDTO.imageUrl)
                .apply(RequestOptions().centerCrop())
                .into(ImageView(context))
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




