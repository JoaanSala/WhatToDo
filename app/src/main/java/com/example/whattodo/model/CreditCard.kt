package com.example.whattodo.model

import javax.crypto.SecretKey

class CreditCard {
    var numTargeta: String? = null
        private set
    var expiritDate: String? = null
        private set
    var nameOwner: String? = null
        private set
    var cVV: String? = null
        private set
    var key: SecretKey? = null
        private set
    var isSelected = false

    constructor() {}
    constructor(numTargeta: String?, expiritDate: String?, nameOwner: String?, CVV: String?, key: SecretKey?, isSelected: Boolean) {
        this.numTargeta = numTargeta
        this.expiritDate = expiritDate
        this.nameOwner = nameOwner
        this.cVV = CVV
        this.key = key
        this.isSelected = isSelected
    }
}