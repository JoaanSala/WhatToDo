package com.example.whattodo.model

class Ofert {
    var photo: String? = null
        private set
    var title: String? = null
        private set
    var event: String? = null
        private set
    var localitzacio: String? = null
        private set
    var validesa: String? = null
        private set
    var adquirit = false
    var price: String? = null
        private set

    constructor() {}
    constructor(photo: String?, title: String?, event: String?, localitzacio: String?, validesa: String?, adquirit: Boolean, price: String?) {
        this.photo = photo
        this.title = title
        this.event = event
        this.localitzacio = localitzacio
        this.validesa = validesa
        this.adquirit = adquirit
        this.price = price
    }

}