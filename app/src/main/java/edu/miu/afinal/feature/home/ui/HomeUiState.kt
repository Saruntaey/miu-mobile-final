package edu.miu.afinal.feature.home.ui

import edu.miu.afinal.data.model.Category

data class HomeUiState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
