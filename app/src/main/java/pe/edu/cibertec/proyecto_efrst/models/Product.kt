package pe.edu.cibertec.proyecto_efrst.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val brand: String = "",
    val category: String = "",
    val stock: Int = 0
) : Parcelable
