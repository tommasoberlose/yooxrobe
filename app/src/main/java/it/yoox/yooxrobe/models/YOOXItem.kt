package it.yoox.yooxrobe.models

import android.util.Log
import com.google.gson.annotations.SerializedName

class YOOXItem(
    var label: String,
    categoryName: String,
    fullPrice: Long,
    retailPrice: Float,
    imageURL: String
) {

    var categories: ArrayList<Category>

    class Category {
        lateinit var products: ArrayList<Product>
    }

    class Product {
        lateinit var price: HashMap<String, Any>

        @SerializedName("itemImages")
        lateinit var images: ArrayList<HashMap<String, String>>
    }

    init {
        val price = HashMap<String, Any>().apply { this["currency"] = "EUR"; this["fullPrice"] = fullPrice; this["retailPrice"] = retailPrice }
        val product = Product().apply { this.price = price; this.images = ArrayList<HashMap<String, String>>().apply { this.add(HashMap<String, String>().apply { this["urlTemplate"] = imageURL }) }}
        val products = ArrayList<Product>().apply { this.add(product) }
        categories = ArrayList<Category>().apply { this.add(Category().apply { this.products = products }) }
        Log.d("yoox", "${categories.size}")
    }
}

