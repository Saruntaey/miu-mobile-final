package edu.miu.afinal.feature.product_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.miu.afinal.data.model.Product
import edu.miu.afinal.data.repository.ProductRepository
import edu.miu.afinal.feature.product_list.state.ProductListUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailViewModel(
    private val productRepository: ProductRepository,
    private val productId: Int,
): ViewModel()  {
//    private val _uiState = MutableStateFlow(ProductDetailUiState())
//    val uiState = _uiState.asStateFlow()

//    init {
//        loadProductDetail()
//    }
    val uiState: StateFlow<ProductDetailUiState> = productRepository.getProductById(productId)
        .flowOn(Dispatchers.IO)
        .distinctUntilChanged()
        .onStart { ProductDetailUiState(isLoading = true) }
        .map { product: Product? -> ProductDetailUiState( product = product ) }
        .catch { exception -> emit(ProductDetailUiState(errorMessage = exception.message)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProductDetailUiState()
        )

    fun loadProductDetail() {
        viewModelScope.launch {
//            _uiState.update {
//                it.copy(
//                    isLoading = true,
//                    errorMessage = null,
//                )
//            }
            productRepository.getProductById(productId)
                .flowOn(Dispatchers.IO)
                .distinctUntilChanged()
                .onStart { ProductDetailUiState(isLoading = true) }
                .map { product: Product? -> ProductDetailUiState( product = product ) }
                .catch { exception -> emit(ProductDetailUiState(errorMessage = exception.message)) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = ProductListUiState()
                )
//                .onSuccess { product: Product? ->
//                _uiState.update {
//                    it.copy(
//                        product = product,
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
