package com.pcs.lean.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.pcs.lean.*
import com.pcs.lean.model.Warning
import java.text.SimpleDateFormat
import java.util.*

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

        mainActivity.warning=mainActivity.warning ?: Warning(Date(),0,"")

        makeEditTextDate(view, R.id.edit_date, mainActivity.warning!!.date)

        makeEditTextOFs(view, R.id.edit_ofs)

        val linearLayout: LinearLayout = view.findViewById(R.id.fragment_warning)
        linearLayout.setOnTouchListener{ _, _ ->
            Utils._closeKeyboard(context!!, mainActivity)
            true
        }

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
        val sdf = SimpleDateFormat("dd/M/yyyy", Locale.forLanguageTag("ES"))
        val currentDate = sdf.format(date)
        val editText: EditText = view.findViewById(resource)
        editText.setText(currentDate)
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val aux: String=dayOfMonth.toString().padStart(2,'0')+"/"+monthOfYear.toString().padStart(2,'0')+"/"+year.toString()
                editText.setText(aux)
                mainActivity.warning!!.date=Utils._StringToDate(aux)
            }
        editText.onRightDrawableClicked {
            val cal = Calendar.getInstance()
            cal.time = Utils._StringToDate(editText.text.toString())
            DatePickerDialog(context!!,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun makeEditTextOFs(view: View, resource: Int){
        val editText: EditText = view.findViewById(resource)
        editText.onRightDrawableClicked {
            mainActivity.navigateToOFsSelector()
        }
    }

}