package com.example.pre_final

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class Edit : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val selectedItem = intent.getSerializableExtra("selectedItem") as? Item

        if (selectedItem != null) {
            val itemPhoto: ImageView = findViewById(R.id.viewItemPhoto)
            val itemName: TextView = findViewById(R.id.viewItemName)
            val itemDescription: TextView = findViewById(R.id.viewItemDescription)
            val itemPrice: TextView = findViewById(R.id.viewItemPrice)

            Glide.with(this)
                .load(selectedItem.url)
                .placeholder(R.drawable.update)
                .error(R.drawable.update)
                .into(itemPhoto)
            itemName.text = selectedItem.name
            itemDescription.text = selectedItem.description
            itemPrice.text = selectedItem.price
        }
    }
}