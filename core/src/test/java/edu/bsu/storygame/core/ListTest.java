package edu.bsu.storygame.core;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ListTest {
    @Test
    public void testTwoEmptyImmutableListsAreEqual() {
        //noinspection EqualsWithItself
        assertTrue(ImmutableList.of().equals(ImmutableList.of()));
    }

    @Test
    public void testTwoEmptyImmutableListsHaveSameHashCode() {
        assertTrue(ImmutableList.of().hashCode() == ImmutableList.of().hashCode());
    }

    @Test
    public void testTwoImmutableListsWithSameContentAreEqual() {
        assertTrue(ImmutableList.of(1).equals(ImmutableList.of(1)));
    }

    @Test
    public void testTwoImmutableListsWithSameContentHaveSameHashcode() {
        assertTrue(ImmutableList.of(1).hashCode() == ImmutableList.of(1).hashCode());
    }
}
