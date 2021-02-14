package com.example.cloneinstagram.main.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloneinstagram.R
import com.example.cloneinstagram.main.user.UserFragment
import com.example.cloneinstagram.model.ContentDTO
import com.example.cloneinstagram.model.FirebaseRepository
import kotlinx.android.synthetic.main.item_detail.view.*

/**
 * 네이밍
 * itemlist 생성자로받기
 * firestore 분리하기
 * 접근지정자, val,var 고려하기
 * 가독성! 높이는 방법 고민하기
 * string 분리
 * etc..
 *
 * java collection List, Map, Set
 * Array
 * List vs Array
 * List vs ArrayList
 */
class DetailViewRecyclerAdapter(
    private val contentDTOs: List<ContentDTO>,
    private val ContentDIdList: List<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var uid: String = FirebaseRepository.uid
    companion object {
        const val TAG = "DetailViewRecyclerAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)
        return CustomViewHolder(view)
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = (holder as CustomViewHolder).itemView
        viewHolder.detailviewitem_profile_textview.text = contentDTOs[position].userId
        Glide.with(holder.itemView.context).load(contentDTOs[position].imageUrl)
            .into(viewHolder.detailviewitem_imageview_content)
        viewHolder.detailviewitem_explain_textview.text = contentDTOs[position].explain
        viewHolder.detailviewitem_favoritecounter_textview.text =
           viewHolder.context.resources.getString(R.string.likes,contentDTOs[position].favoriteCount)
        Glide.with(holder.itemView.context).load(contentDTOs[position].imageUrl)
            .into(viewHolder.detailviewitem_profile_image)
        // when the page is loaded
        if (contentDTOs[position].favorites.containsKey(uid)) {
            //like status
            viewHolder.detailviewitem_favorite_imageview.setImageResource(R.drawable.ic_favorite)
        } else {
            //unlike status
            viewHolder.detailviewitem_favorite_imageview.setImageResource(R.drawable.ic_favorite_border)
        }


        viewHolder.detailviewitem_favorite_imageview.setOnClickListener {
            favoriteEvent(position)
        }
        viewHolder.detailviewitem_profile_image.setOnClickListener {
            val fragment = UserFragment()
            val bundle = Bundle()
            bundle.putString("destinationUid", contentDTOs[position].uid)
            bundle.putString("userId", contentDTOs[position].userId)
            fragment.arguments = bundle
            fragment.activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.main_content, fragment)?.commit()
        }

        viewHolder.detailviewitem_comment_imageview.setOnClickListener { v ->
            val intent = Intent(v.context, CommentActivity::class.java).apply {
                putExtra("contentDid", ContentDIdList[position])
                putExtra("destinationUid", contentDTOs[position].uid)
            }
            v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return contentDTOs.size
    }

    private fun favoriteEvent(position: Int) {
        Log.d("DetailView","Like")
        FirebaseRepository.toggleFavorite(contentDTOs[position], ContentDIdList[position])
        notifyItemChanged(position)
    }
}