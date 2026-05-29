package com.horus.wear.presentation.model

data class MedicalProfile(
    val name: String,
    val bloodType: String,
    val age: String,
    val organDonor: Boolean,
    val allergies: List<AllergyItem>,
    val medications: List<String>,
    val conditions: List<String>,
    val emergencyContact: String,
    val emergencyPhone: String,
    val userId: String,
)

data class AllergyItem(
    val name: String,
    val severity: String,
)

