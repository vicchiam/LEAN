package com.pcs.lean.model

import com.pcs.lean.adapter.SpinnerItem

data class Zona(val id: Long, val zona: String) : SpinnerItem{

    override fun getItem(): String {
        return zona
    }

}