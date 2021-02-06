package com.example.cloneinstagram.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloneinstagram.R
import com.example.cloneinstagram.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_detail.view.*

class DetailViewFragment : Fragment() {
    var firestore: FirebaseFirestore? = null
    var uid: String? = null
    private val recyclerAdapter: DetailViewRecyclerAdapter by lazy {
        DetailViewRecyclerAdapter(contentDTOs, contentUidList)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_detail, container, false)

        uid = FirebaseAuth.getInstance().currentUser?.uid

        getDataList()
        view.detailviewfragment_recyclerview.adapter = recyclerAdapter
        view.detailviewfragment_recyclerview.layoutManager = LinearLayoutManager(context)


        return view
    }

    var contentDTOs: ArrayList<ContentDTO> = arrayListOf()
    var contentUidList: ArrayList<String> = arrayListOf()

    private fun getDataList() {
        firestore?.collection("images")?.orderBy("timestamp")
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                contentDTOs.clear()
                contentUidList.clear()
                //Sometimes, this code return null of queryshanpshot when it signout
                if (querySnapshot == null) return@addSnapshotListener

                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(ContentDTO::class.java)
                    contentDTOs.add(item!!)
                    contentUidList.add(snapshot.id)
                }
                recyclerAdapter.notifyDataSetChanged()
            }
    }
}

