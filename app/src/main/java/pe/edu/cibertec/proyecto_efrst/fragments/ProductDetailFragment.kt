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
import com.google.firebase.database.*
import pe.edu.cibertec.proyecto_efrst.databinding.FragmentProductDetailBinding
import pe.edu.cibertec.proyecto_efrst.models.CartItem
import pe.edu.cibertec.proyecto_efrst.models.Product

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var product: Product
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

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

        // Desactivar botón si no hay stock
        if (product.stock <= 0) {
            binding.btnAddToCart.isEnabled = false
            binding.btnAddToCart.text = "Sin stock"
        }

        checkIfFavorite()
    }

    private fun checkIfFavorite() {
        val userId = auth.currentUser?.uid ?: return

        val favRef = db.child("users").child(userId).child("favorites").child(product.id)

        favRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                isFavorite = snapshot.exists()
                updateFavoriteButton()
                binding.btnAddToFavorites.visibility = View.VISIBLE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error al verificar favorito", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun toggleFavorite() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "Debes iniciar sesión", Toast.LENGTH_SHORT).show()
            return
        }

        val favRef = db.child("users").child(userId).child("favorites").child(product.id)

        if (isFavorite) {
            favRef.removeValue()
                .addOnSuccessListener {
                    isFavorite = false
                    updateFavoriteButton()
                    Toast.makeText(requireContext(), "Eliminado de favoritos", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error al quitar de favoritos", Toast.LENGTH_SHORT).show()
                }
        } else {
            favRef.setValue(product)
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

        if (product.stock < 1) {
            Toast.makeText(requireContext(), "No hay stock disponible", Toast.LENGTH_SHORT).show()
            return
        }

        val cartRef = db.child("users").child(userId).child("cart").child(product.id)

        cartRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val currentQuantity = snapshot.child("quantity").getValue(Int::class.java) ?: 1
                    val newQuantity = currentQuantity + 1

                    if (newQuantity > product.stock) {
                        Toast.makeText(requireContext(), "No hay suficiente stock", Toast.LENGTH_SHORT).show()
                        return
                    }

                    cartRef.child("quantity").setValue(newQuantity)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Cantidad actualizada en el carrito", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Error al actualizar carrito", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    val cartItem = CartItem(
                        id = product.id,
                        productId = product.id,
                        name = product.name,
                        brand = product.brand,
                        price = product.price,
                        imageUrl = product.imageUrl,
                        quantity = 1
                    )
                    cartRef.setValue(cartItem)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Agregado al carrito", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Error al agregar al carrito", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error al acceder al carrito", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
