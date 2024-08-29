package com.royalit.disability.AdaptersAndModels.SubCategoriesItems

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.royalit.disability.AdaptersAndModels.SubCategories.SubCategoriesAdapter
import com.royalit.disability.AdaptersAndModels.SubCategories.SubCategoriesModel
import com.royalit.disability.R

class SubCategoriesItemsAdapter(
    private val items: List<SubCategoriesItemsModel>,
    private val onItemClick: (SubCategoriesItemsModel) -> Unit // Click listener function
) : RecyclerView.Adapter<SubCategoriesItemsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val imgLogo: ImageView = itemView.findViewById(R.id.imgLogo)

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
            .inflate(R.layout.sub_categories_item_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.txtTitle.text = item.text
        holder.imgLogo.setImageResource(item.imageResId)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
