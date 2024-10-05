package com.example.eyezen

data class User(
    val name: String = "",
    val email: String = "",
    val savedQuizzes: Map<String, Boolean> = emptyMap()
)

