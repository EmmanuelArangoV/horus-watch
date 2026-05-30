package com.horus.wear.presentation.model

data class PrivacySettings(
    val showFullName: Boolean = true,
    val showAge: Boolean = true,
    val showBloodType: Boolean = true,
    val showMedications: Boolean = true,
    val showAllergies: Boolean = true,
    val showEmergencyContacts: Boolean = true,
    val showMedicalHistory: Boolean = true,
    val showChronicConditions: Boolean = true,
)

data class MedicalProfile(
    val name: String,
    val bloodType: String,
    val age: String,
    val organDonor: Boolean,
    val insuranceProvider: String = "",
    val allergies: List<AllergyItem>,
    val medications: List<String>,
    val conditions: List<ConditionItem>,
    val emergencyContact: String,
    val emergencyPhone: String,
    val userId: String,
    val medicalNotes: String = "",
    val privacySettings: PrivacySettings = PrivacySettings()
)

data class AllergyItem(
    val name: String,
    val severity: String,
)

data class ConditionItem(
    val name: String,
    val status: String,
)
