package edu.miu.afinal.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.miu.afinal.data.model.Category
import edu.miu.afinal.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class HomeViewModel(
    private val productRepository: ProductRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            runCatching {
                productRepository.getProductCategories()
            }.onSuccess {categories: List<Category> ->
                _uiState.update{
                    it.copy(
                        isLoading = false,
                        categories = categories,
                        errorMessage = null,
                    )
                }
            }.onFailure { err: Throwable ->
                _uiState.update{
                    it.copy(
                        isLoading = false,
                        categories = emptyList(),
                        errorMessage = err.message,
                    )
                }
            }
        }
    }
}
