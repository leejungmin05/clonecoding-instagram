package com.example.cloneinstagram.main.alarm


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cloneinstagram.R
import com.example.cloneinstagram.extensions.getProfileImageByUid
import com.example.cloneinstagram.extensions.getSentence
import com.example.cloneinstagram.extensions.invisible
import com.example.cloneinstagram.model.AlarmDTO
import kotlinx.android.synthetic.main.item_comment.view.*

class AlarmRecyclerAdapter(
    private val alarmDTOList: ArrayList<AlarmDTO>
) : RecyclerView.Adapter<AlarmRecyclerAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(alarmDTOList[position])
    }

    override fun getItemCount(): Int {
        return alarmDTOList.size
    }

    class CustomViewHolder private constructor(holder: View) : RecyclerView.ViewHolder(holder) {
        private val profileTextView: TextView = holder.commentviewitem_textview_profile
        private val commentTextView: TextView = holder.commentviewitem_textview_comment
        private val profileImageView: ImageView = holder.commentviewitem_imageview_profile

        fun bind(alarmDTO: AlarmDTO) {
            val context = itemView.context
            profileImageView.getProfileImageByUid(alarmDTO.uid!!)
            profileTextView.text = alarmDTO.getSentence(context)
            commentTextView.invisible()
        }

        companion object {
            fun from(parent: ViewGroup): CustomViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_comment, parent, false)
                return CustomViewHolder(view)
            }
        }
    }
}



