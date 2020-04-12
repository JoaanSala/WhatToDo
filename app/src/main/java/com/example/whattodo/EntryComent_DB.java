package com.example.whattodo;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EntryComent_DB{

    public ArrayList<EntryComent> entryobject_db;

    public void createDB(){
        entryobject_db = new ArrayList<>();
    }


    public void addNewComent(String name, String comentari, int puntuacio){
        entryobject_db.add(new EntryComent(name, comentari, puntuacio));
    }

    public void addComentExamples(){
        entryobject_db = new ArrayList<>();
        entryobject_db.add(new EntryComent("Joan", "El servei una mica lent, pero el menjar molt bo!", 4));
        entryobject_db.add(new EntryComent("Alba", "Un ambient genial!", 5));
        entryobject_db.add(new EntryComent("Pere", "Relació qualitat preu normal... massa car", 3));
    }

    public ArrayList<EntryComent> getEntryComentDB(){
        return entryobject_db;
    }

    public int getDataBaseSize(){
        return entryobject_db.size();
    }
}