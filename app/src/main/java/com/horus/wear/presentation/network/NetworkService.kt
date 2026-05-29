package com.horus.wear.presentation.network

import com.horus.wear.presentation.model.AllergyItem
import com.horus.wear.presentation.model.MedicalProfile
import com.horus.wear.presentation.util.BASE_URL
import com.horus.wear.presentation.util.ageFromDob
import com.horus.wear.presentation.util.bloodTypeLabel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

suspend fun fetchMedicalProfile(userId: String): MedicalProfile? {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL("$BASE_URL/emergency/$userId/json")
            val json = JSONObject(url.readText())

            val personalInfo = json.optJSONObject("personalInfo")
            val medicalProfile = json.optJSONObject("medicalProfile")
            val allergiesArr = json.optJSONArray("allergies")
            val medsArr = json.optJSONArray("medications")
            val conditionsArr = json.optJSONArray("chronicConditions")
            val contactsArr = json.optJSONArray("emergencyContacts")

            val firstName = personalInfo?.optString("firstName", "") ?: ""
            val lastName = personalInfo?.optString("lastName", "") ?: ""
            val bloodType = personalInfo?.optString("bloodType", "") ?: ""
            val dob = personalInfo?.optString("dateOfBirth", "") ?: ""
            val organDonor = medicalProfile?.optBoolean("organDonor", false) ?: false

            val allergies = mutableListOf<AllergyItem>()
            if (allergiesArr != null) {
                for (i in 0 until allergiesArr.length()) {
                    val a = allergiesArr.getJSONObject(i)
                    allergies.add(AllergyItem(
                        name = a.optString("allergenName", ""),
                        severity = a.optString("severity", "MILD"),
                    ))
                }
            }

            val medications = mutableListOf<String>()
            if (medsArr != null) {
                for (i in 0 until medsArr.length()) {
                    val m = medsArr.getJSONObject(i)
                    val name = m.optString("customMedicationName", "")
                        .ifEmpty { m.optJSONObject("medication")?.optString("genericName", "") ?: "" }
                    val dosage = m.optString("dosage", "")
                    medications.add(if (dosage.isNotEmpty()) "$name · $dosage" else name)
                }
            }

            val conditions = mutableListOf<String>()
            if (conditionsArr != null) {
                for (i in 0 until conditionsArr.length()) {
                    conditions.add(conditionsArr.getJSONObject(i).optString("conditionName", ""))
                }
            }

            var contactName = ""
            var contactPhone = ""
            if (contactsArr != null && contactsArr.length() > 0) {
                val c = contactsArr.getJSONObject(0)
                contactName = "${c.optString("fullName", "")} (${c.optString("relationship", "")})"
                contactPhone = c.optString("phonePrimary", "")
            }

            MedicalProfile(
                name = "$firstName $lastName".trim(),
                bloodType = if (bloodType.isNotEmpty()) bloodTypeLabel(bloodType) else "—",
                age = ageFromDob(dob),
                organDonor = organDonor,
                allergies = allergies,
                medications = medications,
                conditions = conditions,
                emergencyContact = contactName,
                emergencyPhone = contactPhone,
                userId = userId,
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

