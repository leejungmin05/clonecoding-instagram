package com.example.cloneinstagram.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseRepository {
    fun toggleFavorite(
        contentDTO: ContentDTO,
        documentId: String
    ) {
        firestore.runTransaction{ transaction->
            if(contentDTO.favorites.containsKey(uid)){
                //when the button is clicked
                contentDTO.favoriteCount = contentDTO.favoriteCount -1
                contentDTO.favorites.remove(uid)
            }else{
                //when the button is not clicked
                contentDTO.favoriteCount = contentDTO.favoriteCount + 1
                contentDTO.favorites[uid] = true
                favoriteAlarm(contentDTO.uid!!)
            }
            transaction.set(firestore.document(documentId),contentDTO)
        }
    }

    private fun favoriteAlarm(destinationUid : String) {
        val alarmDTO = AlarmDTO(
            destinationUid,
            email,
            uid,
            0,
            "",
            System.currentTimeMillis()
        )
        firestore.collection(ALARMS).document().set(alarmDTO)
    }

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid?:" "
    private val email = FirebaseAuth.getInstance().currentUser?.email

    val ALARMS = "alarms"
}