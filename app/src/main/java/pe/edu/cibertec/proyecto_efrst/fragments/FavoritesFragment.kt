package pe.edu.cibertec.proyecto_efrst.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pe.edu.cibertec.proyecto_efrst.adapters.ProductAdapter
import pe.edu.cibertec.proyecto_efrst.databinding.FragmentFavoritesBinding
import pe.edu.cibertec.proyecto_efrst.models.Product

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    private val favorites = mutableListOf<Product>()
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ProductAdapter(favorites)
        binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorites.adapter = adapter
        loadFavorites()
    }

    private fun loadFavorites() {
        firestore.collection("users")
            .document(userId)
            .collection("favorites") // <-- esta es la ruta correcta
            .get()
            .addOnSuccessListener { result ->
                favorites.clear()
                for (document in result) {
                    val product = document.toObject(Product::class.java)
                    favorites.add(product)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar favoritos", Toast.LENGTH_SHORT).show()
            }
    }
}
