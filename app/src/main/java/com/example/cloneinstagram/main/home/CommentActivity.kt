package com.example.cloneinstagram.main.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloneinstagram.R
import com.example.cloneinstagram.model.ContentDTO
import com.example.cloneinstagram.model.FirebaseRepository
import com.example.cloneinstagram.model.FirebaseRepository.commentAlarm
import com.example.cloneinstagram.model.FirebaseRepository.commentData
import kotlinx.android.synthetic.main.activity_comment.*

class CommentActivity : AppCompatActivity() {
    private var contentUid: String? = null
    private var destinationUid: String? = null
    private val recyclerViewAdapter: CommentRecyclerAdapter by lazy {
        CommentRecyclerAdapter(comments as ArrayList<ContentDTO.Comment>)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        contentUid = intent.getStringExtra("contentUid")
        destinationUid = intent.getStringExtra("destinationUid")
        comment_recyclerView.adapter = recyclerViewAdapter
        comment_recyclerView.layoutManager = LinearLayoutManager(this)

        getComUidDataList()

        comment_btn_send?.setOnClickListener {
            commentData(contentUid.toString())
            commentAlarm(destinationUid.toString(), comment_edit_message.text.toString())
            comment_edit_message.setText("")
        }
    }

    private var comments: MutableList<ContentDTO.Comment> = mutableListOf()

    private fun getComUidDataList() {
        FirebaseRepository.getComUidDataList(contentUid.toString()) { commentList ->
            comments.clear()
            comments.addAll(commentList)
            recyclerViewAdapter.notifyDataSetChanged()

        }

    }
}



