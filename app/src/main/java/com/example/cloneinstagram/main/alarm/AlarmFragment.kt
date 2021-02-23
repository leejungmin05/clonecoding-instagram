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
import com.example.cloneinstagram.model.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_alarm.view.*
import kotlinx.android.synthetic.main.item_comment.view.*

class AlarmFragment : Fragment(){
    var firestore: FirebaseFirestore? = null
    private val recyclerAdapter: AlarmRecyclerAdapter by lazy {
        AlarmRecyclerAdapter(alarmDTOs)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_alarm,container,false)

        getDataList()
        view.alarmfragment_recyclerview.adapter = recyclerAdapter
        view.alarmfragment_recyclerview.layoutManager = LinearLayoutManager(context)

        return view
    }
    private var alarmDTOs : ArrayList<AlarmDTO> = arrayListOf()

    private fun getDataList() {
        FirebaseRepository.getAlarmUidDataList { alarmDTOList ->
            alarmDTOs.clear()
            alarmDTOs.addAll(alarmDTOList)
            recyclerAdapter.notifyDataSetChanged()
        }

        }
    }



