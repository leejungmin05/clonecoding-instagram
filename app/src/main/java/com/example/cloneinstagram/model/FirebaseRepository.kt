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

    fun getDataList(listener: (List<ContentDTO>, List<String>) -> Unit) {
        firestore.collection("images").orderBy("timestamp")
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

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: " "
    private val email = FirebaseAuth.getInstance().currentUser?.email

    val ALARMS = "alarms"


}