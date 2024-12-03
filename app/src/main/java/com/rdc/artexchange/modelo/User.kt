package com.rdc.artexchange.modelo

data class User(
    val userId: String?,
    val nombreUsuario: String?,
    val telefono: String?,
    val urlFotoPerfil: String?
) {
    fun toMap(): MutableMap<String, String?> {
        return mutableMapOf(
            "userId" to this.userId,
            "nombreUsuario" to this.nombreUsuario,
            "telefono" to this.telefono,
            "urlFotoPerfil" to this.urlFotoPerfil
        )
    }
}
