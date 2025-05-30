package pe.edu.cibertec.proyecto_efrst.home

import android.os.Bundle
import com.bumptech.glide.Glide
import androidx.appcompat.app.AppCompatActivity
import pe.edu.cibertec.proyecto_efrst.databinding.ActivityProductDetailBinding

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val price = intent.getDoubleExtra("price", 0.0)
        val description = intent.getStringExtra("description")
        val imageUrl = intent.getStringExtra("imageUrl")

        binding.tvProductNameDetail.text = name
        binding.tvProductPriceDetail.text = "S/ $price"
        binding.tvProductDescription.text = description

        Glide.with(this)
            .load(imageUrl)
            .into(binding.imgProductDetail)
    }
}
