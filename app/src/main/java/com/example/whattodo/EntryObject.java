package com.example.whattodo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class EntryObject implements Parcelable {

    private int mImageResource;
    private String mtitol, mlocalitzacio, mtelefon, mdetalls, madreça, mciutat, mqualificacio;
    private Double mlatitud, mlongitud;

    public EntryObject(int imageResource, String titol, String localitzacio, String telefon, String detalls, String adreça, String ciutat, Double latitud, Double longitud, String qualificacio){
        this.mImageResource = imageResource;
        this.mtitol = titol;
        this.mlocalitzacio = localitzacio;
        this.mtelefon = telefon;
        this.mdetalls = detalls;
        this.madreça = adreça;
        this.mciutat = ciutat;
        this.mlatitud = latitud;
        this.mlongitud = longitud;
        this.mqualificacio = qualificacio;
    }

    protected EntryObject(Parcel in) {
        mImageResource = in.readInt();
        mtitol = in.readString();
        mlocalitzacio = in.readString();
        mtelefon = in.readString();
        mdetalls = in.readString();
        madreça = in.readString();
        mciutat = in.readString();
        mlatitud = in.readDouble();
        mlongitud = in.readDouble();
        mqualificacio = in.readString();
    }

    public static final Creator<EntryObject> CREATOR = new Creator<EntryObject>() {
        @Override
        public EntryObject createFromParcel(Parcel in) {
            return new EntryObject(in);
        }

        @Override
        public EntryObject[] newArray(int size) {
            return new EntryObject[size];
        }
    };

    public int getmImageResource(){
        return mImageResource;
    }

    public String getTitol(){
        return mtitol;
    }

    public String getLocalitzacio(){
        return mlocalitzacio;
    }

    public String getTelefon(){
        return mtelefon;
    }

    public String getDetalls(){
        return mdetalls;
    }

    public String getAdreça(){
        return madreça;
    }

    public String getCiutat() { return mciutat; }

    public Double getLatitud(){
        return mlatitud;
    }

    public Double getLongitud() { return mlongitud; }

    public String getQualificacio(){
        return mqualificacio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mImageResource);
        parcel.writeString(mtitol);
        parcel.writeString(mlocalitzacio);
        parcel.writeString(mtelefon);
        parcel.writeString(mdetalls);
        parcel.writeString(madreça);
        parcel.writeString(mciutat);
        parcel.writeDouble(mlatitud);
        parcel.writeDouble(mlongitud);
        parcel.writeString(mqualificacio);
    }
}
