package com.example.pre_final

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


class Create : Fragment(R.layout.fragment_create) {

    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemArrayList: ArrayList<Item>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create, container, false)
        db = FirebaseFirestore.getInstance()

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        itemArrayList = arrayListOf()
        getItemData()

        return view
    }

    private fun getItemData() {
        val itemCollection = db.collection("items")

        itemCollection.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            value?.let {
                itemArrayList.clear()
                for (document in it) {
                    val item = document.toObject(Item::class.java)
                    itemArrayList.add(item)
                }
//                recyclerView.adapter = Adapter(itemArrayList)
            }
        }
    }
}