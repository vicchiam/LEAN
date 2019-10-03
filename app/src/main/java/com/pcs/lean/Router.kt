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
        fun _GET(context: Context, url: String, params: Map<String, String>, responseListener: (String) -> Unit, errorListener: (String) -> Unit){
            var fullURL = Router.makeURL(context, url, params);

            val queue = Volley.newRequestQueue(context)
            val request = StringRequest(
                Request.Method.GET,
                fullURL,
                Response.Listener<String> { response ->
                    responseListener(response)
                },
                Response.ErrorListener { err ->
                    errorListener(err.toString())
                }
            )
            queue.add(request)
        }

        fun _GETJSON(context: Context, url: String, params: Map<String, String>, responseListener: (Map<String,Any>) -> Unit, errorListener: (String) -> Unit){
            var fullURL = Router.makeURL(context, url, params)

            Log.d("URL", fullURL)

            val queue = Volley.newRequestQueue(context)
            val request = StringRequest(
                Request.Method.GET,
                fullURL,
                Response.Listener<String> { response ->
                    Log.d("RESPONSE", response)
                    var map: Map<String, Any>
                    try {
                        map = Gson().fromJson(
                            response, object : TypeToken<Map<String, Any>>() {}.type
                        )
                        responseListener(map)
                    }
                    catch(e: JsonParseException){
                        errorListener(e.message.toString())
                    }
                },
                Response.ErrorListener { err ->
                    var message: String = error(err)
                    errorListener(message)
                }
            )
            queue.add(request)
        }

        private fun makeURL(context: Context, url: String, params: Map<String, String>): String{
            var list: List<String> = emptyList()
            for(key in params.keys){
                var param = key+"="+params.get(key)
                list = list.plus(param)
            }
            var query = list.joinToString(separator = "&")

            var fullUrl = url
            if(query.isNotEmpty())
                fullUrl = url+"?"+query
            return fullUrl
        }

        private fun error(err: VolleyError): String{
            if(err is TimeoutError || err is NoConnectionError){
                return "No se ha podido conectar. Msg:"+err.toString();
            }
            else if(err is ServerError){
                return "Error en el servidor. Msg: "+err.toString()
            }
            else if(err is NetworkError){
                return "Error en la red. Msg:"+err.toString()
            }
            return "Error. mensaje:"+err.toString()

        }
    }
}