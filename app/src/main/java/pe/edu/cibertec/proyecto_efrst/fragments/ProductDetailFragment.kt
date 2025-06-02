package pe.edu.cibertec.proyecto_efrst.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            addToCollection("cart")
        }

        binding.btnAddToFavorites.setOnClickListener {
            addToCollection("favorites")
        }
    }

    private fun addToCollection(collection: String) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "Debes iniciar sesión", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("users")
            .document(userId)
            .collection(collection)
            .document(product.id)
            .set(product)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Agregado a $collection", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al agregar", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
