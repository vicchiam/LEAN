package com.pcs.lean

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var textView : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.test)

        getOFs()
    }

    fun getOFs(){
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())

        val queue = Volley.newRequestQueue(this)
        val url :String ="http://172.16.0.173/Apps/lean/android/router.php?action=get-ofs&date=${currentDate}"

        val request = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                textView!!.text=response
            },
            Response.ErrorListener { err ->
                textView!!.text = err.toString()
            })

        queue.add(request)
    }

}
