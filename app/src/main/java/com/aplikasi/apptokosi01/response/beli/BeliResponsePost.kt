package com.aplikasi.apptokosi01.response.beli

data class BeliResponsePost(
    val `data` : TransaksiData,
    val message : String,
    val success : Boolean
)

data class TransaksiData(
    val `beli` : Beli,
)