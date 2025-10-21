package edu.miu.afinal.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.HorizontalDivider
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
import edu.miu.afinal.data.model.Category


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onCategoryClick: (Category) -> Unit,
    onSettingClick: () -> Unit,
) {
    val context = LocalContext.current
    val database = remember {InventoryDatabase.getDatabase(context = context)}
    val productDao = remember {database.productDao()}
    val viewModel: HomeViewModel = viewModel {
        HomeViewModel(productRepository = ProductRepositoryImpl(productDao = productDao))
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn (
        modifier = modifier
    ){
        items(uiState.categories) {
            ListItem(
                modifier = Modifier.clickable{
                    onCategoryClick(it)
                },
                headlineContent = { Text(it.name) },
                trailingContent = {
                    Image(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Face"
                    )
                }
            )
            HorizontalDivider()
        }
        item {
            ListItem(
                modifier = Modifier.clickable{
                    onSettingClick()
                },
                headlineContent = { Text("Settings") },
            )
        }
    }
}
