package edu.miu.afinal.feature.product_list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.miu.afinal.data.model.Category
import edu.miu.afinal.data.model.Product
import edu.miu.afinal.data.repository.ProductRepository
import edu.miu.afinal.feature.product_list.state.ProductListUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val productRepository: ProductRepository,
    private val category: Category,
): ViewModel() {
//    private val _uiState = MutableStateFlow(ProductListUiState())
//    val uiState = _uiState.asStateFlow()
//
//    init {
//        loadProductsByCategory()
//    }
    val uiState = productRepository.getProductsByCategory(category)
        .flowOn(Dispatchers.IO)
        .distinctUntilChanged()
        .onStart { ProductListUiState(isLoading = true) }
        .map { products: List<Product> -> ProductListUiState(products = products) }
        .catch { exception -> emit(ProductListUiState(errorMessage = exception.message)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5_000),
            initialValue = ProductListUiState()
        )

    fun loadProductsByCategory() {
        viewModelScope.launch {
            productRepository.getProductsByCategory(category)
                .flowOn(Dispatchers.IO)
                .distinctUntilChanged()
                .onStart { ProductListUiState(isLoading = true) }
                .map { products: List<Product> -> ProductListUiState(products = products) }
                .catch { exception -> emit(ProductListUiState(errorMessage = exception.message)) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.Companion.WhileSubscribed(5_000),
                    initialValue = ProductListUiState()
                )
//                .onSuccess { products ->
//                _uiState.update {
//                    it.copy(
//                        products = products,
//                        isLoading = false,
//                        errorMessage = null,
//                    )
//                }
//            }.onFailure { err: Throwable ->
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        errorMessage = err.message,
//                    )
//                }
//            }
        }

    }
}