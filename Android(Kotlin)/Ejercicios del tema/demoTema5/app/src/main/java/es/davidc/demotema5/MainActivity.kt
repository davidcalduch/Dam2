package es.davidc.demotema5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import es.davidc.demotema5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var demotema5: EditText
    private val datos= arrayOf(
        "uno",
        "dos",
        "tres",
        "cuatro",
        "cinco",
        "seis",
        "siete"
    )
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         binding = ActivityMainBinding.inflate(layoutInflater)
        val adapter= ArrayAdapter(
            this, android.R.layout.simple_list_item_1, datos)
        binding.miListView.adapter=adapter
    }

    override fun onStart() {
        super.onStart()
        binding.miListView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this,
                "Cuenta: ${parent.count}\nHa pulsado el elemento$datos[position]}",
                Toast.LENGTH_SHORT).show()

        }
    }
}