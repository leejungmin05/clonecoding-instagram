package com.example.cloneinstagram.main.user

import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cloneinstagram.model.ContentDTO
import kotlinx.android.synthetic.main.fragment_user.view.*

class UserRecyclerAdapter(
    private val contentDTOs: ArrayList<ContentDTO>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val width = parent.context.resources.displayMetrics.widthPixels /3

        val imageView = ImageView(parent.context)
        imageView.layoutParams = LinearLayoutCompat.LayoutParams(width,width)
        return CustomViewHolder(imageView)
    }
    inner class CustomViewHolder(var imageview: ImageView) : RecyclerView.ViewHolder(imageview){

    }

    override fun getItemCount(): Int {
        return contentDTOs.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var imageview = (holder as CustomViewHolder).imageview
        Glide.with(holder.itemView.context).load(contentDTOs[position].imageUrl).apply(
            RequestOptions().centerCrop()).into(imageview)
    }


}