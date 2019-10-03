package com.pcs.lean.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.pcs.lean.*

class HistoryFragment : Fragment(){

    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        val spinner: Spinner = view.findViewById(R.id.spinner_linea)
        spinner.setSelection(mainActivity.warning!!.line)
        spinner.setOnTouchListener{ _, _ ->
            Utils._closeKeyboard(context!!, mainActivity)
            false
        }

        if(mainActivity.cache.get("lines")==null){
            getLines()
        }
        else{
            val aux: List<String> = (mainActivity.cache.get("lines") as List<String>)
            makeSpinner(view!!, R.id.spinner_linea, aux, mainActivity.warning!!.line)
        }
        return view
    }

    private fun makeSpinner(view: View, resource: Int, data: List<String>, defaultPosition: Int = 0){
        val adapter = ArrayAdapter(context!!, R.layout.spinner_item_selected, data)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        val spinner : Spinner = view.findViewById(resource)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mainActivity.warning!!.line = position
            }
        }
        spinner.setSelection(defaultPosition)
    }

    private fun getLines(){
        val prefs: Prefs? = Prefs(context!!)
        val url = prefs?.settingsPath ?: ""
        val center: Int = prefs?.settingsCenter ?: 0
        Router._GETJSON(
            context = context!!,
            url = url,
            params = mapOf("action" to "get-lineas", "centro" to center.toString()),
            responseListener = { response ->
                responseLines(response)
            },
            errorListener = { err ->
                Utils._Alert(context!!,err)
            }
        )
    }

    private fun responseLines( response: Map<String,Any> ){
        val data = response["data"]
        if (data is List<*>) {
            val aux: MutableList<String> =
                data.filterIsInstance<String>().toMutableList()
            aux.add(0, "Seleccionar Linea")
            mainActivity.cache.set("lines", aux)
            makeSpinner(view!!, R.id.spinner_linea, aux, mainActivity.warning!!.line)
        } else {
            Utils._Alert(context!!,"Error en el formato de los datos")
        }
    }

}