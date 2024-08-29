package com.royalit.disability.AdaptersAndModels

import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.royalit.disability.R

class SaleAdapter(
    private val items: List<SaleModel>,
    private val onItemClick: (SaleModel) -> Unit // Click listener function
) : RecyclerView.Adapter<SaleAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgLogo: ImageView = itemView.findViewById(R.id.imgLogo)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtOfferPrice: TextView = itemView.findViewById(R.id.txtOfferPrice)
        val txtActulePrice: TextView = itemView.findViewById(R.id.txtActulePrice)
        val txtLocation: TextView = itemView.findViewById(R.id.txtLocation)

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
            .inflate(R.layout.sale_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.imgLogo)
            .load(item.image)
            .placeholder(R.drawable.close_ic)
            .error(R.drawable.close_ic)
            .into(holder.imgLogo)

        holder.txtTitle.text = item.product
        holder.txtOfferPrice.text = "₹ "+item.offer_price
        holder.txtLocation.text = item.address

        val spannableString = SpannableString("₹ "+item.actual_price)
        spannableString.setSpan(StrikethroughSpan(),0,
            spannableString.length,
            0
        )
        holder.txtActulePrice.text = spannableString

    }

    override fun getItemCount(): Int {
        return items.size
    }
}