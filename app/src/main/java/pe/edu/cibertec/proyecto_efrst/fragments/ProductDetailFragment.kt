package pe.edu.cibertec.proyecto_efrst.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pe.edu.cibertec.proyecto_efrst.databinding.FragmentProductDetailBinding
import pe.edu.cibertec.proyecto_efrst.models.Product

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var product: Product
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            product = ProductDetailFragmentArgs.fromBundle(it).product
        }

        binding.tvProductNameDetail.text = product.name
        binding.tvProductBrandDetail.text = product.brand
        binding.tvProductPriceDetail.text = "S/ %.2f".format(product.price)
        binding.tvProductStockDetail.text = "Stock: ${product.stock}"
        binding.tvProductDescription.text = product.description

        Glide.with(requireContext())
            .load(product.imageUrl)
            .into(binding.imgProductDetail)

        binding.btnAddToCart.setOnClickListener {
            addToCart()
        }

        binding.btnAddToFavorites.setOnClickListener {
            toggleFavorite()
        }

        checkIfFavorite()
    }

    private fun checkIfFavorite() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users")
            .document(userId)
            .collection("favorites")
            .document(product.id)
            .get()
            .addOnSuccessListener { document ->
                isFavorite = document.exists()
                updateFavoriteButton()
                binding.btnAddToFavorites.visibility = View.VISIBLE // ✅ Mostrar después de saber el estado
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al verificar favorito", Toast.LENGTH_SHORT).show()
            }
    }

    private fun toggleFavorite() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "Debes iniciar sesión", Toast.LENGTH_SHORT).show()
            return
        }

        val favoritesRef = db.collection("users")
            .document(userId)
            .collection("favorites")
            .document(product.id)

        if (isFavorite) {
            // Quitar de favoritos
            favoritesRef.delete()
                .addOnSuccessListener {
                    isFavorite = false
                    updateFavoriteButton()
                    Toast.makeText(requireContext(), "Eliminado de favoritos", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error al quitar de favoritos", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Agregar a favoritos
            favoritesRef.set(product)
                .addOnSuccessListener {
                    isFavorite = true
                    updateFavoriteButton()
                    Toast.makeText(requireContext(), "Agregado a favoritos", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error al agregar a favoritos", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun updateFavoriteButton() {
        if (isFavorite) {
            binding.btnAddToFavorites.text = "Quitar de favoritos"
            binding.btnAddToFavorites.setBackgroundTintList(
                ContextCompat.getColorStateList(requireContext(), android.R.color.darker_gray)
            )
        } else {
            binding.btnAddToFavorites.text = "Agregar a favoritos"
            binding.btnAddToFavorites.setBackgroundTintList(
                ContextCompat.getColorStateList(requireContext(), android.R.color.holo_red_dark)
            )
        }
    }

    private fun addToCart() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "Debes iniciar sesión", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("users")
            .document(userId)
            .collection("cart")
            .document(product.id)
            .set(product)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Agregado al carrito", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al agregar al carrito", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
