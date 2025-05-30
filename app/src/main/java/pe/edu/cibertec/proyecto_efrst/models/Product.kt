package pe.edu.cibertec.proyecto_efrst.models

data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val brand: String = "",         // si ya lo tienes
    val category: String = ""       // NUEVO CAMPO
)
