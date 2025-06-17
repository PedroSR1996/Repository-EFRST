package pe.edu.cibertec.proyecto_efrst.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.edu.cibertec.proyecto_efrst.databinding.ItemCarouselImageBinding

class CarouselAdapter(private val imageUrls: List<String>) :
    RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    inner class CarouselViewHolder(val binding: ItemCarouselImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = ItemCarouselImageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val url = imageUrls[position % imageUrls.size]
        Glide.with(holder.binding.root.context)
            .load(url)
            .into(holder.binding.imageViewCarousel)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}
