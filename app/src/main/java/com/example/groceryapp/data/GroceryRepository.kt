package com.example.groceryapp.data

import com.example.groceryapp.data.GroceryDatabase
import com.example.groceryapp.data.GroceryItems

class GroceryRepository(private val db: GroceryDatabase) {

    suspend fun insert(item: GroceryItems) = db.getGroceryDao().insert(item)

    suspend fun delete(item: GroceryItems) = db.getGroceryDao().delete(item)

    fun getAllItems() = db.getGroceryDao().getAllGroceryItems()
}