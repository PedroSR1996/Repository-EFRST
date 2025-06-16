package pe.edu.cibertec.proyecto_efrst.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var phone: String = "",
    var address: String = "",
    var birthDate: String = ""
)
