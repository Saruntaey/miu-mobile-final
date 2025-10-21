package edu.miu.afinal.feature.product_detail

import edu.miu.afinal.data.model.Product

data class ProductDetailUiState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
