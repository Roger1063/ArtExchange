package com.rdc.artexchange.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rdc.artexchange.pantallas.compras.PantallaCompras
import com.rdc.artexchange.pantallas.compras.PantallaDetallesProducto
import com.rdc.artexchange.pantallas.login.LoginScreen
import com.rdc.artexchange.pantallas.home.PantallaHome
import com.rdc.artexchange.pantallas.perfil.PantallaPerfil
import com.rdc.artexchange.pantallas.perfil.PantallaProductosComprados
import com.rdc.artexchange.pantallas.registro.PantallaRegistro
import com.rdc.artexchange.pantallas.splash.SplashScreen
import com.rdc.artexchange.pantallas.ventas.PantallaSubirProducto
import com.rdc.artexchange.pantallas.ventas.PantallaVentas


@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.PantallaAnimacion.name
    ) {
        composable(Screens.PantallaAnimacion.name) {
            SplashScreen(navController = navController)
        }
        composable(Screens.PantallaLogin.name) {
            LoginScreen(navController = navController)
        }
        composable(Screens.PantallaHome.name) {
            PantallaHome(navController = navController)
        }
        composable(Screens.PantallaVentas.name) {
            PantallaVentas(navController = navController)
        }
        composable(Screens.PantallaCompras.name) {
            PantallaCompras(navController = navController)
        }
        composable(Screens.PantallaPerfil.name) {
            PantallaPerfil(navController = navController)
        }
        composable(Screens.PantallaRegistro.name) {
            PantallaRegistro(navController = navController)
        }
        composable(
            route = "${Screens.PantallaSubirProducto.name}?idProducto={idProducto}",
            arguments = listOf(
                navArgument("idProducto") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val idProducto = backStackEntry.arguments?.getString("idProducto")
            PantallaSubirProducto(navController = navController, idProducto = idProducto)
        }
        composable(
            route = "${Screens.PantallaDetallesProducto.name}?idProducto={idProducto}&permiteCompra={permiteCompra}",
            arguments = listOf(
                navArgument("idProducto") {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("permiteCompra") {
                    type = NavType.BoolType
                    defaultValue = true
                }
            )
        ) { backStackEntry ->
            val idProducto = backStackEntry.arguments?.getString("idProducto")
            val permiteCompra = backStackEntry.arguments?.getBoolean("permiteCompra") ?: true

            PantallaDetallesProducto(navController = navController, idProducto = idProducto, permiteCompra = permiteCompra)
        }
        composable(Screens.PantallaProductosComprados.name) {
            PantallaProductosComprados(navController = navController)
        }
    }
}