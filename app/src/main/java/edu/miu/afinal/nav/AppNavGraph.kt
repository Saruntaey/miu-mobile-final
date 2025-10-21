package edu.miu.afinal.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import edu.miu.afinal.data.model.Category
import edu.miu.afinal.feature.home.ui.HomeScreen
import edu.miu.afinal.feature.login.ui.LoginScreen
import edu.miu.afinal.feature.product_detail.ProductDetailScreen
import edu.miu.afinal.feature.product_list.ui.ProductListScreen
import edu.miu.afinal.feature.setting.SettingScreen

@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack<AppNavKey>(Login)

    Scaffold (
        bottomBar = {
            if (backStack.lastOrNull() != Login) {
                NavigationBar {
                    NavigationBarItem(
                        label = { Text("Home") },
                        selected = backStack.lastOrNull() == Home,
                        onClick = {
                            backStack.clear()
                            backStack.add(Home)
                        },
                        icon = {
                            Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
                        },
                    )
                    NavigationBarItem(
                        label = { Text("Settings") },
                        selected = backStack.lastOrNull() == Settings,
                        onClick = {
                            backStack.clear()
                            backStack.add(Settings)
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (backStack.lastOrNull() == ProductList(Category.ELECTRONICS)) {
                                        Badge {
                                            Text(text = "99+")
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings"
                                )
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavDisplay(
            modifier = modifier.padding(innerPadding),
            backStack = backStack,
            onBack = {backStack.removeLastOrNull()},
            entryDecorators = listOf(
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),

            // android DSL
            entryProvider = entryProvider{
                entry<Login> {
                    LoginScreen(
                        onLoginSuccess = {
                            backStack.clear()
                            backStack.add(Home)
                        }
                    )
                }
                entry<Home> {
                    HomeScreen(
                        modifier = modifier,
                        onCategoryClick = {category ->
                            backStack.add(ProductList(category))
                        },
                        onSettingClick = {
                            backStack.add(Settings)
                        }
                    )
                }
                entry<ProductList> {
                    ProductListScreen(
                        modifier = modifier,
                        category = it.category,
                        onProductClick = { productId ->
                            backStack.add(ProductDetail(productId))
                        }
                    )
                }
                entry<ProductDetail> {
                    ProductDetailScreen(
                        modifier = modifier,
                        productId = it.productId,
                    )
                }
                entry<Settings> {
                    SettingScreen()
                }
            },

        )
    }
}