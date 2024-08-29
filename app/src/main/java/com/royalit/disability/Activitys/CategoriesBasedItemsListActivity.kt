package com.royalit.disability.Activitys

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.disability.AdaptersAndModels.CategoriesHomeAdapter
import com.royalit.disability.AdaptersAndModels.CategoriesModel
import com.royalit.disability.AdaptersAndModels.SubCategories.SubCategoriesAdapter
import com.royalit.disability.AdaptersAndModels.SubCategories.SubCategoriesModel
import com.royalit.disability.AdaptersAndModels.SubCategoriesItems.SubCategoriesItemsAdapter
import com.royalit.disability.AdaptersAndModels.SubCategoriesItems.SubCategoriesItemsModel
import com.royalit.disability.Config.ViewController
import com.royalit.disability.R
import com.royalit.disability.databinding.ActivityAboutUsBinding
import com.royalit.disability.databinding.ActivityCategoriesBasedItemsListBinding

class CategoriesBasedItemsListActivity : AppCompatActivity() {


    val binding: ActivityCategoriesBasedItemsListBinding by lazy {
        ActivityCategoriesBasedItemsListBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.blue), false)

        inits()

    }


    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "List"
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

        dataList()
        dataList2()
    }

    private fun dataList() {
        // Create a static list of data
        val dataList = listOf(
            SubCategoriesModel("All"),
            SubCategoriesModel("Vision Correction"),
            SubCategoriesModel("Contact Lens"),
            SubCategoriesModel("Hearing Clinics"),
            SubCategoriesModel("Sale")
        )

        // Initialize RecyclerView
        binding.Crecyclerview.layoutManager = LinearLayoutManager(this@CategoriesBasedItemsListActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.Crecyclerview.adapter = SubCategoriesAdapter(dataList) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
        }

    }


    private fun dataList2() {
        // Create a static list of data
        val dataList = listOf(
            SubCategoriesItemsModel("Eye Care Vision Hospital",R.drawable.cat_item_1),
            SubCategoriesItemsModel("Hospital",R.drawable.cat_item_2),
            SubCategoriesItemsModel("Eye Care Vision",R.drawable.cat_item_3),
        )

        // Initialize RecyclerView
        binding.recyclerview.layoutManager = LinearLayoutManager(this@CategoriesBasedItemsListActivity)
        binding.recyclerview.adapter = SubCategoriesItemsAdapter(dataList) { item ->
            //Toast.makeText(activity, "Clicked: ${item.text}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@CategoriesBasedItemsListActivity, PostDetailsActivity::class.java))
        }

    }

}