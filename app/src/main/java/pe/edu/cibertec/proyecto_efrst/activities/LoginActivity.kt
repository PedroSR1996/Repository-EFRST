package pe.edu.cibertec.proyecto_efrst.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pe.edu.cibertec.proyecto_efrst.databinding.ActivityLoginBinding
import pe.edu.cibertec.proyecto_efrst.firebase.AuthManager
import pe.edu.cibertec.proyecto_efrst.home.MainActivity
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
        AuthManager.login(email, password) { isSuccess, errorMsg ->
            if (isSuccess) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, errorMsg ?: "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
