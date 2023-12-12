package es.javiercarrasco.dt5recyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import es.javiercarrasco.dt5recyclerview.adapters.AnimalAdapter
import es.javiercarrasco.dt5recyclerview.databinding.ActivityMainBinding
import es.javiercarrasco.dt5recyclerview.model.Animal

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var animalAdapter: AnimalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animalAdapter = AnimalAdapter(getAnimals())
        binding.mRecycler.setHasFixedSize(true)
        binding.mRecycler.adapter = animalAdapter
    }

    // Método encargado de generar la fuente de datos.
    private fun getAnimals(): MutableList<Animal> {
        val animals: MutableList<Animal> = arrayListOf()

        animals.add(Animal("Cisne", "Cygnus olor", R.drawable.cisne))
        animals.add(Animal("Erizo", "Erinaceinae", R.drawable.erizo))
        animals.add(Animal("Gato", "Felis catus", R.drawable.gato))
        animals.add(Animal("Gorrión", "Passer domesticus", R.drawable.gorrion))
        animals.add(Animal("Mapache", "Procyon", R.drawable.mapache))
        animals.add(Animal("Oveja", "Ovis aries", R.drawable.oveja))
        animals.add(Animal("Perro", "Canis lupus familiaris", R.drawable.perro))
        animals.add(Animal("Tigre", "Panthera tigris", R.drawable.tigre))
        animals.add(Animal("Zorro", "Vulpes vulpes", R.drawable.zorro))

        return animals
    }
}