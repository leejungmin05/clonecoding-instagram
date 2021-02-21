package com.example.cloneinstagram.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloneinstagram.R
import com.example.cloneinstagram.model.ContentDTO
import com.example.cloneinstagram.model.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_detail.view.*

class DetailViewFragment : Fragment() {
    private val recyclerAdapter: DetailViewRecyclerAdapter by lazy {
        DetailViewRecyclerAdapter(contentDTOs, contentUids)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_detail, container, false)
        view.detailviewfragment_recyclerview.adapter = recyclerAdapter
        view.detailviewfragment_recyclerview.layoutManager = LinearLayoutManager(context)
        getDataList()
        return view
    }

    var contentDTOs: ArrayList<ContentDTO> = arrayListOf()
    var contentUids: ArrayList<String> = arrayListOf()

    private fun getDataList() {
        FirebaseRepository.getDataList { contentDTOList, documentIDList->
            contentDTOs.clear()
            contentDTOs.addAll(contentDTOList)
            contentUids.clear()
            contentUids.addAll(documentIDList)
            recyclerAdapter.notifyDataSetChanged()
        }
    }
}

