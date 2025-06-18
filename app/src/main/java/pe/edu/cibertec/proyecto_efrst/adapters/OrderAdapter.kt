package pe.edu.cibertec.proyecto_efrst.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.edu.cibertec.proyecto_efrst.databinding.ItemOrderBinding
import pe.edu.cibertec.proyecto_efrst.models.Order

class OrderAdapter(
    private val orders: List<Order>,
    private val onOrderClick: (Order) -> Unit  // Nuevo parámetro para clic
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            binding.tvOrderId.text = "Pedido: ${order.id}"
            binding.tvOrderDate.text = "Fecha: ${order.date}"
            binding.tvOrderTotal.text = "Total: S/ %.2f".format(order.total)
            binding.tvItemCount.text = "Productos: ${order.items.size}"

            binding.root.setOnClickListener {
                onOrderClick(order)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size
}
