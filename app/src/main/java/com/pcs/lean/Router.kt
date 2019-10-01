package com.pcs.lean

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

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

        fun _GETJSON(context: Context, url: String, params: Map<String, String>, responseListener: (String) -> Unit, errorListener: (String) -> Unit){
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
    }



}