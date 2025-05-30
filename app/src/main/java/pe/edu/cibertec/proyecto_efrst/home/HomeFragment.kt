package pe.edu.cibertec.proyecto_efrst.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import pe.edu.cibertec.proyecto_efrst.activities.LoginActivity
import pe.edu.cibertec.proyecto_efrst.databinding.FragmentHomeBinding
import pe.edu.cibertec.proyecto_efrst.firebase.AuthManager
import pe.edu.cibertec.proyecto_efrst.firebase.FirestoreManager
import pe.edu.cibertec.proyecto_efrst.models.User

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var currentUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUser()

        binding.btnTodos.setOnClickListener { openProductList("Todos") }
        binding.btnLaptops.setOnClickListener { openProductList("Laptops") }
        binding.btnTeclados.setOnClickListener { openProductList("Teclados") }
        binding.btnMouse.setOnClickListener { openProductList("Mouse") }
        binding.btnMonitores.setOnClickListener { openProductList("Monitores") }

        binding.btnLogout.setOnClickListener {
            AuthManager.logout()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
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
                    Toast.makeText(requireContext(), "No se pudo cargar el usuario", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun openProductList(categoria: String) {
        val intent = Intent(requireContext(), ProductListActivity::class.java)
        intent.putExtra("categoria", categoria)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
