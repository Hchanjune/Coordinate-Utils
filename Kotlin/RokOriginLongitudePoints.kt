package com.kotlin.spring.management.utils.CoordinateUtils

/**
 * 원점경도값
 *
 * CENTRAL_POINT : 중부원점
 *
 * WESTERN_POINT : 서부원점
 *
 * EASTERN_POINT : 동부원점
 *
 * EAST_SEA_POINT : 동해원점
 *
 * @author hc
 * @since 24.01.11
 */
enum class RokOriginLongitudePoints(val value: Double) {
    CENTRAL_POINT(127.0),
    WESTERN_POINT(125.0),
    EASTERN_POINT(129.0),
    EAST_SEA_POINT(131.0)
}