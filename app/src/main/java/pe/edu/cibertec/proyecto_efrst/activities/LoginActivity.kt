package pe.edu.cibertec.proyecto_efrst.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import pe.edu.cibertec.proyecto_efrst.databinding.ActivityLoginBinding
import pe.edu.cibertec.proyecto_efrst.firebase.AuthManager
import pe.edu.cibertec.proyecto_efrst.firebase.RealtimeDatabaseManager
import pe.edu.cibertec.proyecto_efrst.home.MainActivity
import pe.edu.cibertec.proyecto_efrst.models.User
import pe.edu.cibertec.proyecto_efrst.utils.Validators

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (!Validators.isValidEmail(email)) {
                binding.etEmail.error = "Correo inválido"
                return@setOnClickListener
            }

            if (!Validators.isValidPassword(password)) {
                binding.etPassword.error = "Mínimo 6 caracteres"
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        AuthManager.login(email, password) { isSuccess, uid ->
            if (isSuccess && uid != null) {
                // Obtener el usuario actual de FirebaseAuth para datos extra
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                if (firebaseUser != null) {
                    checkOrCreateUserInDatabase(uid, firebaseUser.email ?: "", firebaseUser.displayName ?: "Usuario")
                } else {
                    // Por si acaso no está logueado aunque dio éxito (poco probable)
                    checkOrCreateUserInDatabase(uid, "", "Usuario")
                }
            } else {
                Toast.makeText(this, "Error al iniciar sesión. Verifica email y contraseña.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkOrCreateUserInDatabase(uid: String, email: String, name: String) {
        RealtimeDatabaseManager.getUser(uid) { user ->
            if (user != null) {
                goToHome()
            } else {
                val newUser = User(
                    id = uid,
                    name = name,
                    email = email,
                    phone = "",
                    address = "",
                    birthDate = ""
                )
                RealtimeDatabaseManager.saveUser(newUser) { saved ->
                    if (saved) {
                        goToHome()
                    } else {
                        Toast.makeText(this, "Error al guardar usuario", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun goToHome() {
        Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
