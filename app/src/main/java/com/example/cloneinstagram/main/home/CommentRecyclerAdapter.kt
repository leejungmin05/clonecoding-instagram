package com.example.cloneinstagram.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cloneinstagram.R
import com.example.cloneinstagram.model.ContentDTO
import com.example.cloneinstagram.model.FirebaseRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentRecyclerAdapter(
    private val comments: ArrayList<ContentDTO.Comment>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CustomViewHolder(view)
    }

    private inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = holder.itemView

        view.context.resources.getString(R.string.comment_text,comments[position].comment)
        view.context.resources.getString(R.string.comment_userId,comments[position].userId)

        FirebaseRepository.getProfileUrl(comments[position].uid!!) { url ->
            Glide.with(view.context).load(url).apply(RequestOptions().circleCrop())
                .into(view.commentviewitem_imageview_profile)
        }
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}
