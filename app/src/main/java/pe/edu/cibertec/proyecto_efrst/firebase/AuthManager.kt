package pe.edu.cibertec.proyecto_efrst.firebase

import com.google.firebase.auth.FirebaseAuth

object AuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun register(email: String, password: String, callback: (Boolean, String?, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, auth.currentUser?.uid, null)
                } else {
                    callback(false, null, task.exception?.message)
                }
            }
    }
}