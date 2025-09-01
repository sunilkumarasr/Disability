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

class SubCategoriesAdapter(
    private val items: List<SubCategoriesModel>,
    private val onItemClick: (SubCategoriesModel) -> Unit
) : RecyclerView.Adapter<SubCategoriesAdapter.ViewHolder>() {

    private var selectedPosition = 0
    private var lastSelectedPosition = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linear: LinearLayout = itemView.findViewById(R.id.linear)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    lastSelectedPosition = selectedPosition
                    notifyItemChanged(selectedPosition) // Deselect old
                    selectedPosition = position
                    notifyItemChanged(selectedPosition) // Select new
                    onItemClick(items[position]) // Handle click
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sub_categories_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.txtTitle.text = item.subcategory

        if (position == selectedPosition) {
            // Selected style
            holder.linear.setBackgroundResource(R.drawable.round_cat_select_blue)
            holder.txtTitle.setTextColor(Color.WHITE)

            // 👉 Animate based on direction
            if (position > lastSelectedPosition) {
                holder.linear.startAnimation(
                    AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_in_right)
                )
            } else if (position < lastSelectedPosition) {
                holder.linear.startAnimation(
                    AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_in_left)
                )
            }

        } else {
            // Unselected style
            holder.linear.setBackgroundResource(R.drawable.round_cat_unselect_blue)
            holder.txtTitle.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount(): Int = items.size
}