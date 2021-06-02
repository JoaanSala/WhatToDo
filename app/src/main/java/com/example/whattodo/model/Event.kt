package com.example.whattodo.model

class Event {
    var Titol: String? = null
        private set
    var Localitzacio: String? = null
        private set
    var Telefon: String? = null
        private set
    var Detalls: String? = null
        private set
    var Adreça: String? = null
        private set
    var Ciutat: String? = null
        private set
    var Qualificacio: String? = null
        private set
    var Latitud: Double? = null
        private set
    var Longitud: Double? = null
        private set
    var URL: String? = null
        private set

    constructor() {}
    constructor(Titol: String?, Localitzacio: String?, Telefon: String?, Detalls: String?, Adreça: String?, Ciutat: String?, Latitud: Double?, Longitud: Double?, Qualificacio: String?, URL: String?) {
        this.Titol = Titol
        this.Localitzacio = Localitzacio
        this.Telefon = Telefon
        this.Detalls = Detalls
        this.Adreça = Adreça
        this.Ciutat = Ciutat
        this.Latitud = Latitud
        this.Longitud = Longitud
        this.Qualificacio = Qualificacio
        this.URL = URL
    }
}