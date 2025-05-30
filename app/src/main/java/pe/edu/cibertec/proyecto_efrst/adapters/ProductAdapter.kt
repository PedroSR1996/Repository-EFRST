package pe.edu.cibertec.proyecto_efrst.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.edu.cibertec.proyecto_efrst.databinding.ItemProductBinding
import pe.edu.cibertec.proyecto_efrst.home.ProductDetailActivity
import pe.edu.cibertec.proyecto_efrst.models.Product

class ProductAdapter(private val products: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.tvProductName.text = product.name
            binding.tvProductBrand.text = product.brand
            binding.tvProductPrice.text = "S/ ${product.price}"

            Glide.with(binding.root.context)
                .load(product.imageUrl)
                .into(binding.imgProduct)

            // 🚀 Abrir ProductDetailActivity al hacer clic
            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, ProductDetailActivity::class.java).apply {
                    putExtra("name", product.name)
                    putExtra("brand", product.brand)
                    putExtra("price", product.price)
                    putExtra("description", product.description)
                    putExtra("imageUrl", product.imageUrl)
                }
                context.startActivity(intent)
            }
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
