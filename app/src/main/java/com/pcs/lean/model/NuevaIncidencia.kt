package com.pcs.lean.model

import java.util.*

data class NuevaIncidencia(var date: Date, var of: OF = OF("","",""), var tipoIncidencia: TipoIncidencia = TipoIncidencia(0,"","","",0), var minutos: Int = 0, var comentario: String = ""){

    fun isEmpty(): Boolean{
        return (of.isEmpty() && tipoIncidencia.isEmpty())
    }

    fun isComplete(): Boolean{
        return (!of.isEmpty() && !tipoIncidencia.isEmpty())
    }

}