package edu.miu.afinal.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import edu.miu.afinal.data.model.Category

interface AppNavKey: NavKey

@Serializable
data object Login: AppNavKey
@Serializable
data object Home: AppNavKey

@Serializable
data class ProductList(val category: Category): AppNavKey

@Serializable
data class ProductDetail(val productId: Int): AppNavKey

@Serializable
data object Settings: AppNavKey