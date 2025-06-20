package pe.edu.cibertec.proyecto_efrst.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

object AuthManager {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun register(email: String, password: String, callback: (Boolean, String?, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    callback(true, uid, null)
                } else {
                    val exception = task.exception
                    if (exception is FirebaseAuthUserCollisionException) {
                        callback(false, null, "El correo ya está registrado")
                    } else {
                        callback(false, null, exception?.message)
                    }
                }
            }
    }

    fun login(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    callback(true, uid)
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}
