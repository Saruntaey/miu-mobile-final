package edu.miu.afinal.feature.product_list.state

import edu.miu.afinal.data.model.Product

data class ProductListUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)