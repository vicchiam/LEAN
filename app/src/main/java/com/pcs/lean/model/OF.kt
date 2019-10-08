package com.pcs.lean.model

import com.google.gson.annotations.SerializedName

data class OF(
    @SerializedName("ORDEN") val orden: String,
    @SerializedName("CODIGO") val codigo: String,
    @SerializedName("NOMBRE") val nombre: String
){

    fun getSearchCriteria(): String{
        return "$orden $codigo $nombre".toLowerCase()
    }
    
    fun isEmpty(): Boolean {
       return (orden=="" && codigo=="" && nombre=="")
    }

}
