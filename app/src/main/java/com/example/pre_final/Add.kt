package com.example.pre_final

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.UUID


class Add : AppCompatActivity() {

    private val TAG = "AddActivity"
    private var selectedImageUri: Uri? = null

    private lateinit var itemNameEditText : EditText
    private lateinit var itemDescriptionEditText : EditText
    private lateinit var itemPriceEditText : EditText
    private lateinit var addButton : Button
    private lateinit var saveButton : Button
    private lateinit var itemList : ImageButton
    private lateinit var home : ImageButton
    private lateinit var storageReference: StorageReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        itemNameEditText = findViewById(R.id.inputName)
        itemDescriptionEditText = findViewById(R.id.inputDesc)
        itemPriceEditText = findViewById(R.id.inputPrice)
        addButton = findViewById(R.id.upload_btn)
        saveButton = findViewById(R.id.savenew_btn)

        storageReference = FirebaseStorage.getInstance().reference

        addButton.setOnClickListener{
            openImagePicker()
        }

        saveButton.setOnClickListener{
            val itemName = itemNameEditText.text.toString()
            val itemDescription = itemDescriptionEditText.text.toString()
            val itemPrice = itemPriceEditText.text.toString()

            if (selectedImageUri != null) {
                uploadImageToStorage(itemName, itemDescription, itemPrice)
            } else {
                saveDataToFirestore(itemName, itemDescription, itemPrice, "")
            }

        }

        itemList = findViewById(R.id.itemList)
        itemList.setOnClickListener {
            val intent = Intent(this, ItemList::class.java)
            startActivity(intent)
        }

        home = findViewById(R.id.home)
        home.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }

    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                selectedImageUri = data.data
                Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImageToStorage(itemName: String, itemDescription: String, itemPrice: String) {
        val filename = UUID.randomUUID().toString()
        val imageRef = storageReference.child("images/$filename")

        val uploadTask: UploadTask = imageRef.putFile(selectedImageUri!!)

        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                saveDataToFirestore(itemName, itemDescription, itemPrice, uri.toString())
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "Error uploading image", e)
        }
    }

    private fun saveDataToFirestore(itemName: String, itemDescription: String, itemPrice: String, imageUrl: String) {
        val newItem = Item(name = itemName, description = itemDescription, price = itemPrice, url = imageUrl, id = "")

        val db = FirebaseFirestore.getInstance()

        db.collection("items")
            .add(newItem)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Data saved!", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Data was not saved", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Error adding document", e)
            }
    }
}