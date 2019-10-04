package com.pcs.lean.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.pcs.lean.R
import com.pcs.lean.model.OF

class OfsAdapter: RecyclerView.Adapter<OfsAdapter.ViewHolder>() {

    lateinit var list: MutableList<OF>
    lateinit var context: Context

    fun OfsAdapter(list: MutableList<OF>, context: Context){
        this.list = list
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = this.list.get(position)
        holder.bind(item, context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_ofs_select, parent, false))
    }

    override fun getItemCount(): Int {
        return this.list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.text)

        fun bind(of: OF, context: Context){
            textView.text = of.orden
            itemView.setOnClickListener{ v ->
                Toast.makeText(context, of.codigo, Toast.LENGTH_LONG)
            }
        }

    }
}