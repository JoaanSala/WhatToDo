package com.example.whattodo.model;

public class Event {

    private String Titol, Localitzacio, Telefon, Detalls, Adreça, Ciutat, Qualificacio, URL;
    private Double Latitud, Longitud;

    public Event(){}

    public Event(String titol, String localitzacio, String telefon, String detalls, String adreça, String ciutat, Double latitud, Double longitud, String qualificacio, String url){
        this.Titol = titol;
        this.Localitzacio = localitzacio;
        this.Telefon = telefon;
        this.Detalls = detalls;
        this.Adreça = adreça;
        this.Ciutat = ciutat;
        this.Latitud = latitud;
        this.Longitud = longitud;
        this.Qualificacio = qualificacio;
        this.URL = url;
    }

    public String getTitol(){
        return Titol;
    }

    public String getLocalitzacio(){
        return Localitzacio;
    }

    public String getTelefon(){
        return Telefon;
    }

    public String getDetalls(){
        return Detalls;
    }

    public String getAdreça(){
        return Adreça;
    }

    public String getCiutat() { return Ciutat; }

    public Double getLatitud(){
        return Latitud;
    }

    public Double getLongitud() { return Longitud; }

    public String getQualificacio(){
        return Qualificacio;
    }

    public String getURL(){return URL;}

}
