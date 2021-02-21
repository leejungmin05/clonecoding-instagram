package com.example.cloneinstagram.model




data class  AlarmDTO (
    var destinationUid : String? = null,
    var userId : String? = null,
    var uid : String? = null,
    var kind : AlarmKind? = null,
    var message : String? = null,
    var timestamp : Long? = null
)
enum class AlarmKind{
    LIKE, COMMENT, FOLLOW
}



