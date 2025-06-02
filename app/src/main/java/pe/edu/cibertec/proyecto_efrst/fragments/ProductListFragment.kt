package pe.edu.cibertec.proyecto_efrst.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import pe.edu.cibertec.proyecto_efrst.adapters.ProductAdapter
import pe.edu.cibertec.proyecto_efrst.databinding.FragmentProductListBinding
import pe.edu.cibertec.proyecto_efrst.models.Product

class ProductListFragment : Fragment() {

    private lateinit var binding: FragmentProductListBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val products = mutableListOf<Product>()
    private lateinit var adapter: ProductAdapter

    private var categoria: String = "Todos"  // Argumento que recibimos

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener categoría desde argumentos
        categoria = arguments?.let {
            ProductListFragmentArgs.fromBundle(it).categoria
        } ?: "Todos"

        adapter = ProductAdapter(products,
            onItemClick = { product ->
                val action = ProductListFragmentDirections
                    .actionProductListFragmentToProductDetailFragment(product)
                findNavController().navigate(action)
            },
            onFavoriteToggled = null // No necesitas actualizar la lista aquí
        )


        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewProducts.adapter = adapter

        loadProducts()
    }
    
    private fun loadProducts() {
        firestore.collection("products")
            .get()
            .addOnSuccessListener { result ->
                products.clear()
                for (document in result) {
                    val product = document.toObject(Product::class.java)

                    // Filtrar según categoría
                    if (categoria == "Todos" || product.category.trim().equals(categoria.trim(), ignoreCase = true)) {
                        products.add(product)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar productos", Toast.LENGTH_SHORT).show()
            }
    }
}
