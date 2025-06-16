package pe.edu.cibertec.proyecto_efrst.models

data class CartItem(
    var id: String = "",
    var productId: String = "",
    var name: String = "",
    var brand: String = "",
    var price: Double = 0.0,
    var imageUrl: String = "",
    var quantity: Int = 1
)