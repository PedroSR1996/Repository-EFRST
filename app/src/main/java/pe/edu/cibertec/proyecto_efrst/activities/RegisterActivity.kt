package pe.edu.cibertec.proyecto_efrst.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pe.edu.cibertec.proyecto_efrst.databinding.ActivityRegisterBinding
import pe.edu.cibertec.proyecto_efrst.firebase.AuthManager
import pe.edu.cibertec.proyecto_efrst.firebase.FirestoreManager
import pe.edu.cibertec.proyecto_efrst.models.User
import pe.edu.cibertec.proyecto_efrst.utils.Validators
import pe.edu.cibertec.proyecto_efrst.home.MainActivity

class RegisterActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
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

            registerUser(name, email, password)
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        AuthManager.register(email, password) { isSuccess, uid, errorMsg ->
            if (isSuccess && uid != null) {
                val user = User(uid, name, email)
                FirestoreManager.saveUser(user) { saved ->
                    if (saved) {
                        Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Error guardando usuario", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, errorMsg ?: "Error registrando", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
