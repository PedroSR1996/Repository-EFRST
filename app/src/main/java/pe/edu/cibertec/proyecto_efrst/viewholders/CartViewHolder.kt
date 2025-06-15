package pe.edu.cibertec.proyecto_efrst.viewholders

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.edu.cibertec.proyecto_efrst.databinding.ItemCartProductBinding
import pe.edu.cibertec.proyecto_efrst.models.CartItem
import pe.edu.cibertec.proyecto_efrst.R

class CartViewHolder(
    private val binding: ItemCartProductBinding,
    private val onFavoriteToggle: ((CartItem) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cartItem: CartItem) {
        binding.tvProductName.text = cartItem.name
        binding.tvProductBrand.text = cartItem.brand
        binding.tvProductPrice.text = "S/ ${cartItem.price}"

        Glide.with(binding.root.context)
            .load(cartItem.imageUrl)
            .into(binding.imgProduct)

    }
}