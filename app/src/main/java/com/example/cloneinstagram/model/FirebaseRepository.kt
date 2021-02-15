package com.example.cloneinstagram.model

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_comment.view.*

object FirebaseRepository {
    fun toggleFavorite(
        contentDTO: ContentDTO,
        documentId: String
    ) {
        firestore.runTransaction { transaction ->
            if (contentDTO.favorites.containsKey(uid)) {
                //when the button is clicked
                contentDTO.favoriteCount = contentDTO.favoriteCount - 1
                contentDTO.favorites.remove(uid)
            } else {
                //when the button is not clicked
                contentDTO.favoriteCount = contentDTO.favoriteCount + 1
                contentDTO.favorites[uid] = true
                favoriteAlarm(contentDTO.uid!!)
            }
            transaction.set(firestore.document(documentId), contentDTO)
        }
    }

    private fun favoriteAlarm(destinationUid: String) {
        val alarmDTO = AlarmDTO(
            destinationUid,
            email,
            uid,
            AlarmKind.LIKE,
            "",
            System.currentTimeMillis()
        )
        firestore.collection(ALARMS).document().set(alarmDTO)
    }


    fun requestFollow(followDTO: FollowDTO) {
        val tsDocFollowing = firestore.collection(USERS).document(currentUserUid!!)
        firestore.runTransaction { transaction ->
            if (followDTO == null) {
                followDTO.followingCount = 1
                followDTO.followers[uid] = true
                return@runTransaction
            }
            if (followDTO.followings.containsKey(uid)) {
                //it remove following third person when a third person follow me
                followDTO.followingCount = followDTO.followingCount - 1
                followDTO.followings.remove(uid)
            } else {
                //it add following third person when a third person do not follow me
                followDTO.followingCount = followDTO.followingCount + 1
                followDTO.followings[uid] = true
            }
            transaction.set(tsDocFollowing, followDTO)
            return@runTransaction
        }
        //save data to third account
        val tsDocFollower = firestore.collection("USERS").document(uid)
        firestore.runTransaction { transaction ->
            if (followDTO == null) {
                followDTO.followerCount = 1
                followDTO.followers[currentUserUid] = true
                return@runTransaction
            }
            if (followDTO.followers.containsKey(currentUserUid)) {
                //it cancel my follwer when i follow a third person
                followDTO.followerCount = followDTO.followerCount - 1
                followDTO.followers.remove(currentUserUid)
            } else {
                //it add my follower when i don't follow a third person
                followDTO.followerCount = followDTO.followerCount + 1
                followDTO.followers[currentUserUid] = true
                followerAlarm(uid)
            }
            transaction.set(tsDocFollower, followDTO)
            return@runTransaction
        }
    }

    private fun followerAlarm(destinationUid: String) {
        val alarmDTO = AlarmDTO(
            destinationUid,
            email,
            uid,
            AlarmKind.FOLLOW,
            "",
            System.currentTimeMillis()
        )
        firestore.collection(ALARMS).document().set(alarmDTO)
    }


    fun getDataList(listener: (List<ContentDTO>, List<String>) -> Unit) {
        firestore.collection(IMAGES).orderBy("timestamp")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                //Sometimes, this code return null of queryshanpshot when it signout
                if (querySnapshot == null) return@addSnapshotListener

                val contentDTOList = querySnapshot.toObjects(ContentDTO::class.java)
                val snapshotIdList = querySnapshot.documents.map { documentSnapshot ->
                    documentSnapshot.id
                }
                listener.invoke(contentDTOList, snapshotIdList)
            }
    }

    fun getUidDataList(listener: (List<ContentDTO>)-> Unit) {
        firestore.collection(IMAGES).whereEqualTo("uid", uid)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (querySnapshot == null) return@addSnapshotListener

                val contentDTOList = querySnapshot.toObjects(ContentDTO::class.java)

                listener.invoke(contentDTOList)
            }
    }
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: " "
    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
    private val email = FirebaseAuth.getInstance().currentUser?.email

    val ALARMS = "alarms"
    val USERS = "users"
    val IMAGES = "images"

}