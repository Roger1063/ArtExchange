package com.rdc.artexchange.modelo

data class Producto(
    val idProducto: String = "",
    val nombreProducto: String = "",
    val autor: String = "",
    val precio: Int = 1,
    val fechaFabricacion: String = "",
    val idVendedor: String = "",
    val idComprador: String? = null,
    val urlImagen: String = "",
    val obraDigital: Boolean = false,
    val tipoObra: String = "",
    val nombreUsuarioVendedor: String = ""
)




