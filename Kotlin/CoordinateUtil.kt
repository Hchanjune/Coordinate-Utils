package com.kotlin.spring.management.utils.CoordinateUtils

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.*
import kotlin.collections.Map
import com.kotlin.spring.management.utils.CoordinateUtils.RokOriginLongitudePoints.*
import com.kotlin.spring.management.utils.CoordinateUtils.RokOriginAdditionValue.Companion.getOriginAdditionValue

/**
 * This Static Class provides utilities for converting coordinates between WGS84 and TM (Transverse Mercator) systems.
 *
 * More Formats May Can Be Added Later
 *
 * Requires [RokOriginLongitudePoints], [RokOriginAdditionValue]
 *
 * *Warning - This Code May Act Only For Republic Of Korea's Coordinate System
 *
 * -Version 1.0.0
 * Basic Coordinate ConvertingFunction For WGS84 to TM Only For Now
 *
 * @author hc
 * @since 24.01.11
 *
 */
object CoordinateUtil {
    // Semi-major axis of the WGS84 ellipsoid
    private const val SEMI_MAJOR_AXIS = 6378137.0
    // Flattening of the WGS84 ellipsoid
    private const val FLATTENING = 1 / 298.257222101
    // Eccentricity squared
    private val eSquare = (SEMI_MAJOR_AXIS.pow(2) - (SEMI_MAJOR_AXIS * (1 - FLATTENING)).pow(2)) / SEMI_MAJOR_AXIS.pow(2)

    /**
     * WGS84 좌표를 TM(중부원점) 좌표로 변환
     *
     * @param longitude 경도 (x)
     * @param latitude 위도 (y)
     * @param refYear 기준연도
     * @return [Map] {"longitude" : Double} {"latitude" : Double} 소수점 아래 5자리 반올림
     * @author hc
     * @since 24.01.11
     *
     */
    fun convertWGS84toCentralTM(longitude: Double, latitude: Double, refYear: Int): Map<String, Double> {
        val originLongitude = CENTRAL_POINT.value
        val originAdditionLongitude = getOriginAdditionValue(refYear, false)

        return mapOf(
            "longitude" to this.convertWGS84toTMLongitude(longitude, latitude, originLongitude, originAdditionLongitude),
            "latitude" to this.convertWGS84toTMLatitude(longitude, latitude, originLongitude)
        )
    }

    /**
     * Private Function Converting Longitude From WGS84 To TM
     */
    private fun convertWGS84toTMLongitude(longitude: Double, latitude: Double, originLongitude: Double, originAdditionX: Double): Double {
        // Constants
        val a = SEMI_MAJOR_AXIS

        // Convert degrees to radians
        val latRad = toRadians(latitude)
        val longRad = toRadians(longitude)
        val originLongRad = toRadians(originLongitude)

        // Calculate the correction term at the origin (38 degrees)
        val originCorrectionLat = toRadians(38.0)
        val M0 = a * ((1 - eSquare / 4 - 3 * eSquare.pow(2) / 64 - 5 * eSquare.pow(3) / 256) * originCorrectionLat
                - (3 * eSquare / 8 + 3 * eSquare.pow(2) / 32 + 45 * eSquare.pow(3) / 1024) * sin(2 * originCorrectionLat)
                + (15 * eSquare.pow(2) / 256 + 45 * eSquare.pow(3) / 1024) * sin(4 * originCorrectionLat)
                - (35 * eSquare.pow(3) / 3072) * sin(6 * originCorrectionLat))

        // Calculate M (Meridian arc length)
        val M = a * ((1 - eSquare / 4 - 3 * eSquare.pow(2) / 64 - 5 * eSquare.pow(3) / 256) * latRad
                - (3 * eSquare / 8 + 3 * eSquare.pow(2) / 32 + 45 * eSquare.pow(3) / 1024) * sin(2 * latRad)
                + (15 * eSquare.pow(2) / 256 + 45 * eSquare.pow(3) / 1024) * sin(4 * latRad)
                - (35 * eSquare.pow(3) / 3072) * sin(6 * latRad))

        // Calculate the correction for longitude (Nu)
        val Nu = a / sqrt(1 - eSquare * sin(latRad).pow(2))

        // Calculate the longitude term (K)
        val K = (Nu * tan(latRad) *
                (longRad - originLongRad).pow(2) * cos(latRad).pow(2) / 2 +
                Nu * tan(latRad) *
                (longRad - originLongRad).pow(4) * cos(latRad).pow(4) / 24 *
                (5 - tan(latRad).pow(2) + 9 * eSquare * cos(latRad).pow(2) + 4 * eSquare.pow(2) * cos(latRad).pow(4)) +
                Nu * tan(latRad) *
                (longRad - originLongRad).pow(6) * cos(latRad).pow(6) / 720 *
                (61 - 58 * tan(latRad).pow(2) + tan(latRad).pow(4) + 270 * eSquare * cos(latRad).pow(2) -
                        330 * eSquare * sin(latRad).pow(2)))

        // Calculate the final X coordinate
        val value = originAdditionX + (M - M0 + K)
        return BigDecimal(value).setScale(5, RoundingMode.HALF_UP).toDouble()
    }

    /**
     * Private Function Converting Latitude From WGS84 To TM
     */
    private fun convertWGS84toTMLatitude(longitude: Double, latitude: Double, originLongitude: Double): Double {
        // Constants
        val a = SEMI_MAJOR_AXIS
        val f = FLATTENING

        // Convert degrees to radians
        val latRad = toRadians(latitude)
        val longRad = toRadians(longitude)
        val originLongRad = toRadians(originLongitude)

        // Formula calculation
        val value = 200000 + 1 * (a / sqrt(1 - eSquare * sin(latRad).pow(2))) *
                (((longRad - originLongRad) * cos(latRad)) +
                        ((longRad - originLongRad) * cos(latRad)).pow(3) / 6 *
                        (1 - tan(latRad).pow(2) + (eSquare / (1 - eSquare)) * cos(latRad).pow(2))) +
                ((longRad - originLongRad) * cos(latRad)).pow(5) / 120 *
                (5 - 18 * tan(latRad).pow(2) + tan(latRad).pow(4) +
                        72 * (eSquare / (1 - eSquare)) * cos(latRad).pow(2) -
                        58 * (eSquare / (1 - f).pow(2)))

        return BigDecimal(value).setScale(5, RoundingMode.HALF_UP).toDouble()
    }

    private fun toRadians(degree: Double): Double = Math.toRadians(degree)

}