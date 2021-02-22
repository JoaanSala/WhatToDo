package com.example.whattodo.model

class Coment {
    var comentari: String? = null
        private set
    var puntuacio = 0
        private set
    var userName: String? = null
        private set

    constructor() {}
    constructor(userName: String?, comentari: String?, puntuacio: Int) {
        this.userName = userName
        this.comentari = comentari
        this.puntuacio = puntuacio
    }

}