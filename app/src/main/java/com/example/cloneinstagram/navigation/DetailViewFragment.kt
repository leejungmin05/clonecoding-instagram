package com.example.cloneinstagram.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.cloneinstagram.R
import com.example.cloneinstagram.navigation.model.ContentDTO
import com.google.firebase.firestore.FirebaseFirestore

class DetailViewFragment : Fragment(){
    var firestore : FirebaseFirestore?= null
    private lateinit var detailviewitem_profile_textview : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_detail,container,false)4
        firestore = FirebaseFirestore.getInstance()

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

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail,parent,false)
            return CustomviewHolder(view)
        }

        inner class CustomviewHolder(view: View) : RecyclerView.ViewHolder(view)  //메모리를 적게 사용하기 위해서

        override fun getItemCount(): Int {
           return contentDTOs.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as CustomviewHolder).itemView

            //UserID
            viewHolder.detailviewitem_profile_textview.text = contentDTOs!![position].userId
        }
    }
}