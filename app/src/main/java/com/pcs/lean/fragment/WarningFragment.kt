package com.pcs.lean.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.pcs.lean.*
import com.pcs.lean.model.OF
import com.pcs.lean.model.Warning
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class WarningFragment : Fragment(){

    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_warning, container, false)

        mainActivity.warning=mainActivity.warning ?: Warning(Date())

        makeEditTextDate(view, mainActivity.warning!!.date)
        makeEditTextOFs(view, mainActivity.warning!!.of.orden)
        makeEditTextIncs(view, mainActivity.warning!!.tipoIncidencia.nombre)
        makeEdiTextMinutes(view, mainActivity.warning!!.minutos)
        makeEditTextComentario(view, mainActivity.warning!!.comentario)
        makeSaveButton(view)

        val linearLayout: LinearLayout = view.findViewById(R.id.fragment_warning)
        linearLayout.setOnTouchListener{ _, _ ->
            Utils.closeKeyboard(context!!, mainActivity)
            true
        }

        return view
    }

    private fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
        this.setOnTouchListener { v, event ->
            Utils.closeKeyboard(context, mainActivity)

            var hasConsumed = false
            if (v is EditText) {
                if (event.x >= v.width - v.totalPaddingRight) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        onClicked(this)
                    }
                    hasConsumed = true
                }
            }
            hasConsumed
        }
    }

    private fun makeEditTextDate(view: View, date: Date = Date()){
        val sdf = SimpleDateFormat("dd/M/yyyy", Locale.forLanguageTag("ES"))
        val currentDate = sdf.format(date)
        val editText: EditText = view.findViewById(R.id.edit_date)
        editText.setText(currentDate)
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val aux: String=dayOfMonth.toString().padStart(2,'0')+"/"+(monthOfYear+1).toString().padStart(2,'0')+"/"+year.toString()
                editText.setText(aux)
                mainActivity.warning!!.date=Utils.stringToDate(aux)
                resetOf()
            }
        editText.onRightDrawableClicked {
            val cal = Calendar.getInstance()
            cal.time = Utils.stringToDate(editText.text.toString())
            Log.d("TIME", Utils.stringToDate(editText.text.toString()).toString())
            DatePickerDialog(context!!,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun makeEditTextOFs(view: View, of: String = ""){
        val editText: EditText = view.findViewById(R.id.edit_ofs)
        editText.setText(of)
        editText.onRightDrawableClicked {
            mainActivity.navigateToOFsSelector()
        }
    }

    private fun makeEditTextIncs(view: View, incidencia: String = ""){
        val editText: EditText = view.findViewById(R.id.edit_incidencia)
        editText.setText(incidencia)
        editText.onRightDrawableClicked {
            mainActivity.navigateToIncSelector()
        }
    }

    private fun makeEdiTextMinutes(view: View, num: Int = 0){
        val editText: EditText = view.findViewById(R.id.edit_minutos)
        val numText: String = if(num==0)"" else num.toString()
        editText.setText(numText)
        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.isNotEmpty())
                    mainActivity.warning!!.minutos=s.toString().toInt()
            }
        })
    }

    private fun makeEditTextComentario(view: View, comentario: String = ""){
        val editText: EditText = view.findViewById(R.id.edit_comentario)
        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(s.isNotEmpty())
                    mainActivity.warning!!.comentario=s.toString()
            }
        })
    }

    private fun makeSaveButton(view: View){
        val buttonSave: Button = view.findViewById(R.id.btn_warning)
        buttonSave.setOnClickListener{
            if(!mainActivity.warning!!.isComplete())
                Utils.alert(context!!, "Faltan campos por rellenar")
            else
                saveWarning()
        }
    }

    private fun saveWarning(){
        var dialog = Utils.modalAlert(mainActivity, "Guardando")
        val params = HashMap<String,String>();
        params["action"]="add-inc"
        params["id_dispositivo"]=mainActivity.idApp.toString()
        params["of"]=mainActivity.warning!!.of.orden
        params["id_tipo_incidencia"]=mainActivity.warning!!.tipoIncidencia.id.toString()
        params["minutos"]=mainActivity.warning!!.minutos.toString()
        params["comentario"]=mainActivity.warning!!.comentario

        val prefs: Prefs? = Prefs(context!!)
        val url = prefs?.settingsPath ?: ""

        dialog.show()
        Router._POST(
            context = context!!,
            url = url,
            params = params,
            responseListener = { response ->
                if(context!=null) {
                    if (response == "ok") {
                        Utils.alert(context!!, "Incidencia guardada correctamente")
                        resetWarning()
                    }
                    dialog.dismiss()
                }
            },
            errorListener = { err ->
                if(context!=null)
                    Utils.alert(context!!,err)
            })
    }

    private fun resetOf(){
        mainActivity.cache.remove("OFs")
        mainActivity.warning!!.of=OF("","","")
        val textView: TextView = view!!.findViewById(R.id.edit_ofs)
        textView.text=""
    }

    private fun resetWarning(){
        mainActivity.warning = Warning(Date())
    }

}