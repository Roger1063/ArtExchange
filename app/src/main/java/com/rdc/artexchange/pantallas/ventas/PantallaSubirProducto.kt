package com.rdc.artexchange.pantallas.ventas

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rdc.artexchange.R
import com.rdc.artexchange.modelo.Producto
import com.rdc.artexchange.pantallas.BottomBar
import java.util.Calendar
import java.util.UUID

@Composable
fun PantallaSubirProducto(navController: NavController, idProducto: String?) {
    var nombreProducto by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf(1) }
    var fechaFabricacion by remember { mutableStateOf("") }
    var urlImagen by remember { mutableStateOf("") }
    var obraDigital by remember { mutableStateOf(false) }
    var tipoObra by remember { mutableStateOf("") }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val nombreUsuarioVendedor = FirebaseAuth.getInstance().currentUser?.displayName ?: ""
    val isEditing = idProducto != null
    val isLoading = remember { mutableStateOf(true) }
    val showError = remember { mutableStateOf(false) }

    LaunchedEffect(idProducto) {
        if (isEditing) {
            Firebase.firestore.collection("productos")
                .document(idProducto!!)
                .get()
                .addOnSuccessListener { document ->
                    val producto = document.toObject(Producto::class.java)
                    if (producto != null) {
                        nombreProducto = producto.nombreProducto
                        autor = producto.autor
                        precio = producto.precio
                        fechaFabricacion = producto.fechaFabricacion
                        urlImagen = producto.urlImagen
                        obraDigital = producto.obraDigital
                        tipoObra = producto.tipoObra
                    }
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
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar",
                        tint = Color(0xFF1A535C)
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
                text = if (isEditing) "Modificar Producto" else "Subir Producto",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A535C),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            if (isLoading.value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    CircularProgressIndicator(color = Color(0xFF40C9A2))
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF1F5F9)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            FormField(
                                value = nombreProducto,
                                onValueChange = { nombreProducto = it },
                                label = "Nombre del producto"
                            )
                            FormField(
                                value = autor,
                                onValueChange = { autor = it },
                                label = "Autor"
                            )
                            DatePickerField(
                                value = fechaFabricacion,
                                onValueChange = { fechaFabricacion = it }
                            )
                            PhotoSelector(
                                selectedImageUri = urlImagen,
                                onImageSelected = { uri -> urlImagen = uri }
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = "¿Obra Digital?",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A535C),
                                    modifier = Modifier.weight(1f)
                                )

                                Switch(
                                    checked = obraDigital,
                                    onCheckedChange = { obraDigital = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color(0xFF40C9A2),
                                        uncheckedThumbColor = Color(0xFFB0BEC5),
                                        checkedTrackColor = Color(0xFF40C9A2).copy(alpha = 0.5f),
                                        uncheckedTrackColor = Color(0xFFB0BEC5).copy(alpha = 0.5f)
                                    ),
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }

                            Text(
                                text = "Tipo de obra",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                color = Color(0xFF1A535C)
                            )

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                listOf("Cuadro", "Escultura", "Otro").forEach { tipo ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    ) {
                                        RadioButton(
                                            selected = tipoObra == tipo,
                                            onClick = { tipoObra = tipo },
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = Color(0xFF40C9A2),
                                                unselectedColor = Color(0xFFB0BEC5)
                                            )
                                        )
                                        Text(
                                            text = tipo,
                                            fontSize = 16.sp,
                                            color = Color(0xFF2C3E50)
                                        )
                                    }
                                }
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                            ) {
                                Text(
                                    text = "Precio: ${precio.toInt()} €",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1A535C),
                                    modifier = Modifier
                                        .padding(bottom = 8.dp)
                                )
                                Text(text = "Max(100k)", color = Color(0xFF40C9A2))

                                PriceSliderWithTextField(
                                    precio = precio,
                                    onPriceChange = { precio = it })
                            }

                        }

                        if (showError.value) {
                            Text(
                                text = "Por favor, completa todos los campos correctamente.",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {

                                if (nombreProducto.isNotEmpty() && autor.isNotEmpty() && urlImagen.isNotEmpty() && fechaFabricacion.isNotEmpty() && tipoObra.isNotEmpty()) {
                                    val producto = Producto(
                                        idProducto = idProducto ?: UUID.randomUUID().toString(),
                                        nombreProducto = nombreProducto,
                                        autor = autor,
                                        precio = precio,
                                        fechaFabricacion = fechaFabricacion,
                                        idVendedor = userId,
                                        idComprador = null,
                                        urlImagen = urlImagen,
                                        obraDigital = obraDigital,
                                        tipoObra = tipoObra,
                                        nombreUsuarioVendedor = nombreUsuarioVendedor
                                    )
                                    val docRef = Firebase.firestore.collection("productos")
                                        .document(producto.idProducto)
                                    docRef.set(producto).addOnSuccessListener {
                                        navController.popBackStack()
                                    }
                                } else {
                                    showError.value = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF40C9A2))
                        ) {
                            Text(
                                if (isEditing) "Modificar" else "Subir Producto",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth(),
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
        maxLines = 1,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun PriceSliderWithTextField(precio: Int, onPriceChange: (Int) -> Unit) {
    var textFieldValue by remember { mutableStateOf(precio.toString()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Slider(
            value = precio.toFloat(),
            onValueChange = { newValue ->
                onPriceChange(newValue.toInt())
                textFieldValue = newValue.toInt().toString()
            },
            valueRange = 1f..100000f,
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF40C9A2),
                activeTrackColor = Color(0xFF40C9A2),
                inactiveTrackColor = Color(0xFFB0BEC5)
            )
        )

        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                if (newValue.toIntOrNull() != null && newValue.toInt() in 1..100000) {
                    textFieldValue = newValue
                    onPriceChange(newValue.toInt())
                }
            },
            label = { Text("Precio") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.width(100.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF40C9A2),
                unfocusedBorderColor = Color(0xFFB0BEC5),
                focusedTextColor = Color(0xFF2C3E50),
                unfocusedTextColor = Color(0xFF2C3E50),
                disabledTextColor = Color(0xFF90A4AE),
                disabledBorderColor = Color(0xFFB0BEC5),
                disabledLabelColor = Color(0xFF2C3E50),
                disabledLeadingIconColor = Color(0xFF2C3E50)
            )
        )
    }
}


