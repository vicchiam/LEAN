package com.pcs.lean.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.pcs.lean.*
import com.pcs.lean.model.Incidencia
import kotlinx.android.synthetic.main.fragment_show.view.*

class ShowIncFragment: Fragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var incidencia: Incidencia

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_show, container, false)

        incidencia = (mainActivity.cache.get("inc-show") as Incidencia)

        val text_fecha: TextView = view.findViewById(R.id.text_fecha)
        val text_of: TextView = view.findViewById(R.id.text_of)
        val text_inc: TextView = view.findViewById(R.id.text_nombre_inc)
        val text_minutos: TextView = view.findViewById(R.id.text_min)
        val text_coment: TextView = view.findViewById(R.id.text_coment)

        view.btn_delete.setOnClickListener{
            Utils.confirm(context!!, "¿Seguro que lo deseas borrar?", yesEvent = { delete() } )
        }

        text_fecha.text = Utils.dateToString(incidencia.fecha)
        text_of.text = incidencia.OF
        text_inc.text = incidencia.nombre
        text_minutos.text = incidencia.minutos.toString()
        text_coment.text = incidencia.comentario

        return view
    }

    private fun delete(){
        val dialog = Utils.modalAlert(mainActivity, "Eliminando")
        val params = HashMap<String,String>()
        params["action"]="delete-inc"
        params["id"]=incidencia.id.toString()

        val prefs: Prefs? = Prefs(context!!)
        val url = prefs?.settingsPath ?: ""

        dialog.show()
        Router._POST(
            context = context!!,
            url = url,
            params = params,
            responseListener = { response ->
                if(context!=null) {
                    Log.d("RESPONSE",response)
                    if (response == "ok") {
                        Utils.alert(context!!, "Incidencia eliminada correctamente")
                        mainActivity.cache.remove("inc")
                        mainActivity.navigateToHome()
                    }
                    dialog.dismiss()
                }
            },
            errorListener = { err ->
                if(context!=null) {
                    Utils.alert(context!!, err)
                    dialog.dismiss()
                }
            })
    }

}