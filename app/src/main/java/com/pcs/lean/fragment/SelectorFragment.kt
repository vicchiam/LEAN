package com.pcs.lean.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
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
        val view = inflater.inflate(R.layout.fragment_selector, container, false)

        search = view.findViewById(R.id.search)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i("SEARCH SUB","Llego al querysubmit")
                ofsAdapter.search(query){}
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.i("SEARCH CHA","Llego al querytextchange")
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

            Log.d("SIZE", ofsAdapter.itemCount.toString())
        }

        return view
    }

    private fun getOFs(){
        val prefs: Prefs? = Prefs(context!!)
        val url = prefs?.settingsPath ?: ""

        Router._GET(
            context = context!!,
            url = url,
            params = "action=get-ofs&date=${Utils.dateToString(mainActivity.warning!!.date)}",
            responseListener = { response ->
                val list: List<OF> = Utils.fromJson(response)
                val mutableList = list.toMutableList()
                mainActivity.cache.set("OFs", mutableList)
                ofsAdapter.OfsAdapter(this, mutableList)
                ofsRecyclerView.adapter = ofsAdapter

                Log.d("SIZE", ofsAdapter.itemCount.toString())
            },
            errorListener = { err ->
                Utils.alert(context!!,err)
            }
        )
    }

    fun setOF(of: OF){
        Utils.closeKeyboard(context!!, mainActivity)
        mainActivity.warning!!.of=of.orden
        mainActivity.navigateToHome()
    }
}