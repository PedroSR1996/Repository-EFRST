package pe.edu.cibertec.proyecto_efrst.models

data class CartItem(
    var id: String = "",
    val productId: String = "",
    val name: String = "",
    val brand: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    var quantity: Int = 1
)
