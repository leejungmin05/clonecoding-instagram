package com.example.cloneinstagram.main.user

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cloneinstagram.R
import com.example.cloneinstagram.login.LoginActivity
import com.example.cloneinstagram.main.MainActivity
import com.example.cloneinstagram.model.AlarmDTO
import com.example.cloneinstagram.model.ContentDTO
import com.example.cloneinstagram.model.FollowDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_user.view.*


class UserFragment : Fragment() {
    private var fragmentView: View? = null
    private var firestore: FirebaseFirestore? = null
    private var uid: String? = null
    private var auth: FirebaseAuth? = null
    private var currentUserUid :String? = null
    private val recyclerAdapter: UserRecyclerAdapter by lazy {
        UserRecyclerAdapter(contentDTOs)
    }


    companion object {
        var PICK_PROFILE_FROM_ALBUM = 10

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getDataList()
        fragmentView =
            LayoutInflater.from(activity).inflate(R.layout.fragment_user, container, false)
        uid = arguments?.getString("destinationUid")
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        currentUserUid = auth?.currentUser?.uid

        if (uid == currentUserUid) {
            //Mypage
            fragmentView?.account_btn_follow_signout?.text = getString(R.string.signout)
            fragmentView?.account_btn_follow_signout?.setOnClickListener {
                activity?.finish()
                startActivity(Intent(activity, LoginActivity::class.java))
                auth?.signOut()
            }
        } else {
            //otherUser
            fragmentView?.account_btn_follow_signout?.text = getString(R.string.follow)
            val mainActivity = (activity as MainActivity)
            mainActivity?.toolbar_username?.text = arguments?.getString("userId")
            mainActivity?.toolbar_btn_back?.setOnClickListener {
                mainActivity.bottom_navigation.selectedItemId = R.id.action_home
            }
            mainActivity?.toolbar_title_image?.visibility = View.GONE
            mainActivity?.toolbar_username?.visibility = View.VISIBLE
            mainActivity?.toolbar_btn_back?.visibility = View.VISIBLE
            fragmentView?.account_btn_follow_signout?.setOnClickListener {
                requestFollow()
            }
        }
        fragmentView?.account_recyclerview?.adapter = recyclerAdapter
        fragmentView?.account_recyclerview?.layoutManager = GridLayoutManager(activity, 3)

        fragmentView?.account_iv_profile?.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            activity?.startActivityForResult(photoPickerIntent, PICK_PROFILE_FROM_ALBUM)
        }
        getProfileImage()
        getFollowerAndFollowing()
        return fragmentView
    }

    private fun getFollowerAndFollowing() {
        firestore?.collection("users")?.document(uid!!)
            ?.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (documentSnapshot == null) return@addSnapshotListener
                val followDTO = documentSnapshot.toObject(FollowDTO::class.java)
                if (followDTO?.followingCount != null) {
                    fragmentView?.account_tv_following_count?.text =
                        followDTO?.followingCount?.toString()
                }
                if (followDTO?.followerCount != null) {
                    fragmentView?.account_tv_follower_count?.text =
                        followDTO?.followerCount?.toString()
                    if (followDTO?.followers?.containsKey(currentUserUid!!)) {
                        fragmentView?.account_btn_follow_signout?.text =
                            getString(R.string.follow_cancel)
                        fragmentView?.account_btn_follow_signout?.background?.setColorFilter(
                            ContextCompat.getColor(requireActivity(), R.color.colorLightGray),
                            PorterDuff.Mode.MULTIPLY
                        )
                    } else {

                        if (uid != currentUserUid) {
                            fragmentView?.account_btn_follow_signout?.text =
                                getString(R.string.follow)
                            fragmentView?.account_btn_follow_signout?.background?.colorFilter =
                                null
                        }
                    }
                }
            }
    }


    private fun requestFollow() {
        //save data to my account
        val tsDocFollowing = firestore?.collection("users")?.document(currentUserUid!!)
        firestore?.runTransaction { transaction ->
            var followDTO = transaction.get(tsDocFollowing!!).toObject(FollowDTO::class.java)
            if (followDTO == null) {
                followDTO = FollowDTO()
                followDTO!!.followingCount = 1
                followDTO!!.followers[uid!!] = true

                transaction.set(tsDocFollowing, followDTO)
                return@runTransaction
            }
            if (followDTO.followings.containsKey(uid)) {
                //it remoe following third person when a third person follow me
                followDTO?.followingCount = followDTO?.followingCount - 1
                followDTO?.followings?.remove(uid)
            } else {
                //it add following third person when a third person do not follow me
                followDTO?.followingCount = followDTO?.followingCount + 1
                followDTO?.followings[uid!!] = true

            }
            transaction.set(tsDocFollowing, followDTO)
            return@runTransaction
        }
        //save data to third account
        val tsDocFollower = firestore?.collection("users")?.document(uid!!)
        firestore?.runTransaction { transaction ->
            var followDTO = transaction.get(tsDocFollower!!).toObject(FollowDTO::class.java)
            if (followDTO == null) {
                followDTO = FollowDTO()
                followDTO!!.followerCount = 1
                followDTO!!.followers[currentUserUid!!] = true

                transaction.set(tsDocFollower, followDTO!!)
                return@runTransaction
            }
            if (followDTO!!.followers.containsKey(currentUserUid)) {
                //it cancel my follwer when i follow a third person
                followDTO!!.followerCount = followDTO!!.followerCount - 1
                followDTO!!.followers.remove(currentUserUid!!)
            } else {
                //it add my follower when i don't follow a third person
                followDTO!!.followerCount = followDTO!!.followerCount + 1
                followDTO!!.followers[currentUserUid!!] = true
                follwerAlarm(uid!!)
            }
            transaction.set(tsDocFollower, followDTO!!)
            return@runTransaction
        }
    }

    private fun getProfileImage() {
        firestore?.collection("profileImages")?.document(uid!!)
            ?.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (documentSnapshot == null) return@addSnapshotListener
                if (documentSnapshot.data != null) {
                    var url = documentSnapshot?.data!!["image"]
                    Glide.with(requireActivity()).load(url).apply(RequestOptions().circleCrop())
                        .into(fragmentView?.account_iv_profile!!)
                }

            }
    }


    private fun follwerAlarm(destinationUid: String) {
        var alarmDTO = AlarmDTO()
        alarmDTO.destinationUid = destinationUid
        alarmDTO.userId = auth?.currentUser?.email
        alarmDTO.uid = auth?.currentUser?.uid
        alarmDTO.kind = 2
        alarmDTO.timestamp = System.currentTimeMillis()
        FirebaseFirestore.getInstance().collection("alarms").document().set(alarmDTO)
    }


    var contentDTOs: ArrayList<ContentDTO> = arrayListOf()


    private fun getDataList() {
        firestore?.collection("images")?.whereEqualTo("uid", uid)
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                contentDTOs.clear()
                if (querySnapshot == null) return@addSnapshotListener

                //get data
                for (snapshot in querySnapshot.documents) {
                    var item = snapshot.toObject(ContentDTO::class.java)
                    contentDTOs.add(item!!)
                }
                fragmentView?.account_tv_post_count?.text = contentDTOs.size.toString()

            }
        recyclerAdapter.notifyDataSetChanged()
    }

}