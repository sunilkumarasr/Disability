package com.smy3infotech.divyaangdisha.AdaptersAndModels.Home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Categorys.CategoriesModel
import com.smy3infotech.divyaangdisha.Config.ViewController
import com.smy3infotech.divyaangdisha.R

class HomeCategoriesAdapter(
    private val items: List<CategoriesModel>,
    private val onItemClick: (CategoriesModel) -> Unit // Click listener function
) : RecyclerView.Adapter<HomeCategoriesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linearImageBg: LinearLayout = itemView.findViewById(R.id.linearImageBg)
        val imgLogo: ImageView = itemView.findViewById(R.id.imgLogo)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)

        init {
            itemView.setOnClickListener {
                val animations = ViewController.animation()
                itemView.startAnimation(animations)
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(items[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_all_categories_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.imgLogo).load(item.category_image).into(holder.imgLogo)
        holder.txtTitle.text = item.category

        // 🎨 Random color list (pastel style for better UI)
        val colors = listOf(
            android.graphics.Color.parseColor("#ffe6e6"), // light red
            android.graphics.Color.parseColor("#ebfaeb"), // light green
            android.graphics.Color.parseColor("#e6f7ff"), // light blue
            android.graphics.Color.parseColor("#e6ffff"), // light yellow
            android.graphics.Color.parseColor("#f9e6ff")  // light purple
        )
        val random = java.util.Random()
        val color = colors[random.nextInt(colors.size)]

        // Rounded background
        val drawable = android.graphics.drawable.GradientDrawable().apply {
            shape = android.graphics.drawable.GradientDrawable.RECTANGLE
            cornerRadius = 30f   // rounded corners
            setColor(color)
        }

        holder.linearImageBg.background = drawable

    }

    override fun getItemCount(): Int {
        return items.size
    }
}
