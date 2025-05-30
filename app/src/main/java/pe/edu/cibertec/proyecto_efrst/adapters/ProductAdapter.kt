package pe.edu.cibertec.proyecto_efrst.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pe.edu.cibertec.proyecto_efrst.R
import pe.edu.cibertec.proyecto_efrst.databinding.ItemProductBinding
import pe.edu.cibertec.proyecto_efrst.home.ProductDetailActivity
import pe.edu.cibertec.proyecto_efrst.models.Product
class ProductAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.tvProductName.text = product.name
            binding.tvProductBrand.text = product.brand
            binding.tvProductPrice.text = "S/ ${product.price}"
            binding.tvProductStock.text = "Stock: ${product.stock}"

            Glide.with(binding.root.context)
                .load(product.imageUrl)
                .into(binding.imgProduct)

            val favRef = firestore.collection("users")
                .document(userId)
                .collection("favorites")
                .document(product.id)

            // Mostrar estado inicial
            favRef.get().addOnSuccessListener { snapshot ->
                val isFavorite = snapshot.exists()
                updateFavoriteIcon(isFavorite)
            }

            // Al hacer clic en el botón de favorito
            binding.btnFavorite.setOnClickListener {
                favRef.get().addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        // Quitar de favoritos
                        favRef.delete()
                        updateFavoriteIcon(false)
                        Toast.makeText(binding.root.context, "Eliminado de favoritos", Toast.LENGTH_SHORT).show()
                    } else {
                        // Agregar a favoritos
                        favRef.set(product)
                        updateFavoriteIcon(true)
                        Toast.makeText(binding.root.context, "Agregado a favoritos", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // Ir al detalle del producto
            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, ProductDetailActivity::class.java).apply {
                    putExtra("name", product.name)
                    putExtra("brand", product.brand)
                    putExtra("price", product.price)
                    putExtra("stock", product.stock)
                    putExtra("description", product.description)
                    putExtra("imageUrl", product.imageUrl)
                }
                context.startActivity(intent)
            }
        }

        private fun updateFavoriteIcon(isFavorite: Boolean) {
            binding.btnFavorite.setImageResource(
                if (isFavorite) R.drawable.ic_favorite_filled
                else R.drawable.ic_favorite_border
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size
}
