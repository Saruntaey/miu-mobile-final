package edu.miu.afinal.feature.product_list.viewmodel

import android.R.attr.category
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.miu.afinal.data.model.Category
import edu.miu.afinal.data.model.Product
import edu.miu.afinal.data.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import edu.miu.afinal.feature.product_list.state.ProductEditUiSate


class ProductEditViewModel(
    private val productRepository: ProductRepository,
): ViewModel() {
    private val _editUiState = MutableStateFlow(ProductEditUiSate.empty(Category.ELECTRONICS))
    val editUiState = _editUiState.asStateFlow()

    fun updateProductName(name: String) {
        _editUiState.update {
            it.copy(name = name)
        }
    }

    fun updateProductQuantity(quantity: String) {
        _editUiState.update {
            it.copy(quantity = quantity)
        }
    }

    fun updateProductPrice(price: String) {
        _editUiState.update {
            it.copy(price = price)
        }
    }

    fun setProduct(product: Product) {
        _editUiState.update {
            it.copy(
                id = product.id,
                name = product.name,
                price = product.price.toString(),
                quantity = product.quantity.toString(),
                category = product.category,
            )
        }
    }

    fun upsertProduct() {
        viewModelScope.launch {
            _editUiState.update {
                it.copy(isLoading = true)
            }
            withContext(Dispatchers.IO) {
                if (_editUiState.value.id != 0) {
                    productRepository.updateProduct(
                        Product(
                            id = _editUiState.value.id,
                            name = _editUiState.value.name,
                            price = _editUiState.value.price.toDouble(),
                            quantity = _editUiState.value.quantity.toInt(),
                            category = _editUiState.value.category,
                        )
                    )
                } else {
                    productRepository.insertProduct(
                        Product(
                            name = _editUiState.value.name,
                            price = _editUiState.value.price.toDouble(),
                            quantity = _editUiState.value.quantity.toInt(),
                            category = _editUiState.value.category,
                        )
                    )
                }
            }
                .onSuccess { item ->
                    _editUiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                        )
                    }
                }
                .onFailure { err ->
                    _editUiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = false,
                            error = err.message
                        )
                    }
                }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            _editUiState.update {
                it.copy(isLoading = true)
            }
            withContext(Dispatchers.IO) {
                productRepository.deleteProduct(product)
            }
                .onSuccess { item ->
                    _editUiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                        )
                    }
                }
                .onFailure { err ->
                    _editUiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = false,
                            error = err.message
                        )
                    }
                }
        }
    }

    fun resetEditUiState(category: Category) {
        _editUiState.update {
            ProductEditUiSate.empty(category = category)
        }
    }
}
