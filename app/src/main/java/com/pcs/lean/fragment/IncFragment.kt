package com.pcs.lean.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonSyntaxException
import com.pcs.lean.*
import com.pcs.lean.adapter.IncAdapter
import com.pcs.lean.model.Incidencia
import com.pcs.lean.model.Zona

class IncFragment: Fragment() {

    private lateinit var mainActivity: MainActivity

    private lateinit var incRecyclerView: RecyclerView
    private val incAdapter: IncAdapter = IncAdapter()

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
        val view = inflater.inflate(R.layout.fragment_incidencias, container, false)

        incRecyclerView = view.findViewById(R.id.recyclerView_incidencias)
        incRecyclerView.setHasFixedSize(true)
        incRecyclerView.layoutManager = LinearLayoutManager(context)

        dialog = Utils.modalAlert(mainActivity)

        val floatingActionButton: FloatingActionButton = view.findViewById((R.id.fab))
        floatingActionButton.setOnClickListener{
            mainActivity.navigateToNuevaIncidencia()
        }

        if(mainActivity.cache.get("inc")==null){
            getIncidencias()
        }
        else{
            incAdapter.IncAdapter(this, mainActivity.cache.get("inc") as List<Incidencia>, { incidenciaItem: Incidencia -> incidenciaCliclListener(incidenciaItem) })
            incRecyclerView.adapter = incAdapter
        }

        return view
    }

    private fun incidenciaCliclListener(incidencia: Incidencia){
        mainActivity.cache.set("inc-show", incidencia)
        mainActivity.navigateToShowInc()
    }

    private fun getIncidencias(){
        val prefs: Prefs? = Prefs(context!!)
        val url = prefs?.settingsPath ?: ""
        val center: Int = prefs?.settingsCenter ?: 0
        val idApp: Int = prefs?.idApp ?: 0
        if(!dialog.isShowing)
            dialog.show()
        Router._GET(
            context = context!!,
            url = url,
            params = "action=get-inc&id_dispositivo=$idApp",
            responseListener = { response ->
                if(context!=null) {
                    try {
                        val list: List<Incidencia> = Utils.fromJson(response)
                        mainActivity.cache.set("inc", list)
                        incAdapter.IncAdapter(this, list, { incidenciaItem: Incidencia -> incidenciaCliclListener(incidenciaItem) })
                        incRecyclerView.adapter = incAdapter
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



}