package com.pcs.lean

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class Utils{

    companion object {
        fun alert(context: Context, message: String){
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Advertencia")
            builder.setMessage(message)
            builder.setNeutralButton("Aceptar"){ dialog, _ ->
                dialog.cancel()
            }
            builder.show()
        }

        fun stringToDate(text: String, delimiter: String = "/"): Date {
            var aux=text.split(delimiter)
            if(aux.size==3){
                var day= aux.get(0).toInt()
                var month = aux.get(1).toInt()
                var year = aux.get(2).toInt()
                var cal: Calendar = Calendar.getInstance()
                cal.set(year, (month-1), day)
                return cal.time
            }
            return Date()
        }

        fun dateToString(date: Date): String{
            var format = SimpleDateFormat("dd/MM/yyyy")
            return format.format(date);
        }

        fun closeKeyboard(context: Context, activity: AppCompatActivity){
            val imm =
                context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity!!.currentFocus!!.windowToken, 0)
        }

        inline fun <reified T> fromJson(jsonString: String): T{
            return Gson().fromJson<T>(jsonString, object: TypeToken<T>(){}.type)
        }

    }

}