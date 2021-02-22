package com.example.whattodo.model;

import javax.crypto.SecretKey;

public class CreditCard {

    private String numTargeta, expiritDate, nameOwner, CVV;
    private SecretKey key;
    private boolean isSelected;

    public CreditCard(){}

    public CreditCard(String numTargeta, String expiritDate, String nameOwner, String CVV, SecretKey key, boolean isSelected) {
        this.numTargeta = numTargeta;
        this.expiritDate = expiritDate;
        this.nameOwner = nameOwner;
        this.CVV = CVV;
        this.key = key;
        this.isSelected = isSelected;
    }

    public String getNumTargeta() {
        return numTargeta;
    }

    public String getExpiritDate() {
        return expiritDate;
    }

    public String getNameOwner() {
        return nameOwner;
    }

    public String getCVV() {
        return CVV;
    }

    public SecretKey getKey(){
        return key;
    }

    public boolean isSelected(){
        return isSelected;
    }

    public void setSelected(boolean isSelected){
        this.isSelected = isSelected;
    }

}

