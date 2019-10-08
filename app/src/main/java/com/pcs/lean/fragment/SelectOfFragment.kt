package com.pcs.lean.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonSyntaxException
import com.pcs.lean.*
import com.pcs.lean.adapter.OfsAdapter
import com.pcs.lean.model.OF

class SelectOfFragment: Fragment(){

    private lateinit var mainActivity: MainActivity

    private lateinit var ofsRecyclerView: RecyclerView
    private val ofsAdapter: OfsAdapter = OfsAdapter()
    private lateinit var search: SearchView

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_select_of, container, false)

        search = view.findViewById(R.id.search)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                ofsAdapter.search(query){}
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                ofsAdapter.search(newText){}
                return true
            }
        })

        ofsRecyclerView = view.findViewById(R.id.recyclerView_ofs)
        ofsRecyclerView.setHasFixedSize(true)
        ofsRecyclerView.layoutManager = LinearLayoutManager(context)

        if(mainActivity.cache.get("OFs")==null){
            getOFs()
        }
        else{
            ofsAdapter.OfsAdapter(this, mainActivity.cache.get("OFs") as MutableList<OF>)
            ofsRecyclerView.adapter = ofsAdapter
        }

        return view
    }

    private fun getOFs(){
        val prefs: Prefs? = Prefs(context!!)
        val url = prefs?.settingsPath ?: ""

        val dialog: AlertDialog = Utils.modalAlert(mainActivity)
        dialog.show()
        Router._GET(
            context = context!!,
            url = url,
            params = "action=get-ofs&date=${Utils.dateToString(mainActivity.warning!!.date)}",
            responseListener = { response ->
                if(context!=null) {
                    try {
                        val list: List<OF> = Utils.fromJson(response)
                        val mutableList = list.toMutableList()
                        mainActivity.cache.set("OFs", mutableList)
                        ofsAdapter.OfsAdapter(this, mutableList)
                        ofsRecyclerView.adapter = ofsAdapter
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

    fun setOF(of: OF){
        Utils.closeKeyboard(context!!, mainActivity)
        mainActivity.warning!!.of=of
        mainActivity.navigateToHome()
    }
}