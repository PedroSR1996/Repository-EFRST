package pe.edu.cibertec.proyecto_efrst.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import pe.edu.cibertec.proyecto_efrst.adapters.ProductAdapter
import pe.edu.cibertec.proyecto_efrst.databinding.FragmentProductListBinding
import pe.edu.cibertec.proyecto_efrst.models.Product

class ProductListFragment : Fragment() {

    private lateinit var binding: FragmentProductListBinding
    private val db = FirebaseDatabase.getInstance().reference
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

        // Mostrar título según categoría
        binding.tvCategoryTitle.text = when (categoria) {
            "Todos" -> "Todos los productos"
            "Comida para peces" -> "Comida para Peces"
            "Iluminacion" -> "Iluminación"
            "Filtros" -> "Filtros para Acuarios"
            "Acuarios" -> "Acuarios de Vidrio"
            "Plantas" -> "Plantas Acuáticas"
            else -> categoria
        }

        adapter = ProductAdapter(
            products,
            onItemClick = { product ->
                val action = ProductListFragmentDirections
                    .actionProductListFragmentToProductDetailFragment(product)
                findNavController().navigate(action)
            },
            onFavoriteToggled = null // O actualiza si usas favoritos
        )

        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewProducts.adapter = adapter

        loadProducts()
    }

    private fun loadProducts() {
        db.child("products").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                products.clear()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    product?.let {
                        if (categoria == "Todos" || it.category.trim().equals(categoria.trim(), ignoreCase = true)) {
                            products.add(it)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error al cargar productos", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
