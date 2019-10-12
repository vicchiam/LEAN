package com.pcs.lean.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pcs.lean.R
import com.pcs.lean.fragment.SelectIncFragment
import com.pcs.lean.model.TipoIncidencia

class TypeIncAdapter: RecyclerView.Adapter<TypeIncAdapter.ViewHolder>(), Filterable {

    private lateinit var selectIncFragment: SelectIncFragment
    lateinit var searchableList: MutableList<TipoIncidencia>
    lateinit var originalList: List<TipoIncidencia>

    fun IncAdapter(selectIncFragment: SelectIncFragment, searchableList: MutableList<TipoIncidencia>){
        this.selectIncFragment = selectIncFragment
        this.originalList = searchableList
        this.searchableList = ArrayList(searchableList)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = this.searchableList[position]
        holder.bind(item, selectIncFragment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_inc_select, parent, false))
    }

    override fun getItemCount(): Int {
        return this.searchableList.size
    }

    private var onNothingFound: (() -> Unit)? = null

    fun search(s: String?, onNothingFound: (() -> Unit)?) {
        this.onNothingFound = onNothingFound
        filter.filter(s)
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            private val filterResults = FilterResults()

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                searchableList.clear()
                if (constraint.isNullOrBlank()) {
                    searchableList.addAll(originalList)
                } else {
                    val searchResults = originalList.filter {
                        it.getSearchCriteria() == constraint
                    }
                    searchableList.addAll(searchResults)
                }
                return filterResults.also {
                    it.values = searchableList
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (searchableList.isNullOrEmpty())
                    onNothingFound?.invoke()
                notifyDataSetChanged()
            }

        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nombreText: TextView = view.findViewById(R.id.nombre)
        private val descripcionText: TextView = view.findViewById(R.id.descripcion)

        fun bind(inc: TipoIncidencia, selectIncFragment: SelectIncFragment){
            nombreText.text = inc.nombre
            descripcionText.text = inc.descripcion

            itemView.setOnClickListener{
                selectIncFragment.setInc(inc)
            }
        }

    }
}