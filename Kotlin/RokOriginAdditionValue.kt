package com.kotlin.spring.management.utils.CoordinateUtils

/**
 * 원점 가산값
 *
 * 2009.12 이후 600000
 *
 * 2009.12 이전 (제주도) 550000
 *
 * 그외 2009.12 이전 500000
 *
 * @author hc
 * @since 24.01.11
 */
enum class RokOriginAdditionValue(val value: Double) {
    POST_2009_DECEMBER(600000.0),
    PRE_2009_DECEMBER_JEJU(550000.0),
    PRE_2009_DECEMBER_OTHERS(500000.0);

    companion object {
        fun getOriginAdditionValue(year: Int, isJeju: Boolean): Double {
            return when {
                year > 2009 -> POST_2009_DECEMBER.value
                year <= 2009 && isJeju -> PRE_2009_DECEMBER_JEJU.value
                else -> PRE_2009_DECEMBER_OTHERS.value
            }
        }
    }
}