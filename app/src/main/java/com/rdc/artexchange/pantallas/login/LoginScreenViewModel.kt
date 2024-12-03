package com.rdc.artexchange.pantallas.login

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.rdc.artexchange.modelo.User
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> get() = _errorMessage

    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit) =
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("MyLogin", "Inicio de sesión exitoso")
                            home()
                        } else {
                            _errorMessage.value = "Usuario o contraseña incorrectos"
                            Log.d(
                                "MyLogin",
                                "Error en inicio de sesión: ${task.exception?.message}"
                            )
                        }
                    }
            } catch (ex: Exception) {
                _errorMessage.value = "Ocurrió un error: ${ex.message}"
                Log.d("MyLogin", "Error en inicio de sesión: ${ex.message}")
            }
        }

    fun signInWithGoogleCredential(credential: AuthCredential, home: () -> Unit) =
        viewModelScope.launch {
            try {
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("MyLogin", "Google logueado!!!!")

                            val user = auth.currentUser

                            if (user != null) {
                                val userDocRef = FirebaseFirestore.getInstance().collection("users")
                                    .document(user.uid)

                                userDocRef.get().addOnSuccessListener { document ->
                                    if (!document.exists()) {
                                        val userData = hashMapOf(
                                            "userId" to user.uid,
                                            "nombreUsuario" to user.displayName.orEmpty(),
                                            "telefono" to "",
                                            "urlFotoPerfil" to user.photoUrl?.toString().orEmpty()
                                        )

                                        userDocRef.set(userData)
                                            .addOnSuccessListener {
                                                Log.d(
                                                    "MyLogin",
                                                    "Usuario creado en Firestore con ID: ${user.uid}"
                                                )
                                                home()
                                            }
                                            .addOnFailureListener {
                                                Log.d(
                                                    "MyLogin",
                                                    "Error al crear usuario en Firestore: ${it.message}"
                                                )
                                            }
                                    } else {

                                        Log.d("MyLogin", "Usuario ya existe en Firestore.")
                                        home()
                                    }
                                }.addOnFailureListener {
                                    Log.d(
                                        "MyLogin",
                                        "Error al obtener datos del usuario: ${it.message}"
                                    )
                                }
                            }
                        } else {
                            Log.d("MyLogin", "signInWithGoogle: ${task.result.toString()}")
                        }
                    }
            } catch (ex: Exception) {
                Log.d("MyLogin", "Error al loguear con Google: ${ex.message}")
            }
        }

    fun createUserWithDetails(
        nombreUsuario: String,
        telefono: String,
        email: String,
        password: String,
        home: () -> Unit
    ) {
        if (_loading.value == false) {
            _loading.value = true

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = task.result.user?.uid ?: return@addOnCompleteListener
                        val userRef =
                            FirebaseFirestore.getInstance().collection("users").document(userId)

                        userRef.get().addOnSuccessListener { document ->
                            if (document.exists()) {
                                _errorMessage.value = "El usuario ya existe."
                            } else {
                                val user = User(
                                    userId = userId,
                                    nombreUsuario = nombreUsuario,
                                    telefono = telefono,
                                    urlFotoPerfil = ""
                                ).toMap()

                                userRef.set(user)
                                    .addOnSuccessListener {
                                        Log.d("MyLogin", "Usuario creado con ID: $userId")
                                        home()
                                    }
                                    .addOnFailureListener {
                                        _errorMessage.value =
                                            "Error al guardar datos del usuario: ${it.message}"
                                    }
                            }
                        }.addOnFailureListener {
                            _errorMessage.value = "Error al verificar el usuario: ${it.message}"
                        }
                    } else {
                        _errorMessage.value = "Error al crear cuenta: ${task.exception?.message}"
                    }
                    _loading.value = false
                }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}


