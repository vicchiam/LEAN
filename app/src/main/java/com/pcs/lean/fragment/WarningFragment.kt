package com.pcs.lean.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.pcs.lean.*
import com.pcs.lean.model.Warning
import java.text.SimpleDateFormat
import java.util.*

class WarningFragment : Fragment(){

    private lateinit var mainActivity: MainActivity

    private var prefs : Prefs? = null

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

        mainActivity.warning=mainActivity.warning ?: Warning(Date(),0,"")
        makeEditTextDate(view, R.id.edit_date, mainActivity.warning!!.date)
        val spinner: Spinner = view.findViewById(R.id.spinner_linea)
        spinner.setSelection(mainActivity.warning!!.line)

        var linearLayout: LinearLayout = view.findViewById(R.id.fragment_warning)
        linearLayout.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val imm =
                    context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(activity!!.currentFocus!!.windowToken, 0)

                return v?.onTouchEvent(event) ?: true
            }
        })



        getLines()

        return view
    }

    private fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
        this.setOnTouchListener { v, event ->
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

    private fun makeEditTextDate(view: View, resource: Int, date: Date = Date()){
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(date)
        var editText: EditText = view.findViewById(resource)
        editText.setText(currentDate)
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                var aux: String=dayOfMonth.toString().padStart(2,'0')+"/"+monthOfYear.toString().padStart(2,'0')+"/"+year.toString()
                editText.setText(aux)
                mainActivity.warning!!.date=Utils._StringToDate(aux)
            }
        }
        editText.onRightDrawableClicked {
            var cal = Calendar.getInstance()
            cal.time = Utils._StringToDate(editText.text.toString())
            DatePickerDialog(context!!,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun makeSpinner(view: View, resource: Int, data: List<String>, defaultPosition: Int = 0){
        val adapter = ArrayAdapter(context!!, R.layout.spinner_item_selected, data)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        var spinner : Spinner = view.findViewById(resource)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mainActivity.warning!!.line = position
            }
        }
        spinner.setSelection(defaultPosition)
    }

    private fun makeEditOFs(view: View, resource: Int, text: String){
        var editText: EditText=view.findViewById(resource)
        editText.onRightDrawableClicked {

        }
    }

    private fun getLines(){
        prefs = Prefs(context!!)
        val url = prefs?.settingsPath ?: ""
        val center: Int = prefs?.settingsCenter ?: 0
        if (url.isNotEmpty()) {
            Router._GETJSON(
                context = context!!,
                url = url,
                params = mapOf("action" to "get-lineas", "centro" to center.toString()),
                responseListener = { response ->
                    responseLines(response)
                },
                errorListener = { err ->
                    error(err)
                }
            )
        }
    }

    private fun responseLines( response: Map<String,Any> ){
        var data = response.get("data")
        if (data is List<*>) {
            val aux: MutableList<String> =
                data.filterIsInstance<String>().toMutableList()
            aux.add(0, "Seleccionar Linea")
            makeSpinner(view!!, R.id.spinner_linea, aux, mainActivity.warning!!.line)
        } else {
            error("Error en el formato de los datos")
        }
    }

    private fun error(err: String){
        Log.d("ERROR", err)
        Snackbar.make(
            this.view!!,
            err,
            Snackbar.LENGTH_SHORT
        )
    }

    private fun getOFs(linea: String){

    }





}