package com.pcs.lean

import android.content.Context
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken

class Router{

    companion object {

        fun <T: Any> getJSON(context: Context, url: String, params: String, responseListener: (T) -> Unit, errorListener: (String) -> Unit){
            val finalUrl = if(params.isEmpty()) url else "$url?$params"

            Log.d("URL", finalUrl)

            val queue = Volley.newRequestQueue(context)
            val request = StringRequest(
                Request.Method.GET,
                finalUrl,
                Response.Listener<String> { response ->
                    Log.d("RESPONSE", response)
                    val sType = object: TypeToken<T>() {}.type
                    val json = Gson().fromJson<T>(response, sType)
                    responseListener(json)
                },
                Response.ErrorListener { err ->
                    error(err)
                }
            )
            queue.add(request)

        }

        private fun error(err: VolleyError): String{
            if(err is TimeoutError || err is NoConnectionError){
                return "No se ha podido conectar. Msg: $err"
            }
            else if(err is ServerError){
                return "Error en el servidor. Msg: $err"
            }
            else if(err is NetworkError){
                return "Error en la red. Msg: $err"
            }
            return "Error. mensaje: $err"

        }
    }
}