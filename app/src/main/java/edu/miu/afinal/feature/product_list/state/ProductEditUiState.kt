package edu.miu.afinal.feature.product_list.state

import edu.miu.afinal.data.model.Category

data class ProductEditUiSate(
    val id: Int = 0,
    val category: Category,
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
) {
    companion object {
        fun empty(category: Category) = ProductEditUiSate(category = category)
    }
}
