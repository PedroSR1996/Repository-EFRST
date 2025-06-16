package pe.edu.cibertec.proyecto_efrst.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import pe.edu.cibertec.proyecto_efrst.adapters.ProductAdapter
import pe.edu.cibertec.proyecto_efrst.databinding.FragmentFavoritesBinding
import pe.edu.cibertec.proyecto_efrst.models.Product

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val dbRef = FirebaseDatabase.getInstance().reference
    private val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    private val favorites = mutableListOf<Product>()
    private lateinit var adapter: ProductAdapter

    private var favoritesListener: ValueEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ProductAdapter(
            favorites,
            onItemClick = { product ->
                val action = FavoritesFragmentDirections
                    .actionFavoritesFragmentToProductDetailFragment(product)
                findNavController().navigate(action)
            },
            onFavoriteToggled = {
                loadFavorites()
            }
        )

        binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorites.adapter = adapter

        loadFavorites()
    }

    private fun loadFavorites() {
        val favRef = dbRef.child("users").child(userId).child("favorites")

        // Remover listener previo si existe
        favoritesListener?.let { favRef.removeEventListener(it) }

        favoritesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                favorites.clear()
                for (child in snapshot.children) {
                    val product = child.getValue(Product::class.java)
                    product?.let { favorites.add(it) }
                }
                adapter.notifyDataSetChanged()

                binding.tvEmptyFavorites.visibility =
                    if (favorites.isEmpty()) View.VISIBLE else View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error al cargar favoritos", Toast.LENGTH_SHORT).show()
            }
        }
        favRef.addValueEventListener(favoritesListener as ValueEventListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Remover listener para evitar fugas de memoria
        val favRef = dbRef.child("users").child(userId).child("favorites")
        favoritesListener?.let { favRef.removeEventListener(it) }
        _binding = null
    }
}
