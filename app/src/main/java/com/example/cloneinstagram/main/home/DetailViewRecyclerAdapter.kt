package com.example.cloneinstagram.main.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.createDeviceProtectedStorageContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloneinstagram.R
import com.example.cloneinstagram.main.user.UserFragment
import com.example.cloneinstagram.model.AlarmDTO
import com.example.cloneinstagram.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_detail.view.*

class DetailViewRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var contentDTOs: ArrayList<ContentDTO> = arrayListOf()
    var contentUidList: ArrayList<String> = arrayListOf()
    var firestore: FirebaseFirestore? = null
    var uid: String? = null

    init {
        firestore?.collection("images")?.orderBy("timestamp")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            contentDTOs.clear()
            contentUidList.clear()
            //Sometimes, this code return null of queryshanpshot when it signout
            if(querySnapshot == null) return@addSnapshotListener

            for(snapshot in querySnapshot!!.documents){
                var item = snapshot.toObject(ContentDTO::class.java)
                contentDTOs.add(item!!)
                contentUidList.add(snapshot.id)
            }
            notifyDataSetChanged()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail,parent,false)
        return CustomViewHolder(view)
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewholder = (holder as CustomViewHolder).itemView
        viewholder.detailviewitem_profile_textview.text = contentDTOs!![position].userId
        Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUrl).into(viewholder.detailviewitem_imageview_content)
        viewholder.detailviewitem_explain_textview.text =contentDTOs!![position].explain
        viewholder.detailviewitem_favoritecounter_textview.text = "Likes" + contentDTOs!![position].favoriteCount
        Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUrl).into(viewholder.detailviewitem_profile_image)


        viewholder.detailviewitem_favorite_imageview.setOnClickListener {
            favoriteEvent(position)
        }
        // when the page is loaded
        if(contentDTOs!![position].favorites.containsKey(uid)){
            //like status
            viewholder.detailviewitem_favorite_imageview.setImageResource(R.drawable.ic_favorite)
        } else{
            //unlike status
            viewholder.detailviewitem_favorite_imageview.setImageResource(R.drawable.ic_favorite_border)
        }

        viewholder.detailviewitem_profile_image.setOnClickListener {
            var fragment = UserFragment()
            var detailFragment = DetailViewFragment()
            var bundle = Bundle()
            bundle.putString("destinationUid",contentDTOs[position].uid)
            bundle.putString("userId",contentDTOs[position].userId)
            fragment.arguments = bundle
            fragment.activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_content,fragment)?.commit()
        }

        viewholder.detailviewitem_comment_imageview.setOnClickListener { v->
            var intent = Intent(v.context,CommentActivity::class.java)
            intent.putExtra("contentUid",contentUidList[position])
            intent.putExtra("destinationUid",contentDTOs[position].uid)
            startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return contentDTOs.size
    }

    fun favoriteEvent(position : Int) {
        var tsDoc = firestore?.collection("images")?.document(contentUidList[position])
        firestore?.runTransaction{ transaction->

            var uid = FirebaseAuth.getInstance().currentUser?.uid
            var contentDTO = transaction.get(tsDoc!!).toObject(ContentDTO::class.java)

            if(contentDTO!!.favorites.containsKey(uid)){
                //when the button is clicked
                contentDTO?.favoriteCount = contentDTO?.favoriteCount -1
                contentDTO?.favorites.remove(uid)
            }else{
                //when the button is not clicked
                contentDTO?.favoriteCount = contentDTO?.favoriteCount + 1
                contentDTO?.favorites[uid!!] = true
                favoriteAlarm(contentDTOs[position].uid!!)
            }
            transaction.set(tsDoc,contentDTO)
        }
    }

    fun favoriteAlarm(destinationUid : String) {
        var alarmDTO = AlarmDTO()
        alarmDTO.destinationUid = destinationUid
        alarmDTO.userId = FirebaseAuth.getInstance().currentUser?.email
        alarmDTO.uid = FirebaseAuth.getInstance().currentUser?.uid
        alarmDTO.kind = 0
        alarmDTO.timestamp = System.currentTimeMillis()
        FirebaseFirestore.getInstance().collection("alarms").document().set(alarmDTO)
    }

}