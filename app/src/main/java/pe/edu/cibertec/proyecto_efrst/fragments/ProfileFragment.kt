package pe.edu.cibertec.proyecto_efrst.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import pe.edu.cibertec.proyecto_efrst.activities.LoginActivity
import pe.edu.cibertec.proyecto_efrst.databinding.FragmentProfileBinding
import pe.edu.cibertec.proyecto_efrst.firebase.RealtimeDatabaseManager
import pe.edu.cibertec.proyecto_efrst.models.User

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cargarDatosUsuario()

        binding.btnCerrarSesion.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun cargarDatosUsuario() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null) {
            val db = FirebaseDatabase.getInstance().reference.child("users").child(uid)
            db.get().addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    binding.tvNombre.text = "Nombre: ${user.name}"
                    binding.tvEmail.text = "Correo: ${user.email}"
                    binding.tvTelefono.text = "Teléfono: ${user.phone}"
                    binding.tvDireccion.text = "Dirección: ${user.address}"
                    binding.tvFechaNacimiento.text = "Fecha de nacimiento: ${user.birthDate}"
                } else {
                    Toast.makeText(requireContext(), "No se pudo cargar el usuario", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar perfil", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
