package com.example.groceryapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryapp.data.GroceryDatabase
import com.example.groceryapp.data.GroceryItems
import com.example.groceryapp.data.GroceryRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), GroceryRVAdapter.GroceryItemClickInterface {
    lateinit var itemsRV: RecyclerView
    lateinit var addFAB: FloatingActionButton
    lateinit var list: List<GroceryItems>
    lateinit var groceryRVAdapter: GroceryRVAdapter
    lateinit var groceryViewModel: GroceryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        itemsRV = findViewById(R.id.idRVItems)
        addFAB = findViewById(R.id.idFABAdd)
        list = ArrayList<GroceryItems>()
        groceryRVAdapter = GroceryRVAdapter(list, this)
        itemsRV.layoutManager = LinearLayoutManager(this)
        itemsRV.adapter = groceryRVAdapter
        val groceryRepository = GroceryRepository(GroceryDatabase(this))
        val factory = GroceryViewModelFactory(groceryRepository)
        groceryViewModel = ViewModelProvider(this, factory)[GroceryViewModel::class.java]
        groceryViewModel.getAllGroceryItems().observe(this, Observer {
            groceryRVAdapter.list = it
            groceryRVAdapter.notifyDataSetChanged()
        })

        addFAB.setOnClickListener {
            openDialog()
        }
    }

    fun openDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.grocery_add_dialog)
        val cancelButton = dialog.findViewById<Button>(R.id.idBtnCancel)
        val addButton = dialog.findViewById<Button>(R.id.idBtnAdd)
        val itemEdit = dialog.findViewById<EditText>(R.id.idEditItemName)
        val itemPriceEdit = dialog.findViewById<EditText>(R.id.idEditItemPrice)
        val itemQuantityEdit = dialog.findViewById<EditText>(R.id.idEditItemQuantity)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        addButton.setOnClickListener {
            val itemName: String = itemEdit.text.toString()
            val itemPrice: String = itemPriceEdit.text.toString()
            val itemQuantity: String = itemQuantityEdit.text.toString()
            if(itemName.isNotEmpty() && itemPrice.isNotEmpty() && itemQuantity.isNotEmpty()){
                val qty = itemQuantity.toInt()
                val price = itemPrice.toInt()
                val items = GroceryItems(itemName, qty, price)
                groceryViewModel.insert(items)
                Toast.makeText(applicationContext,"Item Inserted", Toast.LENGTH_SHORT).show()
                groceryRVAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }else{
                Toast.makeText(applicationContext,"Please Enter All the Data", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    override fun onItemClick(groceryItems: GroceryItems) {
        groceryViewModel.delete(groceryItems)
        groceryRVAdapter.notifyDataSetChanged()
        Toast.makeText(applicationContext, "Item Deleted", Toast.LENGTH_SHORT).show()
    }
}