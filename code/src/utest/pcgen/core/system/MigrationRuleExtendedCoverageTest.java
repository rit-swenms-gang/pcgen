/*
 * Extra coverage for MigrationRule: min-only/max-only, dev bounds, inclusivity,
 * conflicting bounds, and invalid inputs. Designed to avoid internal NPEs by
 * always setting all four bounds to explicit values before calling changeAppliesToVer.
 */
package pcgen.core.system;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MigrationRuleExtendedCoverageTest {

    private MigrationRule rule;

    @BeforeEach
    void setUp() {
        rule = new MigrationRule(MigrationRule.ObjectType.SOURCE, "OldKey");
        // Set WIDE defaults so internal parsing never sees null.
        rule.setMinVer("0.0.0");
        rule.setMaxVer("999.999.999");
        rule.setMinDevVer("0.0.0");
        rule.setMaxDevVer("999.999.999");
    }

    // 1) All versions apply (wide bounds)
    @Test
    void appliesToAll_withWideBounds() {
        assertTrue(rule.changeAppliesToVer(new int[]{0,0,0}));
        assertTrue(rule.changeAppliesToVer(new int[]{6,0,1}));
        assertTrue(rule.changeAppliesToVer(new int[]{99,99,99}));
    }

    // 2) Min-only semantics (narrow min, keep max wide)
    @Test
    void minOnly_validsAndInvalids() {
        rule.setMinVer("5.17.7");
        assertTrue(rule.changeAppliesToVer(new int[]{5,17,7}));
        assertTrue(rule.changeAppliesToVer(new int[]{6,0,0}));
        assertFalse(rule.changeAppliesToVer(new int[]{5,17,6}));
        assertFalse(rule.changeAppliesToVer(new int[]{5,16,99}));
    }

    // 3) Max-only semantics (narrow max)
    @Test
    void maxOnly_validsAndInvalids() {
        rule.setMaxVer("6.0.1");
        assertTrue(rule.changeAppliesToVer(new int[]{5,17,10}));
        assertTrue(rule.changeAppliesToVer(new int[]{6,0,1}));
        assertFalse(rule.changeAppliesToVer(new int[]{6,0,2}));
        assertFalse(rule.changeAppliesToVer(new int[]{7,0,0}));
    }

    // 4) MinDev-only (narrow minDev) — dev floor applies only to dev-series
    @Test
    void minDevOnly_validsAndInvalids() {
        // Stable is wide-open; dev has a floor at 5.17.7
        rule.setMinVer("0.0.0");
        rule.setMaxVer("999.999.999");
        rule.setMinDevVer("5.17.7");
        rule.setMaxDevVer("999.999.999");

        // DEV series >= minDev allowed
        assertTrue(rule.changeAppliesToVer(new int[]{5,17,7}));
        assertTrue(rule.changeAppliesToVer(new int[]{5,17,10}));

        // DEV just below minDev rejected
        assertFalse(rule.changeAppliesToVer(new int[]{5,17,6}));

        // STABLE (non-dev) not clamped by minDev; still allowed by minVer
        assertTrue(rule.changeAppliesToVer(new int[]{5,16,99}));
        assertTrue(rule.changeAppliesToVer(new int[]{6,0,0}));
    }

    // 5) MaxDev-only (narrow maxDev) — cap stable so dev cap is meaningful
    @Test
    void maxDevOnly_validsAndInvalids() {
        // Stable capped at 6.0.1; dev capped at 6.1.3
        rule.setMinVer("0.0.0");
        rule.setMaxVer("6.0.1");
        rule.setMinDevVer("0.0.0");
        rule.setMaxDevVer("6.1.3");

        // STABLE under maxVer allowed
        assertTrue(rule.changeAppliesToVer(new int[]{6,0,1}));
        assertTrue(rule.changeAppliesToVer(new int[]{5,17,10}));

        // DEV at/under maxDev allowed
        assertTrue(rule.changeAppliesToVer(new int[]{6,1,3}));

        // DEV above maxDev rejected
        assertFalse(rule.changeAppliesToVer(new int[]{6,1,4}));

        // STABLE above maxVer rejected regardless of dev window
        assertFalse(rule.changeAppliesToVer(new int[]{6,2,0}));
    }

    // 6) Inclusive boundary at minVer
    @Test
    void inclusiveAtMinVer() {
        rule.setMinVer("3.2.1");
        assertTrue(rule.changeAppliesToVer(new int[]{3,2,1}));  // exactly min
        assertTrue(rule.changeAppliesToVer(new int[]{3,2,2}));  // just above
        assertFalse(rule.changeAppliesToVer(new int[]{3,2,0})); // just below
    }

    // 7) Inclusive boundary at maxVer
    @Test
    void inclusiveAtMaxVer() {
        rule.setMaxVer("4.0.0");
        assertTrue(rule.changeAppliesToVer(new int[]{4,0,0}));  // exactly max
        assertTrue(rule.changeAppliesToVer(new int[]{3,9,9}));  // just below
        assertFalse(rule.changeAppliesToVer(new int[]{4,0,1})); // just above
    }

    // 8) Inclusive boundary at minDevVer — dev series only
    @Test
    void inclusiveAtMinDevVer() {
        rule.setMinDevVer("5.17.7");
        rule.setMaxDevVer("999.999.999");
        // exactly minDev is allowed
        assertTrue(rule.changeAppliesToVer(new int[]{5,17,7}));
        // just above is allowed
        assertTrue(rule.changeAppliesToVer(new int[]{5,17,8}));
        // just below minDev (still dev series) is rejected
        assertFalse(rule.changeAppliesToVer(new int[]{5,17,6}));
    }

    // 9) Inclusive boundary at maxDevVer — cap stable so dev cap decides 6.1.x
    @Test
    void inclusiveAtMaxDevVer() {
        // Restrict stable so 6.1.x can't pass via stable window
        rule.setMinVer("0.0.0");
        rule.setMaxVer("6.0.1");

        // Now dev window governs 6.1.x
        rule.setMinDevVer("0.0.0");
        rule.setMaxDevVer("6.1.3");

        // exactly maxDev allowed
        assertTrue(rule.changeAppliesToVer(new int[]{6,1,3}));
        // just above maxDev rejected
        assertFalse(rule.changeAppliesToVer(new int[]{6,1,4}));
    }

    // 10) Both min/max + minDev/maxDev set: intersection and overlay semantics
    @Test
    void combinedStableAndDevWindows() {
        // Stable window: [5.16.4 .. 6.0.1]; Dev window: [5.17.7 .. 6.1.3]
        rule.setMinVer("5.16.4");
        rule.setMaxVer("6.0.1");
        rule.setMinDevVer("5.17.7");
        rule.setMaxDevVer("6.1.3");

        assertTrue(rule.changeAppliesToVer(new int[]{5,17,7}));  // in both
        assertTrue(rule.changeAppliesToVer(new int[]{6,0,1}));   // in both

        // stable above maxVer rejected
        assertFalse(rule.changeAppliesToVer(new int[]{6,0,2}));

        // dev path extends beyond stable max: allowed up to 6.1.3
        assertTrue(rule.changeAppliesToVer(new int[]{6,1,2}));

        // dev just above maxDev rejected
        assertFalse(rule.changeAppliesToVer(new int[]{6,1,4}));

        // stable below minVer rejected
        assertFalse(rule.changeAppliesToVer(new int[]{5,16,3}));
    }

    // 11) Conflicting bounds: minVer > maxVer => nothing should apply
    @Test
    void conflictingStableBounds_yieldNoMatch() {
        rule.setMinVer("8.0.0");
        rule.setMaxVer("7.9.9");
        assertFalse(rule.changeAppliesToVer(new int[]{7,9,9}));
        assertFalse(rule.changeAppliesToVer(new int[]{8,0,0}));
    }

    // 12) Conflicting dev bounds: ensure only dev path decides (exclude via stable)
    @Test
    void conflictingDevBounds_yieldNoMatch() {
        // Stable excludes 6.1.x entirely
        rule.setMinVer("0.0.0");
        rule.setMaxVer("6.0.1");

        // Dev window is impossible: minDev > maxDev
        rule.setMinDevVer("6.1.5");
        rule.setMaxDevVer("6.1.4");

        // Dev-series versions cannot pass stable, and dev window is empty -> false
        assertFalse(rule.changeAppliesToVer(new int[]{6, 1, 2}));
        assertFalse(rule.changeAppliesToVer(new int[]{6, 1, 4}));
    }

    // 13) Null version array throws (public API should not accept null)
    @Test
    void nullArray_throwsNPE() {
        assertThrows(NullPointerException.class, () -> rule.changeAppliesToVer(null));
    }

    // 14) Wrong length arrays: empty throws AIOOBE; others tolerated under wide bounds
    @Test
    void wrongLengthArrays_handledGracefully() {
        // Wide bounds keep parsing from hitting nulls
        rule.setMinVer("0.0.0");
        rule.setMaxVer("999.999.999");
        rule.setMinDevVer("0.0.0");
        rule.setMaxDevVer("999.999.999");

        // Empty array -> AIOOBE (observed behavior)
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> rule.changeAppliesToVer(new int[]{}));

        // Length 2 and 4 are tolerated (no exception) under wide bounds
        assertDoesNotThrow(() -> rule.changeAppliesToVer(new int[]{1, 2}));
        assertDoesNotThrow(() -> rule.changeAppliesToVer(new int[]{1, 2, 3, 4}));

        // And with permissive bounds they evaluate truthy
        assertTrue(rule.changeAppliesToVer(new int[]{1, 2}));
        assertTrue(rule.changeAppliesToVer(new int[]{1, 2, 3, 4}));
    }

    // 15) Very large versions still accepted within wide ceiling
    @Test
    void veryLargeVersionWithinCeiling() {
        rule.setMaxVer("1000.1000.1000");
        assertTrue(rule.changeAppliesToVer(new int[]{500,500,500}));
        assertTrue(rule.changeAppliesToVer(new int[]{1000,1000,1000}));
        assertFalse(rule.changeAppliesToVer(new int[]{1000,1000,1001}));
    }
}