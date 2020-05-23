package com.example.whattodo.model;

public class Coment{

    private String comentari;
    private int puntuacio;
    private String userName;

    public Coment() {}

    public Coment(String userName, String comentari, int puntuacio){
        this.userName = userName;
        this.comentari = comentari;
        this.puntuacio = puntuacio;
    }


    public String getUserName(){ return userName; }

    public String getComentari(){ return comentari; }

    public int getPuntuacio(){ return puntuacio; }

}
