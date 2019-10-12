package com.pcs.lean.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pcs.lean.R
import com.pcs.lean.Utils
import com.pcs.lean.fragment.IncFragment
import com.pcs.lean.model.Incidencia

class IncAdapter: RecyclerView.Adapter<IncAdapter.ViewHolder>() {

    private lateinit var list: List<Incidencia>

    fun IncAdapter(incFragment: IncFragment, list: List<Incidencia>){
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_inc, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item: Incidencia = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val ofText: TextView = view.findViewById(R.id.of)
        private val fechaText: TextView = view.findViewById(R.id.fecha)
        private val tipoIncText: TextView = view.findViewById(R.id.tipo_incidencia)
        private val minutosText: TextView = view.findViewById(R.id.minutos)

        fun bind(incidencia: Incidencia){
            ofText.text = incidencia.OF
            fechaText.text = Utils.dateToString(incidencia.fecha)
            tipoIncText.text = incidencia.nombre
            minutosText.text = "Minutos: ${incidencia.minutos}"
        }

    }

}