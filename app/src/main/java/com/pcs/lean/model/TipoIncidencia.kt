package com.pcs.lean.model

data class TipoIncidencia(val id: Long, val codigo: String, val nombre: String, val descripcion: String, val id_zona: Long){

    fun getSearchCriteria(): String{
        return id_zona.toString()
    }

    fun isEmpty(): Boolean {
        return (id == 0L && codigo=="" && nombre=="" && descripcion=="" && id_zona == 0L)
    }

}