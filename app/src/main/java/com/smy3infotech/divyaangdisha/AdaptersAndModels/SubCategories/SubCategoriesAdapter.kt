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
import com.smy3infotech.divyaangdisha.Config.ViewController

class SubCategoriesAdapter(
    private val items: List<SubCategoriesModel>,
    private val sub_id: String,
    private val onItemClick: (SubCategoriesModel) -> Unit
) : RecyclerView.Adapter<SubCategoriesAdapter.ViewHolder>() {

    private var selectedSubId: String = sub_id
    private var lastSelectedPosition = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linear: LinearLayout = itemView.findViewById(R.id.linear)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)

        init {
            itemView.setOnClickListener {
                val animations = ViewController.animation()
                it.startAnimation(animations)

                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    lastSelectedPosition = items.indexOfFirst { it.id == selectedSubId } // Save last selected
                    selectedSubId = items[position].id // Update selected subcategory ID
                    notifyDataSetChanged() // Rebind all to reflect selection change
                    onItemClick(items[position]) // Fire click event
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

        val isSelected = item.id == selectedSubId

        if (isSelected) {
            holder.linear.setBackgroundResource(R.drawable.round_cat_select_blue)
            holder.txtTitle.setTextColor(Color.WHITE)

            //Optional animation (based on direction — tweak as needed)
            val animRes = if (position > lastSelectedPosition) {
                R.anim.slide_in_right
            } else {
                R.anim.slide_in_left
            }
            holder.linear.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, animRes))

        } else {
            holder.linear.setBackgroundResource(R.drawable.round_cat_unselect_blue)
            holder.txtTitle.setTextColor(Color.BLACK)
        }

    }

    override fun getItemCount(): Int = items.size
}