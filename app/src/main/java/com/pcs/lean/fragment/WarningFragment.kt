package com.pcs.lean.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.pcs.lean.Prefs
import com.pcs.lean.R
import com.pcs.lean.Router
import java.text.SimpleDateFormat
import java.util.*

class WarningFragment : Fragment(){

    private lateinit var spinner : Spinner

    private var prefs : Prefs? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_warning, container, false)

        var test = arrayOf("Rahul", "Jack", "Rajeev", "Aryan", "Rashmi")

        spinner = view.findViewById(R.id.lineas_spinner)
        val arrayAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, test)
        spinner.adapter = arrayAdapter

        getServerData()

        return view
    }

    private fun getServerData(){
        prefs = Prefs(context!!)
        var url = prefs?.settingsPath ?: ""

        if(url.isNotEmpty()){
            Router._GET(
                context = context!!,
                url = url,
                params = mapOf("action" to "get-lineas", "centro" to "0"),
                responseListener = { response ->
                    Log.d("RESPONSE", response)
                },
                errorListener = { err ->
                    Log.d("ERROR", err)
                }
            )
        }
    }

}