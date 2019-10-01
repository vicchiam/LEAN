package com.pcs.lean.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.pcs.lean.R
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.view.*

class SettingFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_setting, container, false)

        view.btn_settings.setOnClickListener{ _ ->
            save()
        }

        return view
    }

    private fun isValidForm() :Boolean {
        if(edit_router.text.isEmpty()){
            val builder = AlertDialog.Builder(this.context!!)
            builder.setTitle("Advertencia")
            builder.setMessage(("Debes indicar un path"))
            //builder.setIcon(ContextCompat.getDrawable(context!!, android.R.drawable.ic_dialog_alert))
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
            var editRouter = edit_router.text.toString()

            Log.d("Settings", editRouter)
        }


    }

}