package com.example.cloneinstagram.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloneinstagram.R
import com.example.cloneinstagram.navigation.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



class DetailViewFragment : Fragment(){
    var firestore : FirebaseFirestore?= null
    var uid : String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_detail,container,false)
        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid

        view.detailviewfragment_recyclerview.adapter = DetailViewRecyclerViewAdapter()
        view.detailviewfragment_recyclerview.layoutManager = LinearLayoutManager(activity)

        return view
    }
    inner class DetailViewRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var contentDTOs : ArrayList<ContentDTO> = arrayListOf()
        var contentUidList : ArrayList<String> = arrayListOf()

        init { //DB에서 데이터 시간순으로 받아오기
            firestore?.collection("images")?.orderBy("timestamp")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                contentDTOs.clear() //contentDTOs 초기화
                contentUidList.clear() //contentDTOs 초기화
                for(snapshot in querySnapshot!!.documents){
                    var item = snapshot.toObject(ContentDTO::class.java)
                    contentDTOs.add(item!!)
                    contentUidList.add(snapshot.id)
                }
                notifyDataSetChanged()

            }
        }

       override fun onCreateViewHolder(p0: ViewGroup, p1 : Int) : RecyclerView.ViewHolder{
               var view = LayoutInflater.from(p0.context).inflate(R.layout.item_detail,p0,false)
               return CustomViewHolder(view)
           }


        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)  //메모리를 적게 사용하기 위해서

        override fun getItemCount(): Int {
           return contentDTOs.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewholder = (holder as CustomViewHolder).itemView

            //UserID
            viewholder.detailviewitem_profile_textview.text = contentDTOs!!.userId

            //Image
            Glide.with(holder.itemView.context).load(contentDTOs!![holder].imageUrl).into(viewholder.detailviewitem_imageview_content)

            //Explain of content
            viewholder.detailviewitem_explain_textview.text =contentDTOs!![holder].explain

            //likes
            viewholder.detailviewitem_favoritecounter_textview.text = "Likes" + contentDTOs!![holder].favoriteCount

            //profileImage
            Glide.with(holder.itemView.context).load(contentDTOs!![holder].imageUrl).into(viewholder.detailviewitem_profile_image)

            //this code is when the button is clicked
            viewholder.detailviewitem_favorite_imageview.setOnClickListner{
                favoriteEvent(position)
            }

            //this code is when the page is loaded
            if(contentDTOs!![position].favorites.containsKey(uid)){
                //this is like status
                viewholder.detailviewitem_favorite_imageview.setImageResource(R.drawable.ic_favorite)
            }else{
                //this is unlike status
                viewholder.detailviewitem_favorite_imageview.setImageResource(R.drawable.ic_favorite_border)
            }
        }

        fun favoriteEvent(position: Int){
            var tsDoc = firestore?.collection("images")?.document(contentUidList[position])
            firestore?.runTransaction { transaction ->

                var uid = FirebaseAuth.getInstance().currentUser?.uid
                var contentDTO = transaction.get(tsDoc!!).toObject(ContentDTO::class.java)

                if(contentDTO!!.favorties.containsKey(uid)){
                    //when the button is clicked
                    contentDTO?.favoriteCount = contentDTO?.favoriteCount -1
                    contentDTO?.favorties.remove(uid)

                }else {
                    //when the button is not clicked
                    contentDTO?.favoriteCount = contentDTO?.favoriteCount +1
                    contentDTO?.favorties[uid!!] = true
                }
                transaction.set(tsDoc,contentDTO)

            }
        }
    }
}