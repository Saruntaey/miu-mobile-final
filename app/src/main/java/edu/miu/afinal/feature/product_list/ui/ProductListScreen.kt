package edu.miu.afinal.feature.product_list.ui

import android.R.attr.label
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.miu.afinal.data.damain.ProductRepositoryImpl
import edu.miu.afinal.data.database.InventoryDatabase
import edu.miu.afinal.data.model.Category
import edu.miu.afinal.feature.product_list.state.ProductEditUiSate
import edu.miu.afinal.feature.product_list.viewmodel.ProductListViewModel
import edu.miu.afinal.feature.product_list.viewmodel.ProductEditViewModel
import kotlin.times


@Composable
fun ProductListScreen(
    modifier: Modifier = Modifier,
    category: Category,
    onProductClick: (Int) -> Unit,
) {
    val context = LocalContext.current
    val database = remember {InventoryDatabase.getDatabase(context = context)}
    val productDao = remember {database.productDao()}
    val productListViewModel: ProductListViewModel = viewModel {
        ProductListViewModel(
            productRepository = ProductRepositoryImpl(productDao = productDao),
            category = category
        )
    }
    val productListUiState by productListViewModel.uiState.collectAsStateWithLifecycle()

    val productEditViewModel: ProductEditViewModel = viewModel {
            ProductEditViewModel( productRepository = ProductRepositoryImpl(productDao = productDao))
    }
    val productEditUiState by productEditViewModel.editUiState.collectAsStateWithLifecycle()

    val editDialog = rememberSaveable { mutableStateOf(false) }

    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // open dialog window to add item
                    editDialog.value = true
                    productEditViewModel.resetEditUiState(category = category)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add"
                )
            }
        }
    ){ innerPadding ->
        LazyColumn (
            modifier = modifier.padding(innerPadding),
        ){
            items(productListUiState.products) {
                ListItem(
                    modifier = Modifier.clickable{
                        onProductClick(it.id)
                    },
                    headlineContent = { Text(it.name) },
                    supportingContent = {
                        Column {
                            Text("$${it.price} x ${it.quantity} = $${"%.2f".format(it.price * it.quantity)}")
                        }
                    },
                    trailingContent = {
                        Row {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "edit",
                                modifier = Modifier.clickable {
                                    // on edit
                                    productEditViewModel.setProduct(it)
                                    editDialog.value = true
                                }
                            )
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "delete",
                                modifier = Modifier.clickable {
                                    productEditViewModel.deleteProduct(it)
//                                    createItemViewModel.deleteItem(Item(id=it.id, name = "", price = 0.0, quantity = 0))
//                                    createItemViewModel.deleteItemById(it.id)
                                }
                            )
                        }
                    }
                )
            }
        }
    }

    if (editDialog.value) {
        EditProductDialog(
            productEditUiState = productEditUiState,
            productEditViewModel = productEditViewModel,
            onDismissRequest = {
                editDialog.value = false
            },
            onConfirm = {
                productEditViewModel.upsertProduct()
                editDialog.value = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductDialog(
    productEditUiState: ProductEditUiSate,
    productEditViewModel: ProductEditViewModel,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = productEditUiState.name,
                    onValueChange = productEditViewModel::updateProductName,
                    label = { Text("Name") }
                )
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = productEditUiState.price,
                    onValueChange = productEditViewModel::updateProductPrice,
                    label = { Text("Price") }
                )
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = productEditUiState.quantity,
                    onValueChange = productEditViewModel::updateProductQuantity,
                    label = { Text("Quantity") }
                )
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = onConfirm,
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}
