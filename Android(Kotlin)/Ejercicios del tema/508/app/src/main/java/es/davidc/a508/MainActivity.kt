package es.davidc.a508

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private val asignaturas= mutableListOf("Matemáticas","Lengua","Inglés","Física","Química","Historia","Geografía","Filosofía")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         val textField: TextView = view.findViewById(android.R.id.text1)
         }
}