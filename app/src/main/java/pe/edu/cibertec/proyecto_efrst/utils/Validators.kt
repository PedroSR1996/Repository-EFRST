package pe.edu.cibertec.proyecto_efrst.utils

object Validators {
    fun isValidEmail(email: String) =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isValidPassword(password: String) = password.length >= 6

    fun isValidPhone(phone: String) = phone.matches(Regex("^\\d{9,}$"))

    fun isValidBirthDate(date: String): Boolean {
        return try {
            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            sdf.isLenient = false
            val birth = sdf.parse(date)
            birth.before(java.util.Date())
        } catch (e: Exception) {
            false
        }
    }
}
