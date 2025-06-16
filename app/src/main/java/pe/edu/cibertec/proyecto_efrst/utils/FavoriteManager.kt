package pe.edu.cibertec.proyecto_efrst.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import pe.edu.cibertec.proyecto_efrst.models.Product

object FavoriteManager {

    private val dbRef = FirebaseDatabase.getInstance().reference
    private val userId get() = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    fun isFavorite(productId: String, callback: (Boolean) -> Unit) {
        if (userId.isEmpty()) {
            callback(false)
            return
        }

        dbRef.child("users").child(userId).child("favorites").child(productId)
            .get()
            .addOnSuccessListener { snapshot ->
                callback(snapshot.exists())
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun toggleFavorite(product: Product, onToggled: (Boolean) -> Unit) {
        if (userId.isEmpty()) {
            onToggled(false)
            return
        }

        val favRef = dbRef.child("users").child(userId).child("favorites").child(product.id)

        favRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                favRef.removeValue()
                    .addOnSuccessListener { onToggled(false) }
                    .addOnFailureListener { onToggled(true) }
            } else {
                favRef.setValue(product)
                    .addOnSuccessListener { onToggled(true) }
                    .addOnFailureListener { onToggled(false) }
            }
        }
    }
}
