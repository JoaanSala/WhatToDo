package com.example.whattodo;

import java.util.ArrayList;

public class EntryObject_DB {

    public ArrayList<EntryObject> entryobject_db;

    public void createDataBase (){

        entryobject_db = new ArrayList<>();
    }

    public ArrayList<EntryObject> getDataBase(){
        return entryobject_db;
    }


    public void addAllToDataBase(){
        addRestaurantsToDataBase();
        addMonumentsToDataBase();
        addOciToDataBase();
        addNightToDataBase();
    }

    public void addRestaurantsToDataBase (){
        entryobject_db.add(new EntryObject(R.drawable.r_piemontesa, "Restaurant la Piemontesa", "Lleida", "973 30 22 94", "Per afegir...", "Av. de Balmes, 7", "Lleida", 41.616726, 0.620097, "8"));
        entryobject_db.add(new EntryObject(R.drawable.r_xirriclo, "Restaurant Cal Xirricló", "Balaguer", "973 44 50 11" , "Per afegir...","C/ del Dr. Fleming, 53", "Balaguer", 41.788311, 0.809838,"4.9"));
        entryobject_db.add(new EntryObject(R.drawable.bar973, "973 Bar", "Castellserà", "628 92 36 86", "Per afegir...", "C/ Carretera de Penelles, 5", "Castellsera", 41.748509, 0.984632, "6.5"));
    }

    public void addMonumentsToDataBase (){
        entryobject_db.add(new EntryObject(R.drawable.torre_agbar, "Torre Agbar", "Barcelona", "-", "Per afegir...", "Avinguda Diagonal, 211", "Barcelona", 41.403542, 2.189561, "8"));
        entryobject_db.add(new EntryObject(R.drawable.la_seu_vella, "La Seu Vella", "Lleida", "973 23 06 53", "Per afegir...", "Plaça Guifre I / Seu Vella", "Lleida", 41.618107, 0.626879,"8.9"));
        entryobject_db.add(new EntryObject(R.drawable.circ_roma, "Circ Romà Tarragona", "Tarragona", "977 24 22 20", "Per afegir...", "Rambla Vella, 2", "Tarragona" , 41.116132, 1.257007,"10"));

    }

    public void addOciToDataBase (){
        entryobject_db.add(new EntryObject(R.drawable.canet_rock, "Canet Rock", "Canet de Mar", "-", "Preparat per assistir a un dels events de l'any?? Vine al Canet Rock 2020, el 4 de juliol", "Carrer Via Cannetum, 16", "Canet de Mar", 41.593560, 2.568184,"7.5"));
        entryobject_db.add(new EntryObject(R.drawable.barca_vs_madrid, "F.C Barcelona vs Real Madrid", "Camp Nou", "-", "Per afegir...", "C. d'Arístides Maillol, 12", "Barcelona", 41.380902, 2.122820,"9"));
        entryobject_db.add(new EntryObject(R.drawable.bohemian_rhapsody, "Pel·lícula Bohemian Rhapsody", "Cinemes Alpicat", "973 70 63 50", "Per afegir...", "Sortida 458 A2-Autovía del Nordeste", "Alpicat", 41.649971, 0.567829, "7"));

    }

    public void addNightToDataBase (){
        entryobject_db.add(new EntryObject(R.drawable.el_row, "El Row", "Viladecans", "932 06 30 34", "Per afegir...", "C-31, Km 186,1", "Viladecans",  41.274917, 2.046051, "6"));
        entryobject_db.add(new EntryObject(R.drawable.biloba_lleida, "Biloba Lleida", "Lleida", "679 37 53 13", "Per afegir...", "Carrer Ivars d'Urgell, S/N", "Lleida", 41.615961, 0.658045, "8.2"));
        entryobject_db.add(new EntryObject(R.drawable.pub_vins, "Zona dels Vins", "Lleida", "-", "Per afegir...", "C/ Bonaire, 14-22", "Lleida", 41.618887,  0.621228,"7.4"));

    }

    public int getDataBaseSize(){
        return entryobject_db.size();
    }

}
