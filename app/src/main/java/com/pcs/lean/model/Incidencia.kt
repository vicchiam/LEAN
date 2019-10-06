package com.pcs.lean.model

data class Incidencia(val id: Long, val codigo: String, val nombre: String, val descripcion: String, val id_zona: Long){

    fun getSearchCriteria(): String{
        return id_zona.toString()
    }

}