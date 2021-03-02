package com.example.cloneinstagram.main.home


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cloneinstagram.R
import com.example.cloneinstagram.extensions.getProfileImageByUid
import com.example.cloneinstagram.model.ContentDTO
import com.example.cloneinstagram.model.FirebaseRepository.uid
import kotlinx.android.synthetic.main.item_comment.view.*


class CommentRecyclerAdapter(
    private val comments: ArrayList<ContentDTO.Comment>
) : RecyclerView.Adapter<CommentRecyclerAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(comments)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    class CustomViewHolder private constructor(holder: View) : RecyclerView.ViewHolder(holder) {
        private val profileImageView: ImageView = holder.commentviewitem_imageview_profile
        private val idTextView: TextView = holder.commentviewitem_textview_profile
        private val commentTextView: TextView = holder.commentviewitem_textview_comment
        fun bind(comments: ArrayList<ContentDTO.Comment>) {
            val context = itemView.context
            profileImageView.getProfileImageByUid(uid)
            idTextView.text = context.resources.getString(R.string.comment_text, comments[adapterPosition].comment)
            commentTextView.text = context.resources.getString(R.string.comment_userId, comments[adapterPosition].userId)

        }

        companion object {
            fun from(parent: ViewGroup): CustomViewHolder {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_comment, parent, false)
                return CustomViewHolder(view)
            }
        }
    }
}
