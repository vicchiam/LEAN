package com.pcs.lean.model

import java.util.*

data class Incidencia(val id: Long, val id_dipositivo: Int, val fecha: Date, val OF: String, val id_tipo_incidencia: Long, val nombre: String, val minutos: Int, val comentario: String)