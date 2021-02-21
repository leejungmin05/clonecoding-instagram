package com.example.cloneinstagram.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloneinstagram.R
import com.example.cloneinstagram.main.user.UserFragment
import com.example.cloneinstagram.model.ContentDTO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_detail.view.*

class RecyclerAdapterDetail: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var contentDTOs: ArrayList<ContentDTO> = arrayListOf()
    var contentUidList: ArrayList<String> = arrayListOf()
    var firestore: FirebaseFirestore? = null

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

        //userid
        viewholder.detailviewitem_profile_textview.text = contentDTOs!![position].userId

        //Image
        Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUrl).into(viewholder.detailviewitem_imageview_content)

        //Explain of content
        viewholder.detailviewitem_explain_textview.text =contentDTOs!![position].explain

        //likes
        viewholder.detailviewitem_favoritecounter_textview.text = "Likes" + contentDTOs!![position].favoriteCount

        //profileImage
        Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUrl).into(viewholder.detailviewitem_profile_image)

        //when the button is clicked
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
            var bundle = Bundle()
            bundle.putString("destinationUid",contentDTOs[position].uid)
            bundle.putString("userId",contentDTOs[position].userId)
            fragment.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_content,fragment)?.commit()

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



}