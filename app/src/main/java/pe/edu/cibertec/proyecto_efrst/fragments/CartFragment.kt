package pe.edu.cibertec.proyecto_efrst.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import pe.edu.cibertec.proyecto_efrst.adapters.CartAdapter
import pe.edu.cibertec.proyecto_efrst.databinding.FragmentCartBinding
import pe.edu.cibertec.proyecto_efrst.models.CartItem

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CartAdapter
    private val cartItems = mutableListOf<CartItem>()

    private val auth = FirebaseAuth.getInstance()
    private val dbRef = FirebaseDatabase.getInstance().reference

    private var cartListener: ValueEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CartAdapter(
            cartItems = cartItems,
            onRemoveClick = { item -> removeFromCart(item) },
            onQuantityChange = { item, newQty -> updateQuantity(item, newQty) }
        )

        binding.rvCartItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCartItems.adapter = adapter

        binding.btnCheckout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Confirmar pago")
                .setMessage("¿Estás seguro de realizar el pago?")
                .setPositiveButton("Sí") { _, _ ->
                    simulatePaymentWithSuccessDialog()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        loadCartItems()
    }

    private fun loadCartItems() {
        val userId = auth.currentUser?.uid ?: return
        val cartRef = dbRef.child("users").child(userId).child("cart")

        cartListener?.let { cartRef.removeEventListener(it) }

        cartListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cartItems.clear()
                for (child in snapshot.children) {
                    val item = child.getValue(CartItem::class.java)
                    if (item != null) {
                        item.id = child.key ?: ""
                        cartItems.add(item)
                    }
                }
                adapter.notifyDataSetChanged()
                updateTotal()
                updateEmptyState()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error al cargar carrito", Toast.LENGTH_SHORT).show()
            }
        }

        cartRef.addValueEventListener(cartListener as ValueEventListener)
    }

    private fun updateQuantity(item: CartItem, newQty: Int) {
        if (newQty < 1) return

        val userId = auth.currentUser?.uid ?: return
        val itemRef = dbRef.child("users").child(userId).child("cart").child(item.id)

        itemRef.child("quantity").setValue(newQty)
            .addOnSuccessListener {
                val index = cartItems.indexOfFirst { it.id == item.id }
                if (index != -1) {
                    cartItems[index].quantity = newQty
                    adapter.notifyItemChanged(index)
                    updateTotal()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al actualizar cantidad", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeFromCart(item: CartItem) {
        val userId = auth.currentUser?.uid ?: return
        val itemRef = dbRef.child("users").child(userId).child("cart").child(item.id)

        itemRef.removeValue()
            .addOnSuccessListener {
                val index = cartItems.indexOfFirst { it.id == item.id }
                if (index != -1) {
                    cartItems.removeAt(index)
                    adapter.notifyItemRemoved(index)
                    updateTotal()
                    updateEmptyState()
                    Toast.makeText(requireContext(), "Producto eliminado del carrito", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al eliminar del carrito", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateTotal() {
        val total = cartItems.sumOf { it.price * it.quantity }
        binding.tvTotal.text = "Total: S/ %.2f".format(total)
    }

    private fun updateEmptyState() {
        val isEmpty = cartItems.isEmpty()
        binding.tvEmptyCart.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.tvTotal.visibility = if (isEmpty) View.GONE else View.VISIBLE
        binding.btnCheckout.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun simulatePaymentWithSuccessDialog() {
        val userId = auth.currentUser?.uid ?: return
        val cartRef = dbRef.child("users").child(userId).child("cart")

        if (cartItems.isEmpty()) {
            Toast.makeText(requireContext(), "No hay productos para pagar", Toast.LENGTH_SHORT).show()
            return
        }

        val itemsMap = cartItems.associateBy { it.productId }
        val orderId = dbRef.child("orders").child(userId).push().key ?: return
        val orderTotal = cartItems.sumOf { it.price * it.quantity }
        val orderDate = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date())

        val order = mapOf(
            "id" to orderId,
            "date" to orderDate,
            "total" to orderTotal,
            "items" to itemsMap
        )

        dbRef.child("orders").child(userId).child(orderId).setValue(order)
            .addOnSuccessListener {
                // Actualiza stock
                val productsRef = dbRef.child("products")
                for ((_, item) in itemsMap) {
                    productsRef.orderByChild("id").equalTo(item.productId)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (productSnap in snapshot.children) {
                                    val stockRef = productSnap.ref.child("stock")
                                    stockRef.runTransaction(object : Transaction.Handler {
                                        override fun doTransaction(currentData: MutableData): Transaction.Result {
                                            val currentStock = currentData.getValue(Int::class.java) ?: return Transaction.success(currentData)
                                            if (currentStock >= item.quantity) {
                                                currentData.value = currentStock - item.quantity
                                            }
                                            return Transaction.success(currentData)
                                        }

                                        override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {}
                                    })
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                }

                // Limpiar carrito
                cartRef.removeValue()
                cartItems.clear()
                adapter.notifyDataSetChanged()
                updateEmptyState()
                updateTotal()

                // Mostrar diálogo de éxito
                AlertDialog.Builder(requireContext())
                    .setTitle("Pago realizado")
                    .setMessage("¡Tu compra se ha realizado con éxito!")
                    .setPositiveButton("OK", null)
                    .show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al registrar pedido", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        val userId = auth.currentUser?.uid
        if (userId != null && cartListener != null) {
            dbRef.child("users").child(userId).child("cart")
                .removeEventListener(cartListener as ValueEventListener)
        }
        _binding = null
    }
}
