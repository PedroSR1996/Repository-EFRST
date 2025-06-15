package pe.edu.cibertec.proyecto_efrst.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pe.edu.cibertec.proyecto_efrst.models.Product

object FavoriteManager {
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    fun isFavorite(productId: String, callback: (Boolean) -> Unit) {
        firestore.collection("users").document(userId)
            .collection("favorites").document(productId)
            .get()
            .addOnSuccessListener { snapshot ->
                callback(snapshot.exists())
            }
    }

    fun toggleFavorite(product: Product, onToggled: (Boolean) -> Unit) {
        val favRef = firestore.collection("users").document(userId)
            .collection("favorites").document(product.id)

        favRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                favRef.delete().addOnSuccessListener { onToggled(false) }
            } else {
                favRef.set(product).addOnSuccessListener { onToggled(true) }
            }
        }
    }
}