package com.example.workslateapp.DataClasses


data class Message(
    val sender: String = "",
    val message: String = "",
    val timestamp: String = ""
) {
    // No-argument constructor for Firebase
    constructor() : this("", "", "")
}
