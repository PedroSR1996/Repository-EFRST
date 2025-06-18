package pe.edu.cibertec.proyecto_efrst.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import pe.edu.cibertec.proyecto_efrst.adapters.OrderAdapter
import pe.edu.cibertec.proyecto_efrst.databinding.FragmentOrdersBinding
import pe.edu.cibertec.proyecto_efrst.models.Order

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseDatabase.getInstance().reference
    private val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    private val orders = mutableListOf<Order>()
    private lateinit var adapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = OrderAdapter(orders) { selectedOrder ->
            // ✅ Navegar al detalle usando Safe Args
            val action = OrdersFragmentDirections
                .actionOrdersFragmentToOrderDetailFragment(selectedOrder)
            findNavController().navigate(action)
        }

        binding.rvOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrders.adapter = adapter

        loadOrders()
    }

    private fun loadOrders() {
        db.child("orders").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    orders.clear()
                    for (child in snapshot.children) {
                        val order = child.getValue(Order::class.java)
                        order?.let { orders.add(it) }
                    }
                    adapter.notifyDataSetChanged()
                    binding.tvEmptyOrders.visibility = if (orders.isEmpty()) View.VISIBLE else View.GONE
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Error al cargar órdenes", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
