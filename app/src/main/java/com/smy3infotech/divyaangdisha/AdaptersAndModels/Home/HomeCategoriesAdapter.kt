package com.smy3infotech.divyaangdisha.AdaptersAndModels.Home

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smy3infotech.divyaangdisha.Activitys.Categorys.CategoriesBasedItemsListActivity
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Categorys.CategoriesModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Categorys.SubCategoriesModel
import com.smy3infotech.divyaangdisha.AdaptersAndModels.SubCategories.HomeSubCategoriesAdapter
import com.smy3infotech.divyaangdisha.Config.ViewController
import com.smy3infotech.divyaangdisha.R
import com.smy3infotech.divyaangdisha.Retrofit.RetrofitClient

class HomeCategoriesAdapter(
    private val items: List<CategoriesModel>,
    private val onItemClick: (CategoriesModel) -> Unit // Click listener function
) : RecyclerView.Adapter<HomeCategoriesAdapter.ViewHolder>() {

    private var expandedPosition = RecyclerView.NO_POSITION

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linearClick: LinearLayout = itemView.findViewById(R.id.linearClick)
        val linearImageBg: LinearLayout = itemView.findViewById(R.id.linearImageBg)
        val imgArrow: ImageView = itemView.findViewById(R.id.imgArrow)
        val imgLogo: ImageView = itemView.findViewById(R.id.imgLogo)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val recyclerview: RecyclerView = itemView.findViewById(R.id.recyclerview)

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

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = items[position]

        Glide.with(holder.imgLogo).load(item.category_image).into(holder.imgLogo)
        holder.txtTitle.text = item.category

        // 🎨 Pastel background color
        val colors = listOf(
            android.graphics.Color.parseColor("#ffe6e6"),
            android.graphics.Color.parseColor("#ebfaeb"),
            android.graphics.Color.parseColor("#e6f7ff"),
            android.graphics.Color.parseColor("#e6ffff"),
            android.graphics.Color.parseColor("#f9e6ff")
        )
        val color = colors.random()
        val drawable = android.graphics.drawable.GradientDrawable().apply {
            shape = android.graphics.drawable.GradientDrawable.RECTANGLE
            cornerRadius = 30f
            setColor(color)
        }
        holder.linearImageBg.background = drawable

        // ✅ Expand/collapse logic
        val isExpanded = position == expandedPosition
        holder.recyclerview.visibility = if (isExpanded) View.VISIBLE else View.GONE

        // ✅ Rotate arrow (animated)
        holder.imgArrow.animate().rotation(if (isExpanded) 90f else 0f).setDuration(200).start()

        // ✅ Only fetch subcategories if expanded
        if (isExpanded) {
            subcategoriesApi(item.category_id, item.category, holder)
        }

        // ✅ Handle click
        holder.linearClick.setOnClickListener {
            val animations = ViewController.animation()
            it.startAnimation(animations)

            if (isExpanded) {
                // Collapse
                expandedPosition = RecyclerView.NO_POSITION
            } else {
                // Expand new, collapse previous
                val previousPosition = expandedPosition
                expandedPosition = position
                notifyItemChanged(previousPosition)
            }

            notifyItemChanged(position)
        }
    }


    private fun subcategoriesApi(catId: String, catName: String, holder: ViewHolder) {
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.subcategoriesApi(catId)
            .enqueue(object : retrofit2.Callback<List<SubCategoriesModel>> {
                override fun onResponse(
                    call: retrofit2.Call<List<SubCategoriesModel>>,
                    response: retrofit2.Response<List<SubCategoriesModel>>
                ) {
                    if (response.isSuccessful) {
                        val categories = response.body()
                        if (!categories.isNullOrEmpty()) {
                            val layoutManager = GridLayoutManager(holder.itemView.context, 1)
                            holder.recyclerview.layoutManager = layoutManager
                            holder.recyclerview.adapter = HomeSubCategoriesAdapter(categories) { item, clickedView ->
                                val animations = ViewController.animation()
                                clickedView.startAnimation(animations)

                                val context = holder.itemView.context
                                val intent = Intent(context, CategoriesBasedItemsListActivity::class.java).apply {
                                    putExtra("category_id", catId)
                                    putExtra("category_Name", catName)
                                    putExtra("sub_id", item.id)
                                }
                                context.startActivity(intent)
                            }
                        }
                    }
                }


                override fun onFailure(
                    call: retrofit2.Call<List<SubCategoriesModel>>,
                    t: Throwable
                ) {
                    Log.e("subcategories_error", t.message.toString())
                }
            })
    }

    override fun getItemCount(): Int {
        return items.size
    }


}
