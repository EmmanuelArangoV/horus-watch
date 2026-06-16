package com.horus.wear.presentation.util

import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * Simulates heart rate and step sensors for environments where real
 * hardware sensors are not available (emulator, devices without HR sensor).
 *
 * Heart rate: realistic walk around a base of 70 bpm, ±15 range.
 * Steps: increments naturally — faster during "active" bursts.
 */
suspend fun simulateSensors(
    onHeartRate: (Int) -> Unit,
    onSteps: (Int) -> Unit,
    onActivityMinutes: (Int) -> Unit,
) {
    var hr          = Random.nextInt(58, 88)   // aleatorio cada sesión
    var totalSteps  = Random.nextInt(0, 800)   // base aleatoria (simula pasos previos)
    var activeTicks = 0
    var tick        = 0

    while (true) {
        tick++

        // Random walk ±3 bpm, tendencia leve hacia reposo (60-75)
        val bias = if (hr > 75) -1 else if (hr < 60) 1 else 0
        hr = (hr + Random.nextInt(-2, 3) + bias).coerceIn(52, 108)
        onHeartRate(hr)

        // Pasos: ráfagas con frecuencia variable para simular actividad real
        val burstFreq = Random.nextInt(3, 8)  // cada 3-7 ticks (no siempre en 5)
        val burst = if (tick % burstFreq == 0) Random.nextInt(5, 18) else Random.nextInt(0, 2)
        if (burst > 0) activeTicks++
        totalSteps += burst
        onSteps(totalSteps)
        onActivityMinutes(activeTicks / 20)

        delay(3_000L)
    }
}
