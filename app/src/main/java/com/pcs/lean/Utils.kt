package com.pcs.lean

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
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
            val aux=text.split(delimiter)
            if(aux.size==3){
                val day= aux.get(0).toInt()
                val month = aux.get(1).toInt()
                val year = aux.get(2).toInt()
                val cal: Calendar = Calendar.getInstance()
                cal.set(year, (month-1), day)
                return cal.time
            }
            return Date()
        }

        fun dateToString(date: Date): String{
            val format = SimpleDateFormat("dd/MM/yyyy")
            return format.format(date)
        }

        fun closeKeyboard(context: Context, activity: AppCompatActivity){
            val imm =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }

        inline fun <reified T> fromJson(jsonString: String): T{
            return Gson().fromJson<T>(jsonString, object : TypeToken<T>() {}.type)
        }

        fun modalAlert(activity: AppCompatActivity, message: String = "Cargando..."): AlertDialog{
            val builder = AlertDialog.Builder(activity)
            val dialogView = activity.layoutInflater.inflate(R.layout.progress_dialog, null)
            val messageTextView = dialogView.findViewById<TextView>(R.id.message)
            messageTextView.text = message
            builder.setView(dialogView)
            builder.setCancelable(false)
            val dialog = builder.create()
            return dialog
        }

    }

}