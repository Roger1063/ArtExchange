package com.rdc.artexchange.pantallas.registro

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.rdc.artexchange.R
import com.rdc.artexchange.navegacion.Screens
import com.rdc.artexchange.pantallas.login.InputField
import com.rdc.artexchange.pantallas.login.LoginScreenViewModel
import com.rdc.artexchange.pantallas.login.PasswordInput

@Composable
fun PantallaRegistro(navController: NavController, viewModel: LoginScreenViewModel = viewModel()) {
    val nombreUsuario = rememberSaveable { mutableStateOf("") }
    val telefono = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val errorMessage = viewModel.errorMessage.value

    val valido = remember(nombreUsuario.value, telefono.value, email.value, password.value) {
        nombreUsuario.value.isNotBlank() && telefono.value.isNotBlank() && email.value.isNotBlank() && password.value.isNotBlank()
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    val context = LocalContext.current
    val token = "307257128437-b1smqs7q7429vjsqjvv7a8acld185kds.apps.googleusercontent.com"

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                viewModel.signInWithGoogleCredential(credential) {
                    navController.navigate(Screens.PantallaHome.name)
                }
            } catch (ex: Exception) {
                Log.d("Login", "GoogleSignIn falló")
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6AB4AC),
                        Color(0xFF47A99B)
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color(0xFFD7EDEB), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logoartexchange),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Crear Cuenta",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            InputField(
                valueState = nombreUsuario,
                labelId = "Nombre Usuario",
                keyboardType = KeyboardType.Text
            )
            InputField(
                valueState = telefono,
                labelId = "Teléfono",
                keyboardType = KeyboardType.Phone
            )
            InputField(
                valueState = email,
                labelId = "Correo electrónico",
                keyboardType = KeyboardType.Email
            )
            PasswordInput(
                passwordState = password,
                passwordVisible = rememberSaveable { mutableStateOf(false) })

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = {
                    viewModel.createUserWithDetails(
                        nombreUsuario = nombreUsuario.value.trim(),
                        telefono = telefono.value.trim(),
                        email = email.value.trim(),
                        password = password.value.trim()
                    ) {
                        viewModel.clearError()
                        navController.navigate(Screens.PantallaHome.name)
                    }
                    keyboardController?.hide()
                },
                enabled = valido,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(10.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF40C9A2))
            ) {
                Text(text = "Crear Cuenta", color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val opciones = GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(token)
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(context, opciones)
                launcher.launch(googleSignInClient.signInIntent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(10.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF347A74))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.iconogoogle),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Continuar con Google", color = Color.White)
            }
        }



        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate(Screens.PantallaLogin.name) }) {
            Text(text = "¿Ya tienes cuenta? Iniciar sesión", color = Color.White)
        }
    }
}

