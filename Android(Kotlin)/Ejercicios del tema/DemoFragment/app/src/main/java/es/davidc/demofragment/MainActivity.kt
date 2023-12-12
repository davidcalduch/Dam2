package es.davidc.demofragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.davidc.demofragment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val transaction= supportFragmentManager.beginTransaction()
        val fragmentOne=FragmentOne()
        transaction.replace(binding.mrFrame.id,fragmentOne)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    override fun onStart() {
        super.onStart()
        binding.button.setOnClickListener{
            val transaction= supportFragmentManager.beginTransaction()
            val fragmentTwo=FragmentOne()
            transaction.replace(binding.mrFrame.id,fragmentTwo)
            transaction.addToBackStack(null)
            transaction.commit()

        }
    }
}