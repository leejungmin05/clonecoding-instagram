package com.example.cloneinstagram.navigation.model

data class ContentDTO   (var explain : String? = null, // 컨텐츠 설명 관리
                         var imageUrl : String? = null, // 이미지 주소 관리
                         var uid : String? = null, // 어느 유저가 올렸는지
                         var userId : String? = null, //올린 유저의 이미지
                         var timestamp : Long? = null, //컨텐츠 시간
                         var favoriteCount : Int = 0, // 좋아요 갯수 //좋아요 유저 관리
                         var favorites : MutableMap<String,Boolean> = HashMap()) {
    data class  Comment(var uid: String? = null,
                        var userId : String? = null,
                        var comment: String? = null,
                        var timestamp: Long? = null)

}