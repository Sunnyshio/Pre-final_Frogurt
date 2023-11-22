package com.example.pre_final

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.bumptech.glide.Glide

class Dashboard : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private lateinit var add : ImageButton
    private lateinit var logout : Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        recyclerView = findViewById(R.id.recyclerView)
        adapter = ItemAdapter()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val db = Firebase.firestore
        db.collection("items")
            .get()
            .addOnSuccessListener { result ->
                val items = mutableListOf<Item>()
                for (document in result) {
                    val item = Item(
                        document.getString("name"),
                        document.getString("description"),
                        document.getString("price"),
                        document.getString("url")
                    )
                    items.add(item)
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                adapter.setItems(items)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        logout = findViewById(R.id.logout_btn)
        logout.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        add = findViewById(R.id.add)
        add.setOnClickListener {
            val intent = Intent(this, Add::class.java)
            startActivity(intent)
        }

        adapter.setOnItemClickListener { selectedItem ->
            val intent = Intent(this, Edit::class.java)
            intent.putExtra("selectedItem", selectedItem)
            startActivity(intent)
        }
    }
}

