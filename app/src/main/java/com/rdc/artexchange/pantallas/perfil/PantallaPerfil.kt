package com.rdc.artexchange.pantallas.perfil

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rdc.artexchange.R
import com.rdc.artexchange.navegacion.Screens
import com.rdc.artexchange.pantallas.BottomBar

@Composable
fun PantallaPerfil(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid

    if (userId == null) {
        return
    }

    val userDocument = Firebase.firestore.collection("users").document(userId)

    var nombreUsuario by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var urlFotoPerfil by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        userDocument.get().addOnSuccessListener { document ->
            if (document.exists()) {
                nombreUsuario = document.getString("nombreUsuario") ?: ""
                telefono = document.getString("telefono") ?: ""
                urlFotoPerfil = document.getString("urlFotoPerfil") ?: ""
            }
            isLoading.value = false
        }.addOnFailureListener {
            isLoading.value = false
        }
    }

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
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
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(
                    onClick = {
                        auth.signOut()
                        navController.navigate(Screens.PantallaLogin.name) {
                            popUpTo(Screens.PantallaPerfil.name) { inclusive = true }
                            launchSingleTop = true
                            popUpTo(Screens.PantallaLogin.name) { inclusive = false }
                            restoreState = false
                        }


                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Cerrar Sesion",
                            tint = Color(0xFF1A535C)
                        )
                    }
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
                text = "Perfil",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A535C),
                textAlign = TextAlign.Center
            )

            if (isLoading.value) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(color = Color(0xFF40C9A2))
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = if (urlFotoPerfil.isEmpty()) R.drawable.icono_perfil else urlFotoPerfil,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CampoPerfil(
                        label = "Nombre de usuario",
                        value = nombreUsuario,
                        onValueChange = { if (isEditing) nombreUsuario = it },
                        isEditable = isEditing,
                        leadingIcon = Icons.Default.Person
                    )
                    CampoPerfil(
                        label = "Teléfono",
                        value = telefono,
                        onValueChange = { if (isEditing) telefono = it },
                        isEditable = isEditing,
                        leadingIcon = Icons.Default.Phone,
                        keyboardType = KeyboardType.Phone
                    )
                    CampoPerfil(
                        label = "URL de Foto de Perfil",
                        value = urlFotoPerfil,
                        onValueChange = { if (isEditing) urlFotoPerfil = it },
                        isEditable = isEditing,
                        leadingIcon = Icons.Default.Link,
                        keyboardType = KeyboardType.Uri
                    )

                    Button(
                        onClick = {
                            if (isEditing) {
                                userDocument.update(
                                    mapOf(
                                        "nombreUsuario" to nombreUsuario,
                                        "telefono" to telefono,
                                        "urlFotoPerfil" to urlFotoPerfil
                                    )
                                ).addOnSuccessListener {
                                    Log.d("PantallaPerfil", "Datos actualizados correctamente")
                                }.addOnFailureListener {
                                    Log.e("PantallaPerfil", "Error al actualizar los datos", it)
                                }
                            }
                            isEditing = !isEditing
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF40C9A2)
                        )
                    ) {
                        Text(text = if (isEditing) "Guardar" else "Editar", color = Color.White)
                    }

                    Button(
                        onClick = {
                            navController.navigate(Screens.PantallaProductosComprados.name)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF40C9A2))
                    ) {
                        Text(text = "Mis Compras", color = Color.White)
                    }
                }
            }
        }
    }
}


@Composable
fun CampoPerfil(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEditable: Boolean,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null) },
        enabled = isEditable,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF40C9A2),
            unfocusedBorderColor = Color(0xFFB0BEC5),
            focusedTextColor = Color(0xFF2C3E50),
            unfocusedTextColor = Color(0xFF2C3E50),
            disabledTextColor = Color(0xFF90A4AE),
            disabledBorderColor = Color(0xFFB0BEC5),
            disabledLabelColor = Color(0xFF2C3E50),
            disabledLeadingIconColor = Color(0xFF2C3E50)
        ),
        modifier = Modifier.fillMaxWidth()
    )
}




