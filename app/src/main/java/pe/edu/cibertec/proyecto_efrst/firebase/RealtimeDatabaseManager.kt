package pe.edu.cibertec.proyecto_efrst.firebase

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import pe.edu.cibertec.proyecto_efrst.models.User

object RealtimeDatabaseManager {

    private val db = FirebaseDatabase.getInstance().reference
    private const val USERS_NODE = "users"

    fun saveUser(user: User, callback: (Boolean) -> Unit) {
        db.child(USERS_NODE)
            .child(user.id)
            .setValue(user)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun getUser(uid: String, callback: (User?) -> Unit) {
        db.child(USERS_NODE)
            .child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    callback(user)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null)
                }
            })
    }
}
