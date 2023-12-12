package es.javiercarrasco.dt5recyclerview.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar
import es.javiercarrasco.dt5recyclerview.databinding.AnimalItemBinding
import es.javiercarrasco.dt5recyclerview.model.Animal

class AnimalAdapter(val animalList: MutableList<Animal>) :
    RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        return AnimalViewHolder(
            AnimalItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    // Devuelve el número de elementos de la lista
    override fun getItemCount() = animalList.size

    // Asocia cada elemento de la lista con un ViewHolder
    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.bind(animalList[position])
    }

    class AnimalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val bind = AnimalItemBinding.bind(view)

        fun bind(animal: Animal) {
            bind.tvNombre.text = animal.animalName
            bind.tvLatinName.text = animal.latinName

            Glide.with(itemView)
                .load(animal.imageAnimal)
                .transform(CenterCrop(), RoundedCorners(20))
                .into(bind.ivAnimal)

            itemView.setOnClickListener {
                Snackbar.make(
                    itemView,
                    "Has pulsado sobre ${animal.animalName}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}