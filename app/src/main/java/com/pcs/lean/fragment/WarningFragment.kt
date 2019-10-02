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

        val data = emptyArray<String>()
        val adapter = ArrayAdapter(context!!, R.layout.spinner_item_selected, data)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner = view.findViewById(R.id.spinner_linea)
        spinner.adapter = adapter

        getServerData()

        return view
    }

    private fun getServerData(){
        prefs = Prefs(context!!)
        var url = prefs?.settingsPath ?: ""
        var center: String = (prefs?.settingsCenter).toString()

        if(url.isNotEmpty()){
            Router._GET(
                context = context!!,
                url = url,
                params = mapOf("action" to "get-lineas", "centro" to (center+1)),
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