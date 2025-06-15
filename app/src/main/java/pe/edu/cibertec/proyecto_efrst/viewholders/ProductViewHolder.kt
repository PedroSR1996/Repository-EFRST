package pe.edu.cibertec.proyecto_efrst.viewholders

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.edu.cibertec.proyecto_efrst.R
import pe.edu.cibertec.proyecto_efrst.databinding.ItemProductBinding
import pe.edu.cibertec.proyecto_efrst.models.Product
import pe.edu.cibertec.proyecto_efrst.utils.FavoriteManager

class ProductViewHolder (
    private val binding: ItemProductBinding,
    private val onItemClick: (Product) -> Unit,
    private val onFavoriteToggled: (() -> Unit)? = null,
    private val onRemoveClick: ((Product) -> Unit)? = null
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.tvProductName.text = product.name
            binding.tvProductBrand.text = product.brand
            binding.tvProductPrice.text = "S/ ${product.price}"
            binding.tvProductStock.text = "Stock: ${product.stock}"

            Glide.with(binding.root.context)
                .load(product.imageUrl)
                .into(binding.imgProduct)

            FavoriteManager.isFavorite(product.id) { isFav ->
                updateFavoriteIcon(isFav)
            }

            binding.btnFavorite.setOnClickListener {
                FavoriteManager.toggleFavorite(product) { isFav ->
                    updateFavoriteIcon(isFav)
                    val msg = if (isFav) "Agregado a favoritos" else "Eliminado de favoritos"
                    Toast.makeText(binding.root.context, msg, Toast.LENGTH_SHORT).show()
                    onFavoriteToggled?.invoke()
                }
            }

            binding.root.setOnClickListener {
                onItemClick(product)
            }

            if (onRemoveClick != null) {
                binding.btnRemove.visibility = View.VISIBLE
                binding.btnRemove.setOnClickListener {
                    onRemoveClick.invoke(product)
                }
            } else {
                binding.btnRemove.visibility = View.GONE
            }
        }

        private fun updateFavoriteIcon(isFavorite: Boolean) {
            binding.btnFavorite.setImageResource(
                if (isFavorite) R.drawable.ic_favorite_filled
                else R.drawable.ic_favorite_border
            )
        }
    }