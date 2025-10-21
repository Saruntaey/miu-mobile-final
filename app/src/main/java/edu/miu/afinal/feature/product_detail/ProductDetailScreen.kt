package edu.miu.afinal.feature.product_detail

import android.R.attr.text
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.miu.afinal.data.damain.ProductRepositoryImpl
import edu.miu.afinal.data.database.InventoryDatabase

@Composable
fun ProductDetailScreen(modifier: Modifier = Modifier, productId: Int) {
    val context = LocalContext.current
    val database = remember {InventoryDatabase.getDatabase(context = context)}
    val productDao = remember {database.productDao()}
    val viewModel: ProductDetailViewModel = viewModel {
        ProductDetailViewModel(productRepository = ProductRepositoryImpl(productDao = productDao), productId = productId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when{
//        uiState.isLoading -> {
//            Text(text = "Loading...")
//        }
//        uiState.errorMessage != null -> {
//            Text(text = "Error: ${uiState.errorMessage}")
//        }
        else -> {
            Column (
                modifier = modifier,
            ){
                ListItem(
                    headlineContent = { Text(text = uiState.product?.name ?: "unknown") },
                    supportingContent = { Text(text = uiState.product?.quantity.toString() ?: "unknown") },
                    trailingContent = {Text(text = "$${uiState.product?.price}")}
                )
            }
        }
    }
}
