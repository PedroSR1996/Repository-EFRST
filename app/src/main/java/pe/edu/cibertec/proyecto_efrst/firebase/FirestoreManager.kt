package pe.edu.cibertec.proyecto_efrst.firebase

import com.google.firebase.firestore.FirebaseFirestore
import pe.edu.cibertec.proyecto_efrst.models.User

object FirestoreManager {

    private val db = FirebaseFirestore.getInstance()
    private const val USERS_COLLECTION = "users"

    fun saveUser(user: User, callback: (Boolean) -> Unit) {
        db.collection(USERS_COLLECTION)
            .document(user.id)
            .set(user)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun getUser(uid: String, callback: (User?) -> Unit) {
        db.collection(USERS_COLLECTION)
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    callback(user)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }
}
