package com.idappstudio.innabajka.model

data class MenuItem(private val cena: Int, private val ilosc: Int, private val nazwa: String, private val obrazek: String, private val opis: String, private val widoczne: Boolean, private val wyroznione: Boolean){

    constructor() : this(0, 0, "", "", "", false, false)

}