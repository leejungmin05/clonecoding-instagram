package com.example.cloneinstagram.model


import kotlinx.android.synthetic.main.item_comment.view.*

data class  AlarmDTO (
    var destinationUid : String? = null,
    var userId : String? = null,
    var uid : String? = null,

    //0 L like alarm
    //1 : comment alarm
    //2 : follow alarm
    var kind : Int? = null,
    var message : String? = null,
    var timestamp : Long? = null


)

