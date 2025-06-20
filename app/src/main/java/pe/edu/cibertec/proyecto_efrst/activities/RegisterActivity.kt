package pe.edu.cibertec.proyecto_efrst.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pe.edu.cibertec.proyecto_efrst.databinding.ActivityRegisterBinding
import pe.edu.cibertec.proyecto_efrst.firebase.AuthManager
import pe.edu.cibertec.proyecto_efrst.firebase.RealtimeDatabaseManager
import pe.edu.cibertec.proyecto_efrst.home.MainActivity
import pe.edu.cibertec.proyecto_efrst.models.User
import pe.edu.cibertec.proyecto_efrst.utils.Validators
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Selector de fecha
        binding.etBirthDate.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    binding.etBirthDate.setText(sdf.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val address = binding.etAddress.text.toString().trim()
            val birthDate = binding.etBirthDate.text.toString().trim()

            // Validaciones básicas
            if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || birthDate.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Validators.isValidEmail(email)) {
                binding.etEmail.error = "Correo inválido"
                return@setOnClickListener
            }

            if (!Validators.isValidPassword(password)) {
                binding.etPassword.error = "Mínimo 6 caracteres"
                return@setOnClickListener
            }

            // Validar teléfono: exactamente 9 dígitos
            if (!phone.matches(Regex("^\\d{9}$"))) {
                binding.etPhone.error = "El teléfono debe tener 9 dígitos"
                return@setOnClickListener
            }

            // Validar fecha de nacimiento no futura
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val birthDateDate = try {
                sdf.parse(birthDate)
            } catch (e: Exception) {
                null
            }
            val today = Calendar.getInstance().time

            if (birthDateDate == null) {
                binding.etBirthDate.error = "Fecha inválida"
                return@setOnClickListener
            } else if (birthDateDate.after(today)) {
                binding.etBirthDate.error = "La fecha no puede ser futura"
                return@setOnClickListener
            }

            registerUser(name, email, password, phone, address, birthDate)
        }
    }

    private fun registerUser(
        name: String,
        email: String,
        password: String,
        phone: String,
        address: String,
        birthDate: String
    ) {
        AuthManager.register(email, password) { isSuccess, uid, errorMsg ->
            if (isSuccess && uid != null) {
                val user = User(
                    id = uid,
                    name = name,
                    email = email,
                    phone = phone,
                    address = address,
                    birthDate = birthDate
                )
                RealtimeDatabaseManager.saveUser(user) { saved ->
                    if (saved) {
                        Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Error guardando usuario", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // Mostrar mensaje específico si email ya registrado
                Toast.makeText(this, errorMsg ?: "Error registrando", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
