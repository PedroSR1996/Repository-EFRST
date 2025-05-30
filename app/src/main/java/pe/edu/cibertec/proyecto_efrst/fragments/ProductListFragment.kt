package pe.edu.cibertec.proyecto_efrst.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import pe.edu.cibertec.proyecto_efrst.adapters.ProductAdapter
import pe.edu.cibertec.proyecto_efrst.databinding.FragmentProductListBinding
import pe.edu.cibertec.proyecto_efrst.models.Product

class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ProductAdapter
    private val productList = mutableListOf<Product>()
    private var categoriaSeleccionada: String = "Todos"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener categoría desde Safe Args (los argumentos)
        arguments?.let {
            categoriaSeleccionada = ProductListFragmentArgs.fromBundle(it).categoria
        }

        setupRecyclerView()
        loadProducts()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(productList)
        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewProducts.adapter = adapter
    }

    private fun loadProducts() {
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("products")

        collectionRef.get()
            .addOnSuccessListener { documents ->
                productList.clear()
                for (document in documents) {
                    val product = document.toObject(Product::class.java)
                    // Filtrar por categoría si no es "Todos"
                    if (categoriaSeleccionada == "Todos" || product.category == categoriaSeleccionada) {
                        productList.add(product)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error al cargar productos: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
