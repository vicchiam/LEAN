package com.pcs.lean.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

interface SpinnerItem{

    fun getItem(): String

}

class SpinnerAdapter(context: Context, private val resource: Int, private var items: List<SpinnerItem>): ArrayAdapter<SpinnerItem>(context, resource, items) {

    private var dropDownViewResource: Int = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent, resource)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if(dropDownViewResource==0)
            createViewFromResource(position, convertView, parent, resource)
        else
            createViewFromResource(position, convertView, parent, dropDownViewResource)
    }

    override fun setDropDownViewResource(resource: Int) {
        dropDownViewResource=resource
        super.setDropDownViewResource(resource)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?, resource: Int): View{
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(resource, parent, false) as TextView
        view.text = items[position].getItem()
        return view
    }

}