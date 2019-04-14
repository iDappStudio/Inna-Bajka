package com.idappstudio.innabajka.model

data class MenuItem(val cena: Double, val ilosc: Int, val nazwa: String, val obrazek: String, val opis: String, val widoczne: Boolean, val wyroznione: Boolean, val podkategoria: String, val miara: String){

    constructor() : this(0.0, 0, "", "", "", false, false, "", "")

}