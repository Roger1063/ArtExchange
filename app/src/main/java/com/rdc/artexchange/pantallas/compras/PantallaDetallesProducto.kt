package com.rdc.artexchange.pantallas.compras

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rdc.artexchange.R
import com.rdc.artexchange.modelo.Producto
import com.rdc.artexchange.navegacion.Screens
import com.rdc.artexchange.pantallas.BottomBar

@Composable
fun PantallaDetallesProducto(
    navController: NavController,
    idProducto: String?,
    permiteCompra: Boolean
) {
    val producto = remember { mutableStateOf<Producto?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(idProducto) {
        if (!idProducto.isNullOrEmpty()) {
            Firebase.firestore.collection("productos")
                .document(idProducto)
                .get()
                .addOnSuccessListener { document ->
                    producto.value = document.toObject(Producto::class.java)
                    isLoading.value = false
                }
                .addOnFailureListener {
                    isLoading.value = false
                }
        } else {
            isLoading.value = false
        }
    }

    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF1F5F9)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp )
                    .height(30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar",
                        tint = Color(0xFF1A535C),
                    )
                }
            }

            Image(
                painter = painterResource(id = R.drawable.logoartexchange),
                contentDescription = "Logo Art Exchange",
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp)
            )

            Text(
                text = "Detalles del Producto",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A535C),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading.value) {
                CircularProgressIndicator(color = Color(0xFF40C9A2))
            } else {
                producto.value?.let { prod ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .wrapContentHeight(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            AsyncImage(
                                model = prod.urlImagen,
                                contentDescription = "Imagen del producto",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1.5f)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Fit
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                DetalleProducto(titulo = "Nombre", valor = prod.nombreProducto)
                                DetalleProducto(titulo = "Autor", valor = prod.autor)
                                DetalleProducto(
                                    titulo = "Precio",
                                    valor = prod.precio.toString() + " €"
                                )
                                DetalleProducto(
                                    titulo = "Fecha de fabricación",
                                    valor = prod.fechaFabricacion
                                )
                                DetalleProducto(
                                    titulo = "Obra digital",
                                    valor = if (prod.obraDigital) "Sí" else "No"
                                )
                                DetalleProducto(titulo = "Tipo de obra", valor = prod.tipoObra)
                                DetalleProducto(
                                    titulo = "Usuario del vendedor",
                                    valor = prod.nombreUsuarioVendedor
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (permiteCompra) {
                        Button(
                            onClick = {
                                comprarProducto(prod)
                                navController.navigate(Screens.PantallaCompras.name) {
                                    popUpTo(Screens.PantallaCompras.name) { inclusive = true }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF40C9A2)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Comprar",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                } ?: run {
                    Text(
                        text = "El producto no existe o no se pudo cargar.",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DetalleProducto(titulo: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.Gray
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
    }
}

fun comprarProducto(producto: Producto) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    Firebase.firestore.collection("productos")
        .document(producto.idProducto)
        .update("idComprador", userId)
        .addOnSuccessListener {
            Log.d("PantallaDetallesProducto", "Producto comprado exitosamente")
        }
        .addOnFailureListener {
            Log.e("PantallaDetallesProducto", "Error al comprar el producto", it)
        }
}

