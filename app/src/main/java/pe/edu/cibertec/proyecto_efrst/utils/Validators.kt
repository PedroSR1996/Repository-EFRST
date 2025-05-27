package pe.edu.cibertec.proyecto_efrst.utils

object Validators {
    fun isValidEmail(email: String) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    fun isValidPassword(password: String) = password.length >= 6
}