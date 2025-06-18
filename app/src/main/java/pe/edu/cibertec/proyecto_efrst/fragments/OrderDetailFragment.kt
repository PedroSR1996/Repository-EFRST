package pe.edu.cibertec.proyecto_efrst.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import pe.edu.cibertec.proyecto_efrst.adapters.CartAdapter
import pe.edu.cibertec.proyecto_efrst.databinding.FragmentOrderDetailBinding
import pe.edu.cibertec.proyecto_efrst.models.CartItem
import pe.edu.cibertec.proyecto_efrst.models.Order

class OrderDetailFragment : Fragment() {

    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var order: Order
    private lateinit var adapter: CartAdapter
    private val items = mutableListOf<CartItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        order = OrderDetailFragmentArgs.fromBundle(requireArguments()).order

        binding.tvOrderId.text = "Pedido: ${order.id}"
        binding.tvOrderDate.text = "Fecha: ${order.date}"
        binding.tvOrderTotal.text = "Total: S/ %.2f".format(order.total)

        items.addAll(order.items.values)

        adapter = CartAdapter(
            cartItems = items,
            onRemoveClick = {},       // No se pueden eliminar en vista detalle
            onQuantityChange = { _, _ -> } // No se modifica cantidad en vista detalle
        )

        binding.rvOrderItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrderItems.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
