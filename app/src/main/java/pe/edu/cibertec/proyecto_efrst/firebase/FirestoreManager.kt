package pe.edu.cibertec.proyecto_efrst.firebase

import com.google.firebase.firestore.FirebaseFirestore
import pe.edu.cibertec.proyecto_efrst.models.User

object FirestoreManager {
    private val db = FirebaseFirestore.getInstance()

    fun saveUser(user: User, callback: (Boolean) -> Unit) {
        db.collection("users")
            .document(user.uid)
            .set(user)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }
}