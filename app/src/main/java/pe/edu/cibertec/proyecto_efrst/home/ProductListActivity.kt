package pe.edu.cibertec.proyecto_efrst.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import pe.edu.cibertec.proyecto_efrst.adapters.ProductAdapter
import pe.edu.cibertec.proyecto_efrst.databinding.ActivityProductListBinding
import pe.edu.cibertec.proyecto_efrst.models.Product

class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    private lateinit var adapter: ProductAdapter
    private val productList = mutableListOf<Product>()
    private var categoriaSeleccionada: String = "Todos"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recibir la categoría desde el intent (puede ser "Todos" u otra)
        categoriaSeleccionada = intent.getStringExtra("categoria") ?: "Todos"

        setupRecyclerView()
        loadProducts()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(productList)
        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProducts.adapter = adapter
    }

    private fun loadProducts() {
        FirebaseFirestore.getInstance().collection("products")
            .get()
            .addOnSuccessListener { documents ->
                productList.clear()
                for (doc in documents) {
                    val product = doc.toObject(Product::class.java)

                    // Filtrar por categoría seleccionada
                    if (categoriaSeleccionada == "Todos" || product.category == categoriaSeleccionada) {
                        productList.add(product)
                    }
                }

                if (productList.isEmpty()) {
                    Toast.makeText(this, "No hay productos en esta categoría", Toast.LENGTH_SHORT).show()
                }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar productos", Toast.LENGTH_SHORT).show()
            }
    }
}
