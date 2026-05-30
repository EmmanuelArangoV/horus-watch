package com.horus.wear.presentation.network

import com.horus.wear.presentation.model.AllergyItem
import com.horus.wear.presentation.model.ConditionItem
import com.horus.wear.presentation.model.MedicalProfile
import com.horus.wear.presentation.model.PrivacySettings
import com.horus.wear.presentation.util.BASE_URL
import com.horus.wear.presentation.util.ageFromDob
import com.horus.wear.presentation.util.bloodTypeLabel
import com.horus.wear.presentation.util.getAccessToken
import com.horus.wear.presentation.util.getProfileJson
import com.horus.wear.presentation.util.getRefreshToken
import com.horus.wear.presentation.util.saveProfileJson
import com.horus.wear.presentation.util.saveTokens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.content.Context
import android.os.Build
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

private val client = OkHttpClient.Builder()
    .connectTimeout(5, TimeUnit.SECONDS)
    .readTimeout(5, TimeUnit.SECONDS)
    .build()

suspend fun verifyDeviceCode(code: String): Result<JSONObject> {
    return withContext(Dispatchers.IO) {
        try {
            val json = JSONObject().apply {
                put("code", code)
                put("deviceName", Build.DEVICE)
                put("deviceModel", Build.MODEL)
                put("osVersion", Build.VERSION.RELEASE)
            }
            val body = json.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL/api/auth/device-code/verify")
                .post(body)
                .build()
                
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val respBody = response.body?.string() ?: ""
                Result.success(JSONObject(respBody))
            } else {
                Result.failure(Exception(response.code.toString()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

suspend fun refreshTokens(context: Context): Boolean {
    // ... (existing code)
    return withContext(Dispatchers.IO) {
        val refreshToken = getRefreshToken(context) ?: return@withContext false
        val json = JSONObject().apply { put("refreshToken", refreshToken) }
        val body = json.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("$BASE_URL/api/auth/refresh-token")
            .post(body)
            .build()
        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val respStr = response.body?.string() ?: ""
                val respJson = JSONObject(respStr)
                val newAccess = respJson.optString("accessToken")
                val newRefresh = respJson.optString("refreshToken", refreshToken)
                if (newAccess.isNotEmpty()) {
                    saveTokens(context, newAccess, newRefresh)
                    return@withContext true
                }
            }
            false
        } catch(e: Exception) {
            false
        }
    }
}

suspend fun updatePushToken(context: Context, token: String): Boolean {
    return withContext(Dispatchers.IO) {
        val accessToken = getAccessToken(context) ?: return@withContext false
        val json = JSONObject().apply { put("pushToken", token) }
        val body = json.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("$BASE_URL/api/profile/push-token")
            .post(body)
            .header("Authorization", "Bearer $accessToken")
            .build()
        try {
            val response = client.newCall(request).execute()
            response.isSuccessful
        } catch(e: Exception) {
            false
        }
    }
}

suspend fun fetchMedicalProfile(context: Context, userId: String): MedicalProfile? {
    return withContext(Dispatchers.IO) {
        var jsonString = getProfileJson(context)
        
        try {
            var token = getAccessToken(context)
            var request = Request.Builder()
                .url("$BASE_URL/api/profile/full")
                .apply { if (token != null) header("Authorization", "Bearer $token") }
                .build()
                
            var response = client.newCall(request).execute()
            
            // Si el token expiró, interceptamos el 401 y lo renovamos invisiblemente
            if (response.code == 401) {
                response.close() // Liberar recursos
                if (refreshTokens(context)) {
                    token = getAccessToken(context)
                    request = request.newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                    response = client.newCall(request).execute()
                }
            }
            
            if (response.isSuccessful) {
                val networkJson = response.body?.string() ?: ""
                if (networkJson.isNotEmpty()) {
                    saveProfileJson(context, networkJson)
                    jsonString = networkJson
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Si falla la red, usamos la caché que ya cargamos arriba (jsonString)
        }

        if (jsonString.isNullOrEmpty()) return@withContext null

        try {
            val json = JSONObject(jsonString)

            val personalInfo = json.optJSONObject("personalInfo")
            val medicalProfile = json.optJSONObject("medicalProfile")
            val privacySettingsJson = json.optJSONObject("privacySettings")
            val allergiesArr = json.optJSONArray("allergies")
            val medsArr = json.optJSONArray("medications")
            val conditionsArr = json.optJSONArray("chronicConditions")
            val contactsArr = json.optJSONArray("emergencyContacts")

            val firstName = personalInfo?.optString("firstName", "") ?: ""
            val lastName = personalInfo?.optString("lastName", "") ?: ""
            val bloodType = personalInfo?.optString("bloodType", "") ?: ""
            val dob = personalInfo?.optString("dateOfBirth", "") ?: ""
            val organDonor = medicalProfile?.optBoolean("organDonor", false) ?: false
            val insuranceProvider = (medicalProfile?.optString("insuranceProvider") ?: "")
                .ifEmpty { medicalProfile?.optString("insurance_provider") ?: "" }
            val notes = medicalProfile?.optString("additionalNotes", "") ?: ""

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

            val conditions = mutableListOf<ConditionItem>()
            if (conditionsArr != null) {
                for (i in 0 until conditionsArr.length()) {
                    val cObj = conditionsArr.getJSONObject(i)
                    conditions.add(ConditionItem(
                        name = cObj.optString("conditionName", ""),
                        status = cObj.optString("status", "ACTIVE")
                    ))
                }
            }

            var contactName = ""
            var contactPhone = ""
            if (contactsArr != null && contactsArr.length() > 0) {
                val c = contactsArr.getJSONObject(0)
                contactName = "${c.optString("fullName", "")} (${c.optString("relationship", "")})"
                contactPhone = c.optString("phonePrimary", "")
            }

            val privacySettings = PrivacySettings(
                showFullName = privacySettingsJson?.optBoolean("showFullName", true) ?: true,
                showAge = privacySettingsJson?.optBoolean("showAge", true) ?: true,
                showBloodType = privacySettingsJson?.optBoolean("showBloodType", true) ?: true,
                showMedications = privacySettingsJson?.optBoolean("showMedications", true) ?: true,
                showAllergies = privacySettingsJson?.optBoolean("showAllergies", true) ?: true,
                showEmergencyContacts = privacySettingsJson?.optBoolean("showEmergencyContacts", true) ?: true,
                showMedicalHistory = privacySettingsJson?.optBoolean("showMedicalHistory", true) ?: true,
                showChronicConditions = privacySettingsJson?.optBoolean("showChronicConditions", true) ?: true,
            )

            MedicalProfile(
                name = if (privacySettings.showFullName) "$firstName $lastName".trim() else "Oculto por privacidad",
                bloodType = if (privacySettings.showBloodType && bloodType.isNotEmpty()) bloodTypeLabel(bloodType) else "—",
                age = if (privacySettings.showAge) ageFromDob(dob) else "",
                organDonor = if (privacySettings.showMedicalHistory) organDonor else false,
                insuranceProvider = if (privacySettings.showMedicalHistory) insuranceProvider else "",
                allergies = if (privacySettings.showAllergies) allergies else emptyList(),
                medications = if (privacySettings.showMedications) medications else emptyList(),
                conditions = if (privacySettings.showChronicConditions) conditions else emptyList(),
                emergencyContact = if (privacySettings.showEmergencyContacts) contactName else "",
                emergencyPhone = if (privacySettings.showEmergencyContacts) contactPhone else "",
                userId = userId,
                medicalNotes = if (privacySettings.showMedicalHistory) notes else "",
                privacySettings = privacySettings
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
