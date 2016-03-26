package edu.bsu.storygame.core.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pythagoras.f.Rectangle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public final class ScalablePickerTest {

    private ScalablePicker<TestEnum> picker;
    private TestEnum result;

    @Before
    public void setUp() {
        picker = new ScalablePicker<>();
    }

    @After
    public void tearDown() {
        result = null;
    }

    @Test
    public void testPick_empty_isNull() {
        whenPicking(0, 0);
        assertNull(result);
    }

    private void whenPicking(float x, float y) {
        result = picker.pick(x, y);
    }

    @Test
    public void testPick_oneRegion_pickOutside_isNull() {
        givenAPickerWithOneRegion();
        whenPicking(0, 0);
        assertNull(result);
    }

    private void givenAPickerWithOneRegion() {
        picker.register(new Rectangle(10, 10, 10, 10), TestEnum.VALUE);
    }

    @Test
    public void testPick_oneRegion_inside_isValue() {
        givenAPickerWithOneRegion();
        whenPicking(12, 12);
        thenTheValueWasPicked();
    }

    private void thenTheValueWasPicked() {
        assertEquals(TestEnum.VALUE, result);
    }

    @Test
    public void testPick_halfScale_oneRegion_outsideUpperLeft_IsNull() {
        givenAOneRegionPickerWithOneHalfScale();
        whenPicking(2, 2);
        assertNull(result);
    }

    private void givenAOneRegionPickerWithOneHalfScale() {
        givenAPickerWithOneRegion();
        picker.scale(0.5f);
    }

    @Test
    public void testPick_halfScale_oneRegion_outsideBottomRight_IsNull() {
        givenAOneRegionPickerWithOneHalfScale();
        whenPicking(12, 12);
        assertNull(result);
    }

    @Test
    public void testPick_halfScale_oneRegion_inside_isValue() {
        givenAOneRegionPickerWithOneHalfScale();
        whenPicking(7, 7);
        thenTheValueWasPicked();
    }

    enum TestEnum {
        VALUE
    }
}
