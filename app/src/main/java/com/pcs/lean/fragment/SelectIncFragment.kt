package com.pcs.lean.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonSyntaxException
import com.pcs.lean.*
import com.pcs.lean.adapter.TypeIncAdapter
import com.pcs.lean.adapter.SpinnerAdapter
import com.pcs.lean.model.TipoIncidencia
import com.pcs.lean.model.Zona

class SelectIncFragment: Fragment() {

    private lateinit var mainActivity: MainActivity

    private lateinit var incRecyclerView: RecyclerView
    private val typeIncAdapter: TypeIncAdapter = TypeIncAdapter()

    private lateinit var dialog: AlertDialog

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_select_inc, container, false)

        incRecyclerView = view.findViewById(R.id.recyclerView_inc)
        incRecyclerView.setHasFixedSize(true)
        incRecyclerView.layoutManager = LinearLayoutManager(context)

        dialog = Utils.modalAlert(mainActivity)

        if(mainActivity.cache.get("zonas")==null)
            getZonas()
        else
            makeSpinnerZonas(view, (mainActivity.cache.get("zonas") as List<Zona>))

        if(mainActivity.cache.get("type-inc")==null)
            getIncidencias()
        else{
            typeIncAdapter.IncAdapter(this, (mainActivity.cache.get("inc") as MutableList<TipoIncidencia>) )
            incRecyclerView.adapter = typeIncAdapter
        }



        return view
    }

    private fun makeSpinnerZonas(view: View, data: List<Zona>, defaultPosition: Int = 0){
        val adapter = SpinnerAdapter(context!!, R.layout.spinner_item_selected, data)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        val spinner : Spinner = view.findViewById(R.id.spinner_zona)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                typeIncAdapter.search(data[position].id.toString()){}
            }
        }
        spinner.setSelection(defaultPosition)
    }

    private fun getZonas(){
        val prefs: Prefs? = Prefs(context!!)
        val url = prefs?.settingsPath ?: ""
        val center: Int = prefs?.settingsCenter ?: 0
        if(!dialog.isShowing)
            dialog.show()
        Router._GET(
            context = context!!,
            url = url,
            params = "action=get-zonas&centro=$center",
            responseListener = { response ->
                if(context!=null) {
                    try {
                        val list: List<Zona> = Utils.fromJson(response)
                        mainActivity.cache.set("zonas", list)
                        makeSpinnerZonas(view!!, list)
                    }
                    catch (ex: JsonSyntaxException){
                        Utils.alert(context!!,"El formato de la respuesta no es correcto: $response")
                    }
                    dialog.dismiss()
                }
            },
            errorListener = { err ->
                if(context!=null) {
                    Utils.alert(context!!, err)
                    dialog.dismiss()
                }
            }
        )
    }

    private fun getIncidencias(){
        val prefs: Prefs? = Prefs(context!!)
        val url = prefs?.settingsPath ?: ""
        val center: Int = prefs?.settingsCenter ?: 0
        if(!dialog.isShowing)
            dialog.show()
        Router._GET(
            context = context!!,
            url = url,
            params = "action=get-tipo-inc&centro=$center",
            responseListener = { response ->
                if(context!=null) {
                    try {
                        val list: List<TipoIncidencia> = Utils.fromJson(response)
                        mainActivity.cache.set("type-inc", list)

                        typeIncAdapter.IncAdapter(
                            this,
                            (list as MutableList<TipoIncidencia>)
                        )
                        incRecyclerView.adapter = typeIncAdapter
                    }
                    catch (ex: JsonSyntaxException){
                        Utils.alert(context!!,"El formato de la respuesta no es correcto: $response")
                    }
                    dialog.dismiss()
                }
            },
            errorListener = { err ->
                if(context!=null) {
                    Utils.alert(context!!, err)
                    dialog.dismiss()
                }
            }
        )
    }

    fun setInc(incidencia: TipoIncidencia){
        mainActivity.nuevaIncidencia!!.tipoIncidencia = incidencia
        mainActivity.navigateToNuevaIncidencia()
    }

}