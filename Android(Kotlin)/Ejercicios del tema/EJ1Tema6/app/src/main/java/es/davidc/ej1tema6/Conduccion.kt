package es.davidc.ej1tema6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class Conduccion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conduccion)
    }
    override fun onBackPressed() {
        Toast.makeText(this, "No puedes volver atras", Toast.LENGTH_SHORT).show()
        //super.onBackPressed()
    }
}