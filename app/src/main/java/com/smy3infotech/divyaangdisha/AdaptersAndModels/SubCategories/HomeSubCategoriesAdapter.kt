package com.smy3infotech.divyaangdisha.AdaptersAndModels.SubCategories

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smy3infotech.divyaangdisha.R
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Categorys.SubCategoriesModel


class HomeSubCategoriesAdapter(
    private val items: List<SubCategoriesModel>,
    private val onItemClick: (SubCategoriesModel, View) -> Unit
) : RecyclerView.Adapter<HomeSubCategoriesAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linear: LinearLayout = itemView.findViewById(R.id.linear)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(items[position], it) // Handle click
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_sub_categories_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.txtTitle.text = item.subcategory

    }

    override fun getItemCount(): Int = items.size
}