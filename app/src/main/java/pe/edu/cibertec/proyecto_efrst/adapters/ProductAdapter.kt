package pe.edu.cibertec.proyecto_efrst.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.edu.cibertec.proyecto_efrst.databinding.ItemProductBinding
import pe.edu.cibertec.proyecto_efrst.models.Product
import pe.edu.cibertec.proyecto_efrst.viewholders.ProductViewHolder

class ProductAdapter(
    private val products: MutableList<Product>,
    private val onItemClick: (Product) -> Unit,
    private val onFavoriteToggled: (() -> Unit)? = null,
    private val onRemoveClick: ((Product) -> Unit)? = null
) : RecyclerView.Adapter<ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding, onItemClick, onFavoriteToggled, onRemoveClick)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size
}
