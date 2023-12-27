import com.sun.tools.javac.Main

//Crea una clase libro (autor, título,
//año), con setters que dejen los valores
//por defecto “Anónimo”, “No
//indicado” y -1. Crea también un
//método que devuelva los tres datos
//en una misma línea, comenzando por
//el título, separados por espacios y
//guiones, como en “It – Stephen King
//– 1986
class libro
{
    private var autor:String = "Anónimo"
    private var titulo:String= "No indicado"
    private var anyo:Int = -1
    fun setAutor(autor:String)
    {
        this.autor = autor
    }
    fun setTitulo(titulo:String)
    {
        this.titulo = titulo
    }
    fun setAnyo(anyo:Int)
    {
        this.anyo = anyo
    }
    fun getLibro():String
    {
        return "$titulo - $autor - $anyo"
    }
    fun ObtenerDatos():String
    {
        return "Autor: $autor\nTitulo: $titulo\nAño: $anyo"
    }
}
fun Main()
{
    val libro1 = libro()
    libro1.setAutor("Stephen King")
    libro1.setTitulo("It")
    libro1.setAnyo(1986)
    println(libro1.getLibro())
    println(libro1.ObtenerDatos())
}
