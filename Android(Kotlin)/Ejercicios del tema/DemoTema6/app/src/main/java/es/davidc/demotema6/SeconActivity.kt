package es.davidc.demotema6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.davidc.demotema6.databinding.ActivitySeconBinding

class SeconActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeconBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secon)

        val name= intent.getStringExtra(MainActivity.EXTRA_MESSAGE)
        binding.Second.text = name

    }

    // override fun onBackPressed() {
    //    super.onBackPressed()
    //}

}