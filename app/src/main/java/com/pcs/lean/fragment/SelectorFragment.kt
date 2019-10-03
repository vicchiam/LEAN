package com.pcs.lean.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pcs.lean.*

class SelectorFragment: Fragment(){

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
        val view = inflater.inflate(R.layout.fragment_selector, container, false)

        if(mainActivity.cache.get("OFs")==null){
            getOFs()
        }

        return view
    }

    private fun getOFs(){
        val date = mainActivity.warning!!.date
        val prefs: Prefs? = Prefs(context!!)
        val url = prefs?.settingsPath ?: ""
        Router._GETJSON(
            context = context!!,
            url = url,
            params = mapOf("action" to "get-ofs", "date" to Utils._DateToString(date)),
            responseListener = { response ->
                responseOFs(response)
            },
            errorListener = { err ->
                Utils._Alert(context!!,err)
            }
        )
    }

    private fun responseOFs( response: Map<String,Any> ){
        val data = response["data"]
        if (data is List<*>) {
            Log.d("TRY",data.toString())
            for(elem: String in data.filterIsInstance<String>().toList()){
                Log.d("ELEM",elem);
            }

            //mainActivity.cache.set("OFs", aux)
        } else {
            Utils._Alert(context!!,"Error en el formato de los datos")
        }
    }

}