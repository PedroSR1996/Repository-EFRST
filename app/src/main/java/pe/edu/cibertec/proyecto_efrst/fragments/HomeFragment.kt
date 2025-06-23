package pe.edu.cibertec.proyecto_efrst.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import pe.edu.cibertec.proyecto_efrst.activities.LoginActivity
import pe.edu.cibertec.proyecto_efrst.adapters.CarouselAdapter
import pe.edu.cibertec.proyecto_efrst.databinding.FragmentHomeBinding
import pe.edu.cibertec.proyecto_efrst.firebase.AuthManager
import pe.edu.cibertec.proyecto_efrst.firebase.RealtimeDatabaseManager
import pe.edu.cibertec.proyecto_efrst.models.User

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var currentUser: User? = null
    private lateinit var carouselAdapter: CarouselAdapter
    private val handler = Handler(Looper.getMainLooper())
    private var currentSlide = 0

    private val slideRunnable = object : Runnable {
        override fun run() {
            val itemCount = carouselAdapter.itemCount
            if (itemCount > 0) {
                currentSlide = (binding.imageCarousel.currentItem + 1) % itemCount
                binding.imageCarousel.setCurrentItem(currentSlide, true)
                handler.postDelayed(this, 5000)
            }
        }
    }

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
        setupCarousel()

        binding.btnTodos.setOnClickListener { openProductList("Todos") }
        binding.btnPeces.setOnClickListener { openProductList("Comida para peces") }
        binding.btnIluminacion.setOnClickListener { openProductList("Iluminacion") }
        binding.btnFiltros.setOnClickListener { openProductList("Filtros") }
        binding.btnAcuarios.setOnClickListener { openProductList("Acuarios") }
        binding.btnPlantas.setOnClickListener { openProductList("Plantas") }
    }

    private fun loadUser() {
        val uid = AuthManager.getCurrentUserId()
        if (uid != null) {
            RealtimeDatabaseManager.getUser(uid) { user ->
                if (user != null) {
                    currentUser = user
                    binding.tvWelcome.text = "Bienvenido, ${user.name}!"
                } else {
                    Toast.makeText(requireContext(), "No se pudo cargar el usuario", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun openProductList(categoria: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToProductListFragment(categoria)
        findNavController().navigate(action)
    }

    private fun setupCarousel() {
        val images = listOf(
            "https://firebasestorage.googleapis.com/v0/b/proyectoefrst.firebasestorage.app/o/productos%2Fbanner_00.jpg?alt=media&token=92655643-dea1-42f6-82db-bd2d9f9431a2",
            "https://firebasestorage.googleapis.com/v0/b/proyectoefrst.firebasestorage.app/o/productos%2Fbanner_01.jpg?alt=media&token=5ef14627-0238-4831-b093-9f3bf6c72814",
            "https://firebasestorage.googleapis.com/v0/b/proyectoefrst.firebasestorage.app/o/productos%2Fbanner_02.jpg?alt=media&token=d4cce55b-5f2e-455e-b52d-e099805022a4",
            "https://firebasestorage.googleapis.com/v0/b/proyectoefrst.firebasestorage.app/o/productos%2Fbanner_03.jpg?alt=media&token=799767b9-c9a5-432a-a277-61b8fdea837c",
            "https://firebasestorage.googleapis.com/v0/b/proyectoefrst.firebasestorage.app/o/productos%2Fbanner_04.jpg?alt=media&token=65f5083b-2274-4d16-835c-df18bf8c833f"
        )

        carouselAdapter = CarouselAdapter(images)
        binding.imageCarousel.adapter = carouselAdapter

        // Posicionar en el centro para scroll infinito
        val startPosition = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % images.size)
        binding.imageCarousel.setCurrentItem(startPosition, false)

        // Auto slide
        handler.postDelayed(slideRunnable, 2000)

        // Reiniciar auto slide cuando el usuario cambia manualmente
        binding.imageCarousel.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentSlide = position
                handler.removeCallbacks(slideRunnable)
                handler.postDelayed(slideRunnable, 3000)
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(slideRunnable)
        _binding = null
    }
}
