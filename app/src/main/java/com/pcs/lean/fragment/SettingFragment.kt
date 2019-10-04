package com.pcs.lean.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.pcs.lean.Prefs
import com.pcs.lean.R
import com.pcs.lean.Utils
import kotlinx.android.synthetic.main.fragment_setting.view.*

class SettingFragment : Fragment(){

    private lateinit var spinner: Spinner
    private lateinit var editText: EditText

    private var prefs :Prefs? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_setting, container, false)

        view.btn_settings.setOnClickListener{
            save()
        }

        prefs = Prefs(context!!)

        val position = prefs!!.settingsCenter
        val data = resources.getStringArray(R.array.centers);
        val adapter = ArrayAdapter(context!!, R.layout.spinner_item_selected, data)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner = view.findViewById(R.id.spinner_center)
        spinner.adapter = adapter
        spinner.setSelection(position)

        var url = prefs!!.settingsPath
        editText = view.findViewById(R.id.edit_router)
        editText.setText(url)

        return view
    }

    private fun isValidForm() :Boolean {
        if(editText.text.isEmpty()){
            Utils.alert(context!!, "Debes indicar una URL")
            return false
        }
        return true
    }

    private fun save(){
        if(isValidForm()){
            var position: Int = spinner.selectedItemPosition
            prefs!!.settingsCenter = position

            var url = editText.text.toString()
            prefs!!.settingsPath = url
            Snackbar.make(view!!,"Guardado correctamente", Snackbar.LENGTH_SHORT).show()
        }
    }

}