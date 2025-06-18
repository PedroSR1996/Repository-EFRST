package pe.edu.cibertec.proyecto_efrst.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val id: String = "",
    val date: String = "",
    val total: Double = 0.0,
    val items: Map<String, CartItem> = emptyMap()
) : Parcelable