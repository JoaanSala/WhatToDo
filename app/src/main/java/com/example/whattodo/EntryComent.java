package com.example.whattodo;

import android.os.Parcel;
import android.os.Parcelable;

public class EntryComent implements Parcelable {

    String mComentari;
    int mPuntuacio;
    String mUserName;

    public EntryComent (String letterName, String comentari, int puntuacio){
        this.mUserName = letterName;
        this.mComentari = comentari;
        this.mPuntuacio = puntuacio;
    }


    protected EntryComent(Parcel in) {
        mComentari = in.readString();
        mPuntuacio = in.readInt();
        mUserName = in.readString();
    }

    public static final Creator<EntryComent> CREATOR = new Creator<EntryComent>() {
        @Override
        public EntryComent createFromParcel(Parcel in) {
            return new EntryComent(in);
        }

        @Override
        public EntryComent[] newArray(int size) {
            return new EntryComent[size];
        }
    };

    public String getUserName(){ return mUserName; }

    public String getComentari(){ return mComentari; }

    public int getPuntuacio(){ return mPuntuacio; }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mComentari);
        parcel.writeInt(mPuntuacio);
        parcel.writeString(mUserName);
    }
}
