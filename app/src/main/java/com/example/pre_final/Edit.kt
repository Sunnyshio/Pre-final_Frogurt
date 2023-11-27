package com.example.pre_final

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class Edit : AppCompatActivity() {

    private lateinit var add : ImageButton
    private lateinit var home : ImageButton
    private lateinit var itemList : ImageButton
    private lateinit var delete : Button
    private lateinit var edit: Button

    private val db = FirebaseFirestore.getInstance()

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
            itemPrice.text = "${selectedItem.price} Php"
//            itemPrice.text = selectedItem.price

            delete = findViewById(R.id.delete)
            delete.setOnClickListener {
                deleteItem(selectedItem.id)
            }

            edit = findViewById(R.id.edit)
            edit.setOnClickListener {
                val intent = Intent(this, EditItem::class.java)
                intent.putExtra("selectedItem", selectedItem)
                startActivity(intent)
            }
        }

        add = findViewById(R.id.add)
        add.setOnClickListener {
            val intent = Intent(this, Add::class.java)
            startActivity(intent)
        }

        home = findViewById(R.id.home)
        home.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }

        itemList = findViewById(R.id.itemList)
        itemList.setOnClickListener {
            val intent = Intent(this, ItemList::class.java)
            startActivity(intent)
        }

    }

    private fun deleteItem(itemId: String) {
        db.collection("items")
            .document(itemId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Item is removed from database", Toast.LENGTH_SHORT).show()
                finish()
            }

            .addOnFailureListener {
                Toast.makeText(this, "Failed to remove item from database", Toast.LENGTH_SHORT).show()
            }
    }
}