package com.example.eyezen

data class DoctorData(
    val name: String = "",
    val specialty: String = "",
    val rating:  String ="",
    val reviews: Int = 0,
    val yearsOfExperience: Int = 0,
    val imageUrl: String = "",
    val distance: String = "",
    val bio:String = ""
)
