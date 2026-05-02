package com.skillexchange.app

import com.skillexchange.app.util.SkillPointCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SkillPointCalculatorTest {

    @Test
    fun `hoursToPoints converts correctly`() {
        assertEquals(1, SkillPointCalculator.hoursToPoints(1))
        assertEquals(3, SkillPointCalculator.hoursToPoints(3))
        assertEquals(8, SkillPointCalculator.hoursToPoints(8))
    }

    @Test
    fun `canMakeOffer returns true when enough points`() {
        assertTrue(SkillPointCalculator.canMakeOffer(10, 5))
        assertTrue(SkillPointCalculator.canMakeOffer(5, 5))
    }

    @Test
    fun `canMakeOffer returns false when not enough points`() {
        assertFalse(SkillPointCalculator.canMakeOffer(3, 5))
        assertFalse(SkillPointCalculator.canMakeOffer(0, 1))
    }

    @Test
    fun `trustScore increases after swap`() {
        val newScore = SkillPointCalculator.calculateNewTrustScore(0f, 0)
        assertEquals(0.2f, newScore, 0.01f)
    }

    @Test
    fun `trustScore does not exceed 5`() {
        val newScore = SkillPointCalculator.calculateNewTrustScore(4.9f, 24)
        assertEquals(5.0f, newScore, 0.01f)
    }

    @Test
    fun `getTrustLevel returns correct label`() {
        assertEquals("Newcomer", SkillPointCalculator.getTrustLevel(0.5f))
        assertEquals("Apprentice", SkillPointCalculator.getTrustLevel(1.5f))
        assertEquals("Skilled", SkillPointCalculator.getTrustLevel(2.5f))
        assertEquals("Trusted", SkillPointCalculator.getTrustLevel(3.5f))
        assertEquals("Expert", SkillPointCalculator.getTrustLevel(4.5f))
        assertEquals("Master Artisan", SkillPointCalculator.getTrustLevel(5.0f))
    }

    @Test
    fun `getStarCount returns correct value`() {
        assertEquals(1, SkillPointCalculator.getStarCount(0f))
        assertEquals(3, SkillPointCalculator.getStarCount(2.9f))
        assertEquals(5, SkillPointCalculator.getStarCount(5.0f))
    }
}
