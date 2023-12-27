package com.example.imagenscalerfx2.utils;
//informa sobre Nombre del archivo de imagen: una cadena que contiene el nombre del archivo de la imagen, como imagen.jpg, para
//instancia.
public class ImagenData {
    private String imagenName;
    private String imagenPath;

    public String getImagenName() {
        return imagenName;
    }

    public void setImagenName(String imagenName) {
        this.imagenName = imagenName;
    }

    public String getImagenPath() {
        return imagenPath;
    }

    public void setImagenPath(String imagenPath) {
        this.imagenPath = imagenPath;
    }

    @Override
    public String toString() {
        return imagenName;
    }
}