@Composable
fun PhotoSelector(selectedImageUri: String, onImageSelected: (String) -> Unit) {
    var imageUrl by remember { mutableStateOf(selectedImageUri) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = imageUrl,
            onValueChange = { newUrl ->
                imageUrl = newUrl
                onImageSelected(newUrl)
            },
            label = { Text("URL de la imagen") },
            placeholder = { Text("Ingrese el enlace de la imagen") },
            singleLine = true,
            modifier = Modifier.weight(1f),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF40C9A2),
                unfocusedBorderColor = Color(0xFFB0BEC5),
                focusedTextColor = Color(0xFF2C3E50),
                unfocusedTextColor = Color(0xFF2C3E50),
            )
        )

        if (imageUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Imagen seleccionada",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF00796B))
            )
        } else {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = "Seleccionar imagen",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF00796B)),
                tint = Color.White
            )
        }
    }
}


@Composable
fun DatePickerField(value: String, onValueChange: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                onValueChange(formattedDate)
            },
            year, month, day
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { datePickerDialog.show() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF40C9A2)),
            modifier = Modifier.height(48.dp),
        ) {
            Text(text = "Seleccionar Fecha Fabricacion", color = Color.White)
        }

        Text(
            text = value.ifEmpty { "No seleccionada" },
            style = MaterialTheme.typography.bodyMedium,
            color = if (value.isEmpty()) Color.Gray else Color.Black,
            modifier = Modifier
                .weight(2f)
                .padding(start = 8.dp)
        )
    }
}





