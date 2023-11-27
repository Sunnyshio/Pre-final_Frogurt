package com.example.pre_final

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class EditItem : AppCompatActivity() {

    private lateinit var itemNameEditText: EditText
    private lateinit var itemDescriptionEditText: EditText
    private lateinit var itemPriceEditText: EditText
    private lateinit var saveButton: Button

    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        val selectedItem = intent.getSerializableExtra("selectedItem") as? Item

        itemNameEditText = findViewById(R.id.editItemName)
        itemDescriptionEditText = findViewById(R.id.editItemDescription)
        itemPriceEditText = findViewById(R.id.editItemPrice)
        saveButton = findViewById(R.id.saveButton)

        if (selectedItem != null) {
            itemNameEditText.setText(selectedItem.name)
            itemDescriptionEditText.setText(selectedItem.description)
            itemPriceEditText.setText(selectedItem.price)

            saveButton.setOnClickListener {
                val editedName = itemNameEditText.text.toString()
                val editedDescription = itemDescriptionEditText.text.toString()
                val editedPrice = itemPriceEditText.text.toString()

                updateItemDetails(selectedItem.id, editedName, editedDescription, editedPrice)
            }
        }

    }

    private fun updateItemDetails(itemId: String, name: String, description: String, price: String) {
        db.collection("items")
            .document(itemId)
            .update(
                "name", name,
                "description", description,
                "price", price
            )
            .addOnSuccessListener {
                Toast.makeText(this, "Item was edited successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Details were not saved", Toast.LENGTH_SHORT).show()
            }
    }
}