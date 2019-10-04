package com.pcs.lean.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pcs.lean.*
import com.pcs.lean.adapter.OfsAdapter
import com.pcs.lean.model.OF

class SelectorFragment: Fragment(){

    private lateinit var mainActivity: MainActivity

    private lateinit var ofsRecyclerView: RecyclerView
    private val ofsAdapter: OfsAdapter = OfsAdapter()

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

        ofsRecyclerView = view.findViewById(R.id.recyclerView_ofs)
        ofsRecyclerView.setHasFixedSize(true)
        ofsRecyclerView.layoutManager = LinearLayoutManager(context)

        if(mainActivity.cache.get("OFs")==null){
            getOFs()
        }
        else{
            ofsAdapter.OfsAdapter(mainActivity.cache.get("OFs") as MutableList<OF>, context!!)
            ofsRecyclerView.adapter = ofsAdapter
        }

        return view
    }

    private fun getOFs(){
        val prefs: Prefs? = Prefs(context!!)
        val url = prefs?.settingsPath ?: ""
        Router.getJSON<List<OF>>(
            context = context!!,
            url = url,
            params = "action=get-ofs&date=${Utils._DateToString(mainActivity.warning!!.date)}",
            responseListener = { response ->
                mainActivity.cache.set("OFs", response)
                ofsAdapter.OfsAdapter( (response as MutableList<OF>), context!!)
                ofsRecyclerView.adapter = ofsAdapter
            },
            errorListener = { err ->
                Utils._Alert(context!!,err)
            }
        )
    }

}