package pe.edu.cibertec.proyecto_efrst.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import pe.edu.cibertec.proyecto_efrst.databinding.ActivityProductDetailBinding

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obteniendo datos del intent
        val name = intent.getStringExtra("name")
        val brand = intent.getStringExtra("brand")
        val price = intent.getDoubleExtra("price", 0.0)
        val description = intent.getStringExtra("description")
        val imageUrl = intent.getStringExtra("imageUrl")

        // Asignando los datos a los TextViews
        binding.tvProductNameDetail.text = name
        binding.tvProductBrandDetail.text = brand
        binding.tvProductPriceDetail.text = "S/ $price"
        binding.tvProductDescription.text = description

        // Cargando imagen
        Glide.with(this)
            .load(imageUrl)
            .into(binding.imgProductDetail)
    }
}
