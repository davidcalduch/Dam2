package es.davidc.ej2tema6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.davidc.ej2tema6.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val message = intent.getStringExtra( "nombre").apply{ binding.informacionRecibida.text = this }
    }
}