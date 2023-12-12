package es.javiercarrasco.dt5recyclerview.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
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

    override fun getItemCount() = animalList.size

    // Asocia cada elemento de la lista con un ViewHolder
    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.bind(animalList[position])
    }
    ItemTouchHelper(object:
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) :Boolean {
                val fromPos= viewHolder.adapterPosition
                val toPos= target.adapterPosition
                Log.d("onMove", "Movieminto from$fromPos to $toPos")
                val myAdapter= recyclerView.adapter as AnimalAdapter
                myAdapter.notifyItemMoved(fromPos, toPos)
                    return true
            }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val listAnimal= mutableListOf<Animal>()
            val position=viewHolder.adapterPosition
            val itemDeleted=listAnimal[position]

            listAnimal.removeAt(position)
            val myAdapter= viewHolder.itemView as AnimalAdapter
            myAdapter.notifyItemRemoved(position)
            Snackbar.make(
                binding.root,
                "${itemDeleted.animalName} ha sido eliminado",
                Snackbar.LENGTH_LONG
            ).setAction("Deshacer"){
                listAnimal.add(position, itemDeleted)
                myAdapter.notifyItemInserted(position)
            }.show()
            }

        }).attachToRecyclerView(binding.mRecycler)
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