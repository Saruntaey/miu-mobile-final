package edu.miu.afinal.data.damain

import edu.miu.afinal.data.database.dao.ProductDao
import edu.miu.afinal.data.model.Category
import edu.miu.afinal.data.model.Product
import edu.miu.afinal.data.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(
    private val productDao: ProductDao
): ProductRepository {
    override fun getProductCategories(): List<Category> = Category.entries.toList()
    override fun getProductById(id: Int): Flow<Product?> = productDao.getProductById(id)
    override fun getProductsByCategory(category: Category): Flow<List<Product>> = productDao.getProductsByCategory(category)

    override suspend fun insertProduct(product: Product): Result<Unit> {
        return runCatching {
            productDao.insertProduct(product)
        }
    }

    override suspend fun updateProduct(product: Product): Result<Unit> {
        return runCatching {
            productDao.updateProduct(product)
        }
    }

    override suspend fun deleteProduct(product: Product): Result<Unit> {
        return runCatching {
            productDao.deleteProduct(product)
        }
    }
}