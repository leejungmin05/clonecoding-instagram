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
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CustomViewHolder(view)
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var view = holder.itemView

        firestore.collection("profileImages")
            .document(alarmDTOList[position].uid!!)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val url = task.result!!["image"]
                    Glide.with(view.context).load(url).apply(RequestOptions().circleCrop())
                        .into(view.commentviewitem_imageview_profile)
                }
            }

        when (alarmDTOList[position].kind) {
            0 -> {
                val str_0 = alarmDTOList[position].userId + getString(R.string.alarm_favorite)
                view.commentviewitem_textview_profile.text = str_0
            }
            1 -> {
                val str_0 =
                    alarmDTOList[position].userId + " " + getString(R.string.alarm_comment) + " of " + alarmDTOList[position].message
                view.commentviewitem_textview_profile.text = str_0
            }
            2 -> {
                val str_0 = alarmDTOList[position].userId + " " + getString(R.string.alarm_follow)
                view.commentviewitem_textview_profile.text = str_0
            }
        }
        view.commentviewitem_textview_comment.visibility = View.INVISIBLE
    }

    override fun getItemCount(): Int {
        return alarmDTOList.size
    }


}


