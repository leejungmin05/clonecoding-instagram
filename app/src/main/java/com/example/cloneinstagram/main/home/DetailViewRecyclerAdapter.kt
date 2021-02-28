package com.example.cloneinstagram.main.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloneinstagram.R
import com.example.cloneinstagram.main.user.UserFragment
import com.example.cloneinstagram.model.ContentDTO
import com.example.cloneinstagram.model.FirebaseRepository
import com.example.cloneinstagram.model.FirebaseRepository.uid
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
 */
class DetailViewRecyclerAdapter(
    private val contentDTOs: List<ContentDTO>,
    private val ContentDIdList: List<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var uid: String = FirebaseRepository.uid


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CustomViewHolder , position: Int) {
        holder.bind(contentDTOs[position])
    }

    override fun getItemCount(): Int {
        return contentDTOs.size
    }




    class CustomViewHolder private constructor(holder: View) : RecyclerView.ViewHolder(holder){
        private val profileTextView: TextView = holder.detailviewitem_profile_textview
        private val mainImageView: ImageView = holder.detailviewitem_imageview_content
        private val explainTextView: TextView = holder.detailviewitem_explain_textview
        private val favoriteTextView: TextView = holder.detailviewitem_favoritecounter_textview
        private val profileImageView: ImageView = holder.detailviewitem_profile_image
        private val favoriteImageView: ImageView = holder.detailviewitem_favorite_imageview

        fun bind(contentDTO: ContentDTO){
            val viewHolder = itemView
            profileTextView.text = contentDTO.userId
            mainImageView //Glide.with(itemView.context).load(contentDTO.imageUrl).into(viewHolder.detailviewitem_imageview_content)
            explainTextView.text = contentDTO.explain
            favoriteTextView.text = viewHolder.context.resources.getString(
                R.string.likes,
                contentDTO.favoriteCount
            )
            profileImageView //Glide.with(itemView.context).load(contentDTO.imageUrl).into(viewHolder.detailviewitem_profile_image)

            // when the page is loaded
            if (contentDTO.favorites.containsKey(uid)) {
                //like status
                favoriteImageView.setImageResource(R.drawable.ic_favorite)
            } else {
                //unlike status
                favoriteImageView.setImageResource(R.drawable.ic_favorite_border)
            }


            viewHolder.detailviewitem_favorite_imageview.setOnClickListener {
                favoriteEvent()
            }
            viewHolder.detailviewitem_profile_image.setOnClickListener {
                val fragment = UserFragment()
                val bundle = Bundle()
                bundle.putString(DESTINATIONUID, contentDTOs[position].uid)
                bundle.putString(USERID, contentDTOs[position].userId)
                fragment.arguments = bundle
                fragment.activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.main_content, fragment)?.commit()
            }

            viewHolder.detailviewitem_comment_imageview.setOnClickListener { v ->
                val intent = Intent(v.context, CommentActivity::class.java).apply {
                    putExtra(CONTENTDID, ContentDIdList[position])
                    putExtra(DESTINATIONUID, contentDTOs[position].uid)
                }
                v.context.startActivity(intent)
            }
        }

        fun favoriteEvent(position: Int) {
            Log.d("DetailView", "Like")
            FirebaseRepository.toggleFavorite(contentDTOs[position], ContentDIdList[position])
            notifyItemChanged(position)
        }

        companion object{
            fun from(parent: ViewGroup): CustomViewHolder{
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_detail, parent, false)
                return CustomViewHolder(view)
            }
        }

        val DESTINATIONUID = "destinationUid"
        val USERID = "userId"
        val CONTENTDID = "contentDid"

    }
}