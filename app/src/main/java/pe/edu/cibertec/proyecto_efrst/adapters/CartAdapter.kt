package pe.edu.cibertec.proyecto_efrst.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.edu.cibertec.proyecto_efrst.databinding.ItemCartProductBinding
import pe.edu.cibertec.proyecto_efrst.models.CartItem

class CartAdapter(
    private val cartItems: MutableList<CartItem>,
    private val onRemoveClick: (CartItem) -> Unit,
    private val onQuantityChange: (CartItem, Int) -> Unit   // Nuevo callback para cambios en cantidad
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(private val binding: ItemCartProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem) {
            binding.tvProductName.text = item.name
            binding.tvProductBrand.text = item.brand
            binding.tvProductPrice.text = "S/ %.2f".format(item.price)

            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .into(binding.imgProduct)

            // Mostrar cantidad actual
            binding.tvQuantity.text = item.quantity.toString()

            // Botones para modificar cantidad
            binding.btnIncrease.setOnClickListener {
                val newQty = item.quantity + 1
                onQuantityChange(item, newQty)
            }

            binding.btnDecrease.setOnClickListener {
                val newQty = if (item.quantity > 1) item.quantity - 1 else 1
                onQuantityChange(item, newQty)
            }

            // Botón eliminar producto
            binding.btnRemove.setOnClickListener {
                onRemoveClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size
}
