package es.davidc.ej2tema6

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.davidc.ej2tema6.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            val myIntent = Intent(
                this,
                MainActivity2::class.java
            )
            myIntent.putExtra("nombre", binding.MandarInformacion.text.toString())
            startActivity(myIntent)
        }
    }
}