package es.davidc.demoexistencias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import es.davidc.demoexistencias.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mainViewModel=MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        savedInstanceState?.run {
            binding.textView.text=it.getString("texto")
        }

        mainViewModel=ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.textoEtiqueta= binding.textView.text.toString()
        binding.button.setOnClickListener {
            binding.textView.append("${binding.textView2.text}\n")
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("texto", binding.textView.text.toString())
        super.onSaveInstanceState(outState)

    }
}