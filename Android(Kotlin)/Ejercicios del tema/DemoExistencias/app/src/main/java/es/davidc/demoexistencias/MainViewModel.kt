package es.davidc.demoexistencias

import androidx.lifecycle.ViewModel

class MainViewModel:ViewModel (){
    var textoEtiqueta: String? =null

    val textEtiqueta:String?
        get() = textoEtiqueta

    fun steTextEtiqueta(texto:String){
        textoEtiqueta=texto
    }
}