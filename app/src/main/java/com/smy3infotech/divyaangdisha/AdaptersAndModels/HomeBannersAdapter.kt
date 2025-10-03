package com.smy3infotech.divyaangdisha.AdaptersAndModels

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.smy3infotech.divyaangdisha.AdaptersAndModels.Home.HomeBannersModel
import com.smy3infotech.divyaangdisha.R

class HomeBannersAdapter(
    private val context: Context,
    private val imageList: List<HomeBannersModel>,
    private val viewPager2: ViewPager2
) : RecyclerView.Adapter<HomeBannersAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_banners, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = imageList[position]

        Glide.with(holder.imageView.context)
            .load(item.image)
            .placeholder(R.drawable.vision_dummy)
            .error(R.drawable.vision_dummy)
            .into(holder.imageView)

        if (position == imageList.size - 1) {
            viewPager2.post {
                viewPager2.setCurrentItem(0, false)
            }
        }

        holder.itemView.setOnClickListener {

        }

    }

    override fun getItemCount(): Int = imageList.size

}
