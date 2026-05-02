package com.skillexchange.app.util

/**
 * Skill Point System:
 * - 1 hour offered = 1 Skill Point
 * - Points are transferred only after both parties confirm completion
 * - Trust Score increases by 1 after each confirmed swap (capped at 5)
 */
object SkillPointCalculator {

    /**
     * Calculate skill points required for a given number of hours
     */
    fun hoursToPoints(hours: Int): Int = hours

    /**
     * Check if user has enough skill points to make an offer
     */
    fun canMakeOffer(userPoints: Int, hoursOffered: Int): Boolean {
        return userPoints >= hoursToPoints(hoursOffered)
    }

    /**
     * Calculate new trust score after a successful swap
     * Trust score increases by 0.2 per swap, max 5.0
     */
    fun calculateNewTrustScore(currentScore: Float, completedSwaps: Int): Float {
        val increment = 0.2f
        val newScore = currentScore + increment
        return minOf(newScore, 5.0f)
    }

    /**
     * Get trust level label based on score
     */
    fun getTrustLevel(score: Float): String {
        return when {
            score < 1.0f -> "Newcomer"
            score < 2.0f -> "Apprentice"
            score < 3.0f -> "Skilled"
            score < 4.0f -> "Trusted"
            score < 5.0f -> "Expert"
            else -> "Master Artisan"
        }
    }

    /**
     * Get star count for UI display (1-5)
     */
    fun getStarCount(score: Float): Int {
        return minOf(score.toInt() + 1, 5)
    }
}
