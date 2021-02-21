package com.example.cloneinstagram.main.alarm


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cloneinstagram.R
import com.example.cloneinstagram.extensions.invisible
import com.example.cloneinstagram.model.AlarmDTO
import com.example.cloneinstagram.model.AlarmKind
import com.example.cloneinstagram.model.FirebaseRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_comment.view.*

class AlarmRecyclerAdapter(
    private var alarmDTOList: ArrayList<AlarmDTO>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CustomViewHolder(view)
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = holder.itemView
        FirebaseRepository.getProfileUrl(alarmDTOList[position].uid!!) { url ->
            Glide.with(view.context).load(url).apply(RequestOptions().circleCrop())
                .into(view.commentviewitem_imageview_profile)
        }
        val alarmText = when (alarmDTOList[position].kind) {
            AlarmKind.LIKE -> {
                alarmDTOList[position].userId + view.context.getString(R.string.alarm_favorite)
            }
            AlarmKind.COMMENT -> {
                alarmDTOList[position].userId + " " + alarmComment + " of " + alarmDTOList[position].message
            }
            AlarmKind.FOLLOW -> {
                alarmDTOList[position].userId + " " + view.context.getString(R.string.alarm_follow)
            }
            else -> ""
        }
        view.commentviewitem_textview_profile.text = alarmText
        view.commentviewitem_textview_comment.invisible()
    }

    override fun getItemCount(): Int {
        return alarmDTOList.size
    }

    private val alarmComment = "메세지를 남겼습니다."
}



