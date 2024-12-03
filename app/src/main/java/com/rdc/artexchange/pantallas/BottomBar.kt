package com.rdc.artexchange.pantallas

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.rdc.artexchange.navegacion.Screens

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        Screens.PantallaHome,
        Screens.PantallaVentas,
        Screens.PantallaCompras,
        Screens.PantallaPerfil
    )

    val currentRoute = navController.currentDestination?.route

    val selectedScreen = when {
        currentRoute?.contains(Screens.PantallaProductosComprados.name) == true -> Screens.PantallaPerfil
        currentRoute?.contains(Screens.PantallaDetallesProducto.name) == true && navController.previousBackStackEntry?.destination?.route == Screens.PantallaProductosComprados.name -> Screens.PantallaPerfil
        currentRoute?.contains(Screens.PantallaDetallesProducto.name) == true && navController.previousBackStackEntry?.destination?.route == Screens.PantallaCompras.name -> Screens.PantallaCompras
        currentRoute?.contains(Screens.PantallaSubirProducto.name) == true -> Screens.PantallaVentas
        currentRoute?.contains(Screens.PantallaVentas.name) == true -> Screens.PantallaVentas
        currentRoute?.contains(Screens.PantallaCompras.name) == true -> Screens.PantallaCompras
        currentRoute?.contains(Screens.PantallaHome.name) == true -> Screens.PantallaHome
        currentRoute?.contains(Screens.PantallaPerfil.name) == true -> Screens.PantallaPerfil
        else -> Screens.PantallaHome
    }

    NavigationBar(
        containerColor = Color(0xFF00796B),
        contentColor = Color.White
    ) {
        items.forEach { screen ->
            NavigationBarItem(
                selected = screen == selectedScreen,
                onClick = { navController.navigate(screen.name) },
                icon = {
                    Icon(
                        imageVector = when (screen) {
                            Screens.PantallaHome -> Icons.Filled.Home
                            Screens.PantallaVentas -> Icons.Filled.Sell
                            Screens.PantallaCompras -> Icons.Filled.ShoppingCart
                            Screens.PantallaPerfil -> Icons.Filled.Person
                            else -> Icons.Default.Help
                        },
                        contentDescription = screen.name,
                        tint = if (screen == selectedScreen) Color(0xFF80CBC4) else Color.White
                    )
                },
                label = {
                    Text(
                        text = screen.name.replace("Pantalla", ""),
                        color = if (screen == selectedScreen) Color(0xFF80CBC4) else Color.White,
                        fontWeight = if (screen == selectedScreen) FontWeight.Bold else FontWeight.Normal
                    )
                },
                alwaysShowLabel = true
            )
        }
    }

}
