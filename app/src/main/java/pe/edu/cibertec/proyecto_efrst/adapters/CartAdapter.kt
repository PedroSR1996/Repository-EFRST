package pe.edu.cibertec.proyecto_efrst.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.edu.cibertec.proyecto_efrst.databinding.ItemCartProductBinding
import pe.edu.cibertec.proyecto_efrst.models.CartItem

class CartAdapter(
    private val cartItems: MutableList<CartItem>,
    private val onRemoveClick: ((CartItem) -> Unit)? = null,
    private val onQuantityChange: ((CartItem, Int) -> Unit)? = null,
    private val readOnly: Boolean = false  // <- Agregado para modo solo lectura
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

            binding.tvQuantity.text = item.quantity.toString()

            if (readOnly) {
                // Ocultar botones en modo solo lectura
                binding.btnIncrease.visibility = View.GONE
                binding.btnDecrease.visibility = View.GONE
                binding.btnRemove.visibility = View.GONE
            } else {
                binding.btnIncrease.setOnClickListener {
                    val newQty = item.quantity + 1
                    if (newQty <= item.stock) {
                        onQuantityChange?.invoke(item, newQty)
                    } else {
                        Toast.makeText(binding.root.context, "Stock insuficiente", Toast.LENGTH_SHORT).show()
                    }
                }

                binding.btnDecrease.setOnClickListener {
                    val newQty = if (item.quantity > 1) item.quantity - 1 else 1
                    onQuantityChange?.invoke(item, newQty)
                }

                binding.btnRemove.setOnClickListener {
                    onRemoveClick?.invoke(item)
                }
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
