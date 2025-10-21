package edu.miu.afinal.data.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import edu.miu.afinal.data.model.Category
import edu.miu.afinal.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProductCategories(): List<Category>
    fun getProductById(id: Int): Flow<Product?>
    fun getProductsByCategory(category: Category): Flow<List<Product>>
    suspend fun insertProduct(product: Product): Result<Unit>
    suspend fun updateProduct(product: Product): Result<Unit>
    suspend fun deleteProduct(product: Product): Result<Unit>
}
