package com.example.cloneinstagram.model


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
        //내가 상대방 누구를 팔로우 하는지
        firestore.runTransaction { transaction ->
            /*if (followDTO == null) {
                followDTO.followingCount = 1
                followDTO.followers[uid] = true
                return@runTransaction
            }*/
            if (followDTO.followings.containsKey(uid)) {
                //상대방을 내가 팔로우 한 상태
                followDTO.followingCount = followDTO.followingCount - 1
                followDTO.followings.remove(uid)
            } else {
                //상대방을 팔로우를 안한 상태
                followDTO.followingCount = followDTO.followingCount + 1
                followDTO.followings[uid] = true
            }
            transaction.set(firestore.document(currentUserUid!!), followDTO)
            return@runTransaction
        }

        //내가 팔로우 한 상대방 계정이 누구(제3자)를 팔로우 하는지
        firestore.runTransaction { transaction ->
            /* if (followDTO == null) {
                 followDTO.followerCount = 1
                 followDTO.followers[currentUserUid!!] = true
                 return@runTransaction
             }*/
            if (followDTO.followers.containsKey(currentUserUid)) {
                //상대방 계정에 내가 팔로우 한 상태
                followDTO.followerCount = followDTO.followerCount - 1
                followDTO.followers.remove(currentUserUid)
            } else {
                //상대방 계정 팔로우 안한 상태
                followDTO.followerCount = followDTO.followerCount + 1
                followDTO.followers[currentUserUid!!] = true
                followerAlarm(uid)
            }
            transaction.set(firestore.document(uid), followDTO)
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

    fun commentAlarm(destinationUid: String, message: String) {
        val alarmDTO = AlarmDTO(
            destinationUid,
            email,
            uid,
            AlarmKind.COMMENT,
            "",
            System.currentTimeMillis()
        )
        firestore.collection(ALARMS).document().set(alarmDTO)

    }

    fun commentData(contentUid: String) {
        val comment = ContentDTO.Comment(
            email,
            uid,
            "",
            System.currentTimeMillis()
        )
        firestore.collection(IMAGES).document(contentUid)
            .collection("comments").document().set(comment)
    }

    fun getDataList(listener: (List<ContentDTO>, List<String>) -> Unit) {
        firestore.collection(IMAGES).orderBy("timestamp")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (querySnapshot == null) return@addSnapshotListener
                val contentDTOList = querySnapshot.toObjects(ContentDTO::class.java)
                val snapshotIdList = querySnapshot.documents.map { documentSnapshot ->
                    documentSnapshot.id
                }
                listener.invoke(contentDTOList, snapshotIdList)
            }
    }

    fun getUidDataList(listener: (List<ContentDTO>) -> Unit) {
        firestore.collection(IMAGES).whereEqualTo("uid", uid)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (querySnapshot == null) return@addSnapshotListener
                val contentDTOList = querySnapshot.toObjects(ContentDTO::class.java)
                listener.invoke(contentDTOList)
            }
    }

    fun getProfileImage(listener: (String) -> Unit) {
        firestore.collection(PROFILE).document(uid)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (documentSnapshot == null) return@addSnapshotListener
                if (documentSnapshot.data != null) {
                    val url = documentSnapshot.data!!["image"].toString()
                    listener(url)
                }
            }
    }

    fun getFollowData(listener: (FollowDTO?) -> Unit) {
        firestore.collection(USERS).document(uid)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (documentSnapshot == null)
                    return@addSnapshotListener
                val followDTOList = documentSnapshot.toObject(FollowDTO::class.java)
                listener.invoke(followDTOList)
            }
    }

    fun getAlarmUidDataList(listener: (List<AlarmDTO>) -> Unit) {
        firestore.collection(ALARMS).whereEqualTo("destinationUid", uid)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (querySnapshot == null) return@addSnapshotListener
                val alarmDTOList = querySnapshot.toObjects(AlarmDTO::class.java)
                listener.invoke(alarmDTOList)
            }

    }


    fun getProfileUrl(uid: String, listener: (String) -> Unit) {
        firestore.collection(PROFILE)
            .document(uid).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val url = task.result!![IMAGE].toString()
                    listener(url)
                }
            }
    }

    fun getComUidDataList(contentUid: String, listener: (MutableList<ContentDTO.Comment>) -> Unit) {
        firestore.collection(IMAGES)
            .document(contentUid)
            .collection("comments")
            .orderBy("timestamp")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (querySnapshot == null) return@addSnapshotListener

                val commentList = querySnapshot.toObjects(ContentDTO.Comment::class.java)
                listener.invoke(commentList)

            }
    }

    fun getGridUidList(listener: (List<ContentDTO>) -> Unit) {
        firestore.collection(IMAGES)
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
    val USERS = "users"
    val ALARMS = "alarms"
    val IMAGES = "images"
    val PROFILE = "profileImages"
    private val IMAGE = "image"
}