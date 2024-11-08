package com.smy3infotech.divyaangdish.AdaptersAndModels.JobAlerts

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smy3infotech.divyaangdish.R

class JobAlertHomeAdapter(
    private val items: List<JobAlertModel>,
    private val onItemClick: (JobAlertModel) -> Unit // Click listener function
) : RecyclerView.Adapter<JobAlertHomeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtDec: TextView = itemView.findViewById(R.id.txtDec)
        val txtPostTime: TextView = itemView.findViewById(R.id.txtPostTime)
        val txtLastTime: TextView = itemView.findViewById(R.id.txtLastTime)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(items[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.job_alerts_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.txtTitle.text = item.title
        holder.txtDec.text = Html.fromHtml(item.description, Html.FROM_HTML_MODE_LEGACY)
        holder.txtPostTime.text = item.post_date
        holder.txtLastTime.text = item.last_date
    }

    override fun getItemCount(): Int {
        return items.size
    }
}