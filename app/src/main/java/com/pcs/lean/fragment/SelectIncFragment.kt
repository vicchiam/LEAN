package com.pcs.lean.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pcs.lean.*
import com.pcs.lean.adapter.IncAdapter
import com.pcs.lean.adapter.SpinnerAdapter
import com.pcs.lean.model.Incidencia
import com.pcs.lean.model.Zona

class SelectIncFragment: Fragment() {

    private lateinit var mainActivity: MainActivity

    private lateinit var incRecyclerView: RecyclerView
    private val incAdapter: IncAdapter = IncAdapter()

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

        if(mainActivity.cache.get("zonas")==null)
            getZonas()
        else
            makeSpinnerZonas(view, (mainActivity.cache.get("zonas") as List<Zona>))

        if(mainActivity.cache.get("inc")==null)
            getIncidencias()
        else{
            incAdapter.IncAdapter(this, (mainActivity.cache.get("inc") as MutableList<Incidencia>) )
            incRecyclerView.adapter = incAdapter
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
                incAdapter.search(data[position].id.toString()){}
            }
        }
        spinner.setSelection(defaultPosition)
    }

    private fun getZonas(){
        val prefs: Prefs? = Prefs(context!!)
        val url = prefs?.settingsPath ?: ""
        val center: Int = prefs?.settingsCenter ?: 0
        Router._GET(
            context = context!!,
            url = url,
            params = "action=get-zonas&centro=$center",
            responseListener = { response ->
                if(context!=null) {
                    val list: List<Zona> = Utils.fromJson(response)
                    mainActivity.cache.set("zonas",list)
                    makeSpinnerZonas(view!!, list)
                }
            },
            errorListener = { err ->
                if(context!=null)
                    Utils.alert(context!!,err)
            }
        )
    }

    private fun getIncidencias(){
        val prefs: Prefs? = Prefs(context!!)
        val url = prefs?.settingsPath ?: ""
        val center: Int = prefs?.settingsCenter ?: 0
        Router._GET(
            context = context!!,
            url = url,
            params = "action=get-inc&centro=$center",
            responseListener = { response ->
                if(context!=null) {
                    val list: List<Incidencia> = Utils.fromJson(response)
                    mainActivity.cache.set("inc",list)

                    incAdapter.IncAdapter(this, (mainActivity.cache.get("inc") as MutableList<Incidencia>) )
                    incRecyclerView.adapter = incAdapter
                }
            },
            errorListener = { err ->
                if(context!=null)
                    Utils.alert(context!!,err)
            }
        )
    }

    fun setInc(incidencia: Incidencia){

    }

}