package pe.edu.cibertec.proyecto_efrst.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var imageUrl: String = "",
    var brand: String = "",
    var category: String = "",
    var stock: Int = 0
) : Parcelable
