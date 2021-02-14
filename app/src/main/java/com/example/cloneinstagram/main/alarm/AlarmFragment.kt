package com.example.cloneinstagram.main.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cloneinstagram.R
import com.example.cloneinstagram.model.AlarmDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_alarm.view.*
import kotlinx.android.synthetic.main.item_comment.view.*

class AlarmFragment : Fragment(){
    var firestore: FirebaseFirestore? = null
    private val recyclerAdapter: AlarmRecyclerAdapter by lazy {
        AlarmRecyclerAdapter(alarmDTOList)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_alarm,container,false)

        getDataList()
        view.alarmfragment_recyclerview.adapter = recyclerAdapter
        view.alarmfragment_recyclerview.layoutManager = LinearLayoutManager(context)

        return view
    }
    var alarmDTOList : ArrayList<AlarmDTO> = arrayListOf()

    private fun getDataList() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        firestore?.collection("alarms")?.whereEqualTo("destinationUid",uid)
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            alarmDTOList.clear()

            if(querySnapshot ==null) return@addSnapshotListener

            for(snapshot in querySnapshot.documents){
                var item = snapshot.toObject(AlarmDTO::class.java)
                alarmDTOList.add(item!!)
            }
                recyclerAdapter.notifyDataSetChanged()
        }
    }

}

