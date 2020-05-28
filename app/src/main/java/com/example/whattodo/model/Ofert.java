package com.example.whattodo.model;

public class Ofert {

    private String photo, title, event, localitzacio, validesa;
    private boolean adquirit;

    public Ofert(){}

    public Ofert(String photo, String title, String event, String localitzacio, String validesa, boolean adquirit){
        this.photo = photo;
        this.title = title;
        this.event = event;
        this.localitzacio = localitzacio;
        this.validesa = validesa;
        this.adquirit = adquirit;
    }

    public String getPhoto(){
        return photo;
    }

    public String getTitle() {
        return title;
    }

    public String getEvent() {
        return event;
    }

    public String getLocalitzacio() {
        return localitzacio;
    }

    public String getValidesa() {
        return validesa;
    }

    public boolean getAdquirit() {
        return adquirit;
    }

    public void setAdquirit(boolean adquirit){
        this.adquirit = adquirit;
    }
}
