package com.pcs.lean.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pcs.lean.R
import com.pcs.lean.fragment.SelectOfFragment
import com.pcs.lean.model.OF

class OfsAdapter: RecyclerView.Adapter<OfsAdapter.ViewHolder>(), Filterable {

    private lateinit var selectOfFragment: SelectOfFragment
    lateinit var searchableList: MutableList<OF>
    lateinit var originalList: List<OF>

    fun OfsAdapter(selectOfFragment: SelectOfFragment, searchableList: MutableList<OF>){
        this.selectOfFragment = selectOfFragment
        this.originalList = searchableList
        this.searchableList = ArrayList(searchableList)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = this.searchableList[position]
        holder.bind(item, selectOfFragment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_ofs_select, parent, false))
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
                        it.getSearchCriteria().contains(constraint)
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
        private val ordenText: TextView = view.findViewById(R.id.orden)
        private val codigoText: TextView = view.findViewById(R.id.codigo)
        private val nombreText: TextView = view.findViewById(R.id.nombre)

        fun bind(of: OF, selectOfFragment: SelectOfFragment){
            ordenText.text = "OF: ${of.orden}"
            codigoText.text = "Codigo: ${of.codigo}"
            nombreText.text = of.nombre ?: "No se puede mostrar"

            itemView.setOnClickListener{
                selectOfFragment.setOF(of)
            }
        }

    }
}