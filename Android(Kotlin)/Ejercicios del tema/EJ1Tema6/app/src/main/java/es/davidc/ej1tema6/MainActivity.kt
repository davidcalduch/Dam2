package es.davidc.ej1tema6

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.davidc.ej1tema6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.button.setOnClickListener {
           val myIntent= Intent(
               this,
               Conduccion::class.java
           )
            startActivity(myIntent)
        }
        binding.button2.setOnClickListener {
            val myIntent= Intent(
                this,
                Conduccion::class.java
            )
            startActivity(myIntent)
        }
        binding.button3.setOnClickListener {
            val myIntent= Intent(
                this,
                Conduccion::class.java
            )
            startActivity(myIntent)
        }
       /* starActivity(
            Intent(
                this,
                Conduccion::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP ),
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        )
*/
    }
}