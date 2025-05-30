package pe.edu.cibertec.proyecto_efrst.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pe.edu.cibertec.proyecto_efrst.activities.LoginActivity
import pe.edu.cibertec.proyecto_efrst.databinding.ActivityMainBinding
import pe.edu.cibertec.proyecto_efrst.firebase.AuthManager
import pe.edu.cibertec.proyecto_efrst.firebase.FirestoreManager
import pe.edu.cibertec.proyecto_efrst.models.User

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUser()

        // Botones de categoría
        binding.btnTodos.setOnClickListener { openProductList("Todos") }
        binding.btnLaptops.setOnClickListener { openProductList("Laptops") }
        binding.btnTeclados.setOnClickListener { openProductList("Teclados") }
        binding.btnMouse.setOnClickListener { openProductList("Mouse") }
        binding.btnMonitores.setOnClickListener { openProductList("Monitores") }

        binding.btnLogout.setOnClickListener {
            AuthManager.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun loadUser() {
        val uid = AuthManager.getCurrentUserId()
        if (uid != null) {
            FirestoreManager.getUser(uid) { user ->
                if (user != null) {
                    currentUser = user
                    binding.tvWelcome.text = "Bienvenido, ${user.name}!"
                } else {
                    Toast.makeText(this, "No se pudo cargar el usuario", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun openProductList(categoria: String) {
        val intent = Intent(this, ProductListActivity::class.java)
        intent.putExtra("categoria", categoria)
        startActivity(intent)
    }
}


/*
package pe.edu.cibertec.proyecto_efrst.home

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseApp
import pe.edu.cibertec.proyecto_efrst.R

class `MainActivity` : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔍 Verificación de Firebase
        if (FirebaseApp.getApps(this).isNotEmpty()) {
            Log.d("FirebaseCheck", "✅ Firebase está inicializado correctamente")
        } else {
            Log.e("FirebaseCheck", "❌ Firebase NO está inicializado")
        }
    }
}
*/
