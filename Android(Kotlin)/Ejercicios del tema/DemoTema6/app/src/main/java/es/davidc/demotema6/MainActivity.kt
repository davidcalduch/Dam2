package es.davidc.demotema6

import android.app.Instrumentation
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import es.davidc.demotema6.databinding.ActivityMainBinding
import es.davidc.demotema6.databinding.ActivitySeconBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeconBinding
    companion object
    {
        const val EXTRA_MESSAGE = "MESSAGE"
        var resultadoActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            if(result,resutlCode == RESULT_OK)
            {
                val data: Intent? = result?.data
                val message = data?.getStringExtra("MESSAGE")
                binding.Second.text = message
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding.floatButton.SetOnclikListener{
            val intent = Intent(this, SeconActivity::class.java)
            intent.putExtra(EXTRA_MESSAGE, "Hola desde el MainActivity")
        }
    }
}