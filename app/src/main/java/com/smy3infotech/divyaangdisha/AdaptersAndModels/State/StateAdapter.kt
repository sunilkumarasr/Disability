package com.smy3infotech.divyaangdisha.AdaptersAndModels.State

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.smy3infotech.divyaangdisha.R

class StateAdapter(
    context: Context,
    private val states: List<StateModel>
) : ArrayAdapter<StateModel>(context, R.layout.spinner_item, states) {

    init {
        // Define the layout to use for the Spinner dropdown
        setDropDownViewResource(R.layout.spinner_item)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.spinnerItemText)
        textView.text = states[position].state
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.spinnerItemText)
        textView.text = states[position].state
        return view
    }
}