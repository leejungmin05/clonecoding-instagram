package com.example.cloneinstagram.main.alarm


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cloneinstagram.R
import com.example.cloneinstagram.model.AlarmDTO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_comment.view.*

class AlarmRecyclerAdapter(
    private var alarmDTOList: ArrayList<AlarmDTO>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CustomViewHolder(view)
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = holder.itemView

        firestore.collection("profileImages")
            .document(alarmDTOList[position].uid!!).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val url = task.result!!["image"]
                    Glide.with(view.context).load(url).apply(RequestOptions().circleCrop())
                        .into(view.commentviewitem_imageview_profile)
                }
            }
        when (alarmDTOList[position].kind) {
            0 -> {
                val str_0 = alarmDTOList[position].userId + alarmFavorite
                view.commentviewitem_textview_profile.text = str_0
            }
            1 -> {
                val str_0 =
                    alarmDTOList[position].userId + " " + alarmComment + " of " + alarmDTOList[position].message
                view.commentviewitem_textview_profile.text = str_0
            }
            2 -> {
                val str_0 = alarmDTOList[position].userId + " " + alarmFollow
                view.commentviewitem_textview_profile.text = str_0
            }
        }
        view.commentviewitem_textview_comment.visibility = View.INVISIBLE
    }

    override fun getItemCount(): Int {
        return alarmDTOList.size
    }

    val alarmFavorite = "님이 좋아요를 눌렀습니다."
    val alarmComment = "메세지를 남겼습니다."
    val alarmFollow = "님이 당신의 계정을 팔로워하기 시작했습니다."

}


