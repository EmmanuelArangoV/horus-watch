package com.horus.wear.presentation.tile

import android.content.Context
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ColorBuilders.argb
import androidx.wear.protolayout.DeviceParametersBuilders
import androidx.wear.protolayout.DimensionBuilders.dp
import androidx.wear.protolayout.DimensionBuilders.sp
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.*
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TileService
import androidx.wear.tiles.tooling.preview.Preview
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tiles.tooling.preview.TilePreviewHelper
import androidx.wear.tooling.preview.devices.WearDevices
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.horus.wear.R
import com.horus.wear.presentation.util.ageFromDob
import com.horus.wear.presentation.util.bloodTypeLabel
import com.horus.wear.presentation.util.getProfileJson
import org.json.JSONObject

class HorusEmergencyTileService : TileService() {
    private val RESOURCES_VERSION = "22"
    
    val ID_IC_HEART = "ic_heart"

    override fun onTileRequest(
        requestParams: RequestBuilders.TileRequest
    ): ListenableFuture<TileBuilders.Tile> {
        val tile = TileBuilders.Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(
                TimelineBuilders.Timeline.Builder()
                    .addTimelineEntry(
                        TimelineBuilders.TimelineEntry.Builder()
                            .setLayout(
                                LayoutElementBuilders.Layout.Builder()
                                    .setRoot(tileLayout(requestParams.deviceConfiguration, this))
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()
        return Futures.immediateFuture(tile)
    }

    override fun onTileResourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest
    ): ListenableFuture<ResourceBuilders.Resources> {
        return Futures.immediateFuture(
            ResourceBuilders.Resources.Builder()
                .setVersion(RESOURCES_VERSION)
                .addIdToImageMapping(ID_IC_HEART, ResourceBuilders.ImageResource.Builder()
                    .setAndroidResourceByResId(ResourceBuilders.AndroidImageResourceByResId.Builder()
                        .setResourceId(R.drawable.ic_heart)
                        .build()
                    ).build()
                )
                .build()
        )
    }

    private fun tileLayout(
        deviceParameters: DeviceParametersBuilders.DeviceParameters,
        context: Context
    ): LayoutElement {
        return HorusTileLayout.build(context, deviceParameters)
    }
}

object HorusTileLayout {
    fun build(
        context: Context,
        deviceParameters: DeviceParametersBuilders.DeviceParameters
    ): LayoutElement {
        val jsonString = getProfileJson(context)

        var name = "Sin Sincronizar"
        var blood = "—"
        var age = ""
        var organDonor = false
        var showBlood = true
        var isLogged = false

        if (!jsonString.isNullOrEmpty()) {
            try {
                isLogged = true
                val json = JSONObject(jsonString)
                val personalInfo = json.optJSONObject("personalInfo")
                val medicalProfile = json.optJSONObject("medicalProfile")
                val privacySettingsJson = json.optJSONObject("privacySettings")

                val firstName = personalInfo?.optString("firstName", "") ?: ""
                val lastName = personalInfo?.optString("lastName", "") ?: ""
                val rawBlood = personalInfo?.optString("bloodType", "") ?: ""
                val dob = personalInfo?.optString("dateOfBirth", "") ?: ""
                
                organDonor = medicalProfile?.optBoolean("organDonor", false) ?: false

                val privacyShowFullName = privacySettingsJson?.optBoolean("showFullName", true) ?: true
                val privacyShowAge = privacySettingsJson?.optBoolean("showAge", true) ?: true
                showBlood = privacySettingsJson?.optBoolean("showBloodType", true) ?: true

                name = if (privacyShowFullName) "$firstName $lastName".trim() else "Oculto"
                blood = if (showBlood && rawBlood.isNotEmpty()) bloodTypeLabel(rawBlood) else "—"
                age = if (privacyShowAge) ageFromDob(dob) else ""
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val launchAction = ActionBuilders.LaunchAction.Builder()
            .setAndroidActivity(
                ActionBuilders.AndroidActivity.Builder()
                    .setPackageName("com.horus.wear")
                    .setClassName("com.horus.wear.presentation.MainActivity")
                    .build()
            )
            .build()

        val horusSecondaryLight = 0xFFF4F0E8.toInt()
        val horusTextDark = 0xFF2C2C2E.toInt()
        val horusTextMuted = 0xFFA0A0A0.toInt()
        val horusPastelPink = 0xFFFFAEE4.toInt()
        val horusPastelBlue = 0xFFAFD5FF.toInt()
        val horusSuccess = 0xFF6BCC7B.toInt()

        val now = java.util.Calendar.getInstance()
        val timeString = String.format(java.util.Locale.getDefault(), "%02d:%02d", now.get(java.util.Calendar.HOUR_OF_DAY), now.get(java.util.Calendar.MINUTE))

        // Root container with lateral padding to prevent name clipping on round screens
        val root = Column.Builder()
            .setHorizontalAlignment(HORIZONTAL_ALIGN_CENTER)
            .setWidth(androidx.wear.protolayout.DimensionBuilders.expand())
            .setHeight(androidx.wear.protolayout.DimensionBuilders.expand())
            .setModifiers(
                ModifiersBuilders.Modifiers.Builder()
                    .setBackground(
                        ModifiersBuilders.Background.Builder()
                            .setColor(argb(horusSecondaryLight))
                            .build()
                    )
                    .setPadding(
                        ModifiersBuilders.Padding.Builder()
                            .setStart(dp(14f))
                            .setEnd(dp(14f))
                            .build()
                    )
                    .setClickable(
                        ModifiersBuilders.Clickable.Builder()
                            .setId("launch_app")
                            .setOnClick(launchAction)
                            .build()
                    )
                    .build()
            )

        // 1. Time at top
        root.addContent(Spacer.Builder().setHeight(dp(12f)).build())
        root.addContent(
            Text.Builder()
                .setText(timeString)
                .setFontStyle(
                    FontStyle.Builder()
                        .setColor(argb(horusTextMuted))
                        .setSize(sp(13f))
                        .setPreferredFontFamilies("sans-serif-condensed")
                        .build()
                )
                .build()
        )

        // 2. HORUS Header
        root.addContent(Spacer.Builder().setHeight(dp(4f)).build())
        root.addContent(
            Row.Builder()
                .setVerticalAlignment(VERTICAL_ALIGN_CENTER)
                .addContent(
                    Box.Builder()
                        .setWidth(dp(5f))
                        .setHeight(dp(5f))
                        .setModifiers(
                            ModifiersBuilders.Modifiers.Builder()
                                .setBackground(
                                    ModifiersBuilders.Background.Builder()
                                        .setColor(argb(horusPastelBlue))
                                        .setCorner(ModifiersBuilders.Corner.Builder().setRadius(dp(2.5f)).build())
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .addContent(Spacer.Builder().setWidth(dp(5f)).build())
                .addContent(
                    Text.Builder()
                        .setText("HORUS")
                        .setFontStyle(
                            FontStyle.Builder()
                                .setColor(argb(horusTextMuted))
                                .setWeight(FONT_WEIGHT_BOLD)
                                .setSize(sp(11f))
                                .setPreferredFontFamilies("sans-serif-condensed")
                                .build()
                        )
                        .build()
                )
                .build()
        )

        root.addContent(Spacer.Builder().setHeight(dp(14f)).build())

        if (!isLogged) {
            root.addContent(
                Text.Builder()
                    .setText("Abre la app e inicia sesión")
                    .setFontStyle(
                        FontStyle.Builder()
                            .setColor(argb(horusTextDark))
                            .setSize(sp(14f))
                            .setPreferredFontFamilies("sans-serif-condensed")
                            .build()
                    )
                    .setMaxLines(2)
                    .setMultilineAlignment(TEXT_ALIGN_CENTER)
                    .build()
            )
        } else {
            // 3. User Name - Condensed Bold to prevent clipping
            root.addContent(
                Text.Builder()
                    .setText(name)
                    .setFontStyle(
                        FontStyle.Builder()
                            .setColor(argb(horusTextDark))
                            .setWeight(FONT_WEIGHT_BOLD)
                            .setSize(sp(17f))
                            .setPreferredFontFamilies("sans-serif-condensed")
                            .build()
                    )
                    .setMaxLines(2)
                    .setMultilineAlignment(TEXT_ALIGN_CENTER)
                    .build()
            )

            // Age
            if (age.isNotEmpty()) {
                root.addContent(Spacer.Builder().setHeight(dp(2f)).build())
                root.addContent(
                    Text.Builder()
                        .setText(age)
                        .setFontStyle(
                            FontStyle.Builder()
                                .setColor(argb(horusTextMuted))
                                .setSize(sp(12f))
                                .setPreferredFontFamilies("sans-serif-condensed")
                                .build()
                        )
                        .build()
                )
            }

            // Blood Type Pill
            if (showBlood && blood != "—") {
                root.addContent(Spacer.Builder().setHeight(dp(8f)).build())
                root.addContent(
                    Box.Builder()
                        .setModifiers(
                            ModifiersBuilders.Modifiers.Builder()
                                .setBackground(
                                    ModifiersBuilders.Background.Builder()
                                        .setColor(argb(horusPastelPink))
                                        .setCorner(ModifiersBuilders.Corner.Builder().setRadius(dp(16f)).build())
                                        .build()
                                )
                                .setPadding(
                                    ModifiersBuilders.Padding.Builder()
                                        .setTop(dp(4f)).setBottom(dp(4f))
                                        .setStart(dp(12f)).setEnd(dp(12f))
                                        .build()
                                )
                                .build()
                        )
                        .addContent(
                            Row.Builder()
                                .setVerticalAlignment(VERTICAL_ALIGN_CENTER)
                                .addContent(
                                    Image.Builder()
                                        .setResourceId("ic_heart")
                                        .setWidth(dp(12f))
                                        .setHeight(dp(12f))
                                        .build()
                                )
                                .addContent(Spacer.Builder().setWidth(dp(4f)).build())
                                .addContent(
                                    Text.Builder()
                                        .setText(blood)
                                        .setFontStyle(
                                            FontStyle.Builder()
                                                .setColor(argb(horusTextDark))
                                                .setWeight(FONT_WEIGHT_BOLD)
                                                .setSize(sp(14f))
                                                .setPreferredFontFamilies("sans-serif-condensed")
                                                .build()
                                        )
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
            }
            
            if (organDonor) {
                root.addContent(Spacer.Builder().setHeight(dp(6f)).build())
                root.addContent(
                    Text.Builder()
                        .setText("Donante de órganos")
                        .setFontStyle(
                            FontStyle.Builder()
                                .setColor(argb(horusSuccess))
                                .setSize(sp(10f))
                                .setWeight(FONT_WEIGHT_BOLD)
                                .setPreferredFontFamilies("sans-serif-condensed")
                                .build()
                        )
                        .build()
                )
            }
        }

        // 4. Footer CTA
        root.addContent(Spacer.Builder().setHeight(dp(12f)).build())
        root.addContent(
            Text.Builder()
                .setText("Toca para entrar a la app")
                .setFontStyle(
                    FontStyle.Builder()
                        .setColor(argb(horusTextMuted))
                        .setSize(sp(9f))
                        .setPreferredFontFamilies("sans-serif-condensed")
                        .build()
                )
                .build()
        )

        return root.build()
    }
}

@Preview(device = WearDevices.SMALL_ROUND)
fun horusTilePreview(context: Context): TilePreviewData {
    return TilePreviewData(
        onTileRequest = { request ->
            TilePreviewHelper.singleTimelineEntryTileBuilder(
                HorusTileLayout.build(context, request.deviceConfiguration)
            ).build()
        }
    )
}
