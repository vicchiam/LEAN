package com.pcs.lean.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.pcs.lean.Prefs
import com.pcs.lean.R
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
        spinner.setSelection((position-1))

        var url = prefs!!.settingsPath
        editText = view.findViewById(R.id.edit_router)
        editText.setText(url)

        return view
    }

    private fun isValidForm() :Boolean {
        if(editText.text.isEmpty()){
            val builder = AlertDialog.Builder(this.context!!)
            builder.setTitle("Advertencia")
            builder.setMessage(("Debes indicar un path"))
            builder.setNeutralButton("Aceptar"){ dialog, _ ->
                dialog.cancel()
            }
            builder.show()
            return false
        }
        return true
    }

    private fun save(){
        if(isValidForm()){
            var position: Int = spinner.selectedItemPosition
            prefs!!.settingsCenter = (position+1)

            var url = editText.text.toString()
            prefs!!.settingsPath = url
            Snackbar.make(view!!,"Guardado correctamente", Snackbar.LENGTH_SHORT).show()
        }
    }

}