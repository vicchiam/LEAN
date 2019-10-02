package com.pcs.lean

import android.content.Context
import androidx.appcompat.app.AlertDialog
import java.util.*

class Utils{

    companion object {
        fun _Alert(context: Context, message: String){
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Advertencia")
            builder.setMessage(message)
            builder.setNeutralButton("Aceptar"){ dialog, _ ->
                dialog.cancel()
            }
            builder.show()
        }

        fun _StringToDate(text: String, delimiter: String = "/"): Date {
            var aux=text.split(delimiter)
            if(aux.size==3){
                var day= aux.get(0).toInt()
                var month = aux.get(1).toInt()
                var year = aux.get(2).toInt()
                var cal: Calendar = Calendar.getInstance()
                cal.set(year, month, day)
                return cal.time
            }
            return Date()
        }
    }

}