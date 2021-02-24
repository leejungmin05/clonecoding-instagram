package com.example.cloneinstagram.main.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cloneinstagram.R
import com.example.cloneinstagram.main.alarm.AlarmRecyclerAdapter
import com.example.cloneinstagram.model.ContentDTO
import com.example.cloneinstagram.model.FirebaseRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_grid.view.*

class GridFragment : Fragment() {
    private val recyclerAdapter: GridRecyclerAdapter by lazy {
        GridRecyclerAdapter(contentDTOs)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            LayoutInflater.from(activity).inflate(R.layout.fragment_grid, container, false)

        getGridUidList()
        view.gridfragment_recyclerview.adapter = recyclerAdapter
        view.gridfragment_recyclerview.layoutManager = GridLayoutManager(activity, 3)
        return view
    }

    private var contentDTOs: ArrayList<ContentDTO> = arrayListOf()

    private fun getGridUidList() {
        FirebaseRepository.getGridUidList { contentDTOList ->
            contentDTOs.clear()
            contentDTOs.addAll(contentDTOList)
            recyclerAdapter.notifyDataSetChanged()

        }
    }


}