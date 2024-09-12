package com.royalit.disability.AdaptersAndModels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.royalit.disability.R

class MyProductsListAdapter(
    private val items: List<MyProductsModel>,
    private val onItemClick: (MyProductsModel, Any?) -> Unit,
) : RecyclerView.Adapter<MyProductsListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgLogo: ImageView = itemView.findViewById(R.id.imgLogo)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtStatus: TextView = itemView.findViewById(R.id.txtStatus)
        val txtActiveStatus: TextView = itemView.findViewById(R.id.txtActiveStatus)
        val linearView: LinearLayout = itemView.findViewById(R.id.linearView)
        val linearEdit: LinearLayout = itemView.findViewById(R.id.linearEdit)
        val linearDelete: LinearLayout = itemView.findViewById(R.id.linearDelete)


        init {

            linearView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(items[position],"View")
                }
            }

            linearEdit.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(items[position],"Edit")
                }
            }

            linearDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(items[position],"Delete")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_products_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.txtTitle.text = item.product
        Glide.with(holder.imgLogo).load(item.additional_images.firstOrNull()).error(R.drawable.vision_dummy).into(holder.imgLogo)

    }

    override fun getItemCount(): Int {
        return items.size
    }

}