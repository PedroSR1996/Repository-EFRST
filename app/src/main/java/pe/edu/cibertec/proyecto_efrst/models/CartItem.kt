package pe.edu.cibertec.proyecto_efrst.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    var id: String = "",
    var productId: String = "",
    var name: String = "",
    var brand: String = "",
    var price: Double = 0.0,
    var imageUrl: String = "",
    var quantity: Int = 1,
    var stock: Int = 0
) : Parcelable