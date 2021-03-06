/*
 * Copyright 2007, 2008 Ange Optimization ApS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.simuline.octave.type;

import        eu.simuline.testhelpers.Actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

//import net.sourceforge.cobertura.coveragedata.HasBeenInstrumented;

/**
 * Test {@link OctaveDouble}
 */
public class TestOctaveDouble {

    /**
     * Test
     */
    @Test public void testScalar() {
        final OctaveDouble s1a = Octave.scalar(1);
        final OctaveDouble s1b = Octave.scalar(1);
        final OctaveDouble s1c = Octave.scalar(0);
        s1c.set(1, 1, 1);

        assertEquals(s1a, s1b);
        assertEquals(s1a, s1c);
        assertEquals(s1b, s1c);
        assertNotSame(s1a, s1b);
        assertNotSame(s1a, s1c);
        assertNotSame(s1b, s1c);

        final OctaveDouble s0 = Octave.scalar(0);
        final OctaveDouble s2 = Octave.scalar(2);

        assertTrue(!s1a.equals(s0));
        assertTrue(!s1a.equals(s2));
    }

    /**
     */
    @Test public void testGetAndSet() {
        final OctaveDouble matrix = new OctaveDouble(3, 6, 5, 4);
        matrix.set(2.0, 2, 5, 2, 3);
        for (int row = 1; row <= 3; row++) {
            for (int column = 1; column <= 6; column++) {
                for (int depth = 1; depth <= 5; depth++) {
                    for (int coffee = 1; coffee <= 4; coffee++) {
                        if (row == 2 && column == 5 && 
			    depth == 2 && coffee == 3) {
                            assertEquals(2.0, 
					 matrix.get(row, column, depth, coffee),
					 0.0);
                        } else {
                            assertEquals(0.0, 
					 matrix.get(row, column, depth, coffee),
					 0.0);
                        }
                    }
                }
            }
        }
        try {
            matrix.get(2, 3, 1, 0);
            fail("Attempt to get with a position " + 
		 "that includes a 0 should fail");
        } catch (final IndexOutOfBoundsException e) {
            // ok
        }
        try {
            matrix.get(2, 3, 10, 3);
            fail("Attempt to get with a position " + 
		 "that exceeds range should fail");
        } catch (final IndexOutOfBoundsException e) {
            // ok
        }
        try {
            matrix.get(2, 3, 2, 3, 4);
            fail("Attempt to get with a position " + 
		 "that exceeds dimensions should fail");
        } catch (final IndexOutOfBoundsException e) {
            // ok
        }

    }

    /**
     * Test that the Matrix is not modified when the size matrix is changed.
     */
    @Test public void testSizeConstructorModify() {
        final int[] size = new int[] { 2, 2 };
        final OctaveDouble matrix = new OctaveDouble(size);
        assertEquals(2, matrix.getSize(1));
        assertEquals(2, matrix.getSize(2));
        size[1] = 3;
        assertEquals(2, matrix.getSize(1));
        assertEquals(2, matrix.getSize(2));
    }

    /**
     */
    @Test public void testSizeConstructor() {
        final OctaveDouble matrix = new OctaveDouble(3, 6, 5, 4);
        assertEquals(4, matrix.getSizeLength());
        assertEquals(3, matrix.getSize(1));
        assertEquals(6, matrix.getSize(2));
        assertEquals(5, matrix.getSize(3));
        assertEquals(4, matrix.getSize(4));

        final OctaveDouble matrixEmpty = new OctaveDouble(0, 0);
	assertEquals(matrixEmpty.dataSize(), 0);

        try {
            new OctaveDouble(1);
            fail("OctaveMatrix should not support one dimensional matrices");
        } catch (final IllegalArgumentException e) {
            // OK
        }
    }

    /**
     */
    @Test public void testDataSizeConstructor() {
        final double[] data = new double[2 * 3 * 4];
        for (int idx = 0; idx < data.length; idx++) {
            data[idx] = idx + 1.0;
        }
        final OctaveDouble matrix = new OctaveDouble(data, 2, 3, 4);
        double d = 1.0;
        for (int depth = 1; depth <= 4; depth++) {
            for (int column = 1; column <= 3; column++) {
                for (int row = 1; row <= 2; row++) {
                    assertEquals(d, matrix.get(row, column, depth), 0.0);
                    d++;
                }
            }
        }

        // a larger data array is ok
        new OctaveDouble(data, 2, 2, 4);

        try {
            new OctaveDouble(data, 2, 4, 4);
            fail("should throw IllegalArgumentException");
        } catch (final IllegalArgumentException e) {
            assertEquals("length of data(24) is smaller than size([2, 4, 4])",
			 e.getMessage());
        }
    }

    /**
     * Test that shallowCopy makes an identical copy 
     * that does not change when the original is changed.
     */
    @Test public void testShallowCopy() {
        final OctaveDouble a = new OctaveDouble(1, 1);
        a.set(22, 1, 1);
        final OctaveDouble b = a.shallowCopy();
        assertEquals(a, b);
        assertEquals(22.0, a.get(1, 1), 0.0);
        assertEquals(22.0, b.get(1, 1), 0.0);
        a.set(33, 1, 1);
        assertEquals(33.0, a.get(1, 1), 0.0);
        assertEquals(22.0, b.get(1, 1), 0.0);
        b.set(44, 1, 1);
        assertEquals(33.0, a.get(1, 1), 0.0);
        assertEquals(44.0, b.get(1, 1), 0.0);
    }

    /**
     * Test that shallowCopy makes an identical copy 
     * that does not change size when the original is changed.
     */
    @Test public void testShallowCopySize() {
        final OctaveDouble a = new OctaveDouble(2, 2);
        final OctaveDouble b = a.shallowCopy();
        assertEquals(a, b);
        assertEquals(2, a.getSize(1));
        assertEquals(2, a.getSize(2));
        assertEquals(2, b.getSize(1));
        assertEquals(2, b.getSize(2));
        a.set(33, 2, 3);
        assertEquals(2, a.getSize(1));
        assertEquals(3, a.getSize(2));
        assertEquals(2, b.getSize(1));
        assertEquals(2, b.getSize(2));
        b.set(44, 3, 2);
        assertEquals(2, a.getSize(1));
        assertEquals(3, a.getSize(2));
        assertEquals(3, b.getSize(1));
        assertEquals(2, b.getSize(2));
    }

    /**
     */
    @Test public void testMakeCopy() {
        final double[] data = new double[2 * 3 * 4];
        for (int idx = 0; idx < data.length; idx++) {
            data[idx] = idx + 1.0;
        }
        final OctaveDouble matrix = new OctaveDouble(data, 2, 3, 4)
	    .shallowCopy();
        double d = 1.0;
        for (int depth = 1; depth <= 4; depth++) {
            for (int column = 1; column <= 3; column++) {
                for (int row = 1; row <= 2; row++) {
                    assertEquals(matrix.get(row, column, depth), d, 0.0);
                    d++;
                }
            }
        }

    }

    /** */
    @Test public void testGrowth() {
        final OctaveDouble matrix = new OctaveDouble(3, 3, 3, 3);
	// test set and get without resize 
        matrix.set(42.0, 2, 2, 2, 2);
        matrix.set( 1.0, 3, 2, 2, 2);
        matrix.set( 2.0, 2, 3, 2, 2);
        matrix.set( 3.0, 2, 2, 3, 2);
        matrix.set( 4.0, 2, 2, 2, 3);
        assertEquals(42.0, matrix.get(2, 2, 2, 2), 0.0);
        assertEquals( 1.0, matrix.get(3, 2, 2, 2), 0.0);
        assertEquals( 2.0, matrix.get(2, 3, 2, 2), 0.0);
        assertEquals( 3.0, matrix.get(2, 2, 3, 2), 0.0);
        assertEquals( 4.0, matrix.get(2, 2, 2, 3), 0.0);

	// test set and get with resize 
        matrix.set(Math.PI, 4, 5, 7, 6);
        // assertEquals(42.0, matrix.get(2, 2, 2, 2), 0.0);
        // assertEquals( 1.0, matrix.get(3, 2, 2, 2), 0.0);
        // assertEquals( 2.0, matrix.get(2, 3, 2, 2), 0.0);
        // assertEquals( 3.0, matrix.get(2, 2, 3, 2), 0.0);
        // assertEquals( 4.0, matrix.get(2, 2, 2, 3), 0.0);
        // assertEquals(Math.PI, matrix.get(4, 5, 7, 6), 0.0);


	final OctaveDouble matrixB = new OctaveDouble(4, 5, 7, 6);
        matrixB.set(42.0, 2, 2, 2, 2);
        matrixB.set( 1.0, 3, 2, 2, 2);
        matrixB.set( 2.0, 2, 3, 2, 2);
        matrixB.set( 3.0, 2, 2, 3, 2);
        matrixB.set( 4.0, 2, 2, 2, 3);
        matrixB.set(Math.PI, 4, 5, 7, 6);
 	assertEquals(matrixB, matrix);
    }

    /** */
    @Test public void testResize() {
        final OctaveDouble matrix = new OctaveDouble(0, 4);
        assertEquals(2, matrix.getSizeLength());
        assertEquals(0, matrix.getSize(1));
        assertEquals(4, matrix.getSize(2));
        // assertEquals(0, matrix.dataLength()); is 0
        assertTrue(matrix.dataSize() >= 0);

        matrix.set(42.0, 1, 1);
        assertEquals(42.0, matrix.get(1, 1), 0.0);
        assertEquals(2, matrix.getSizeLength());
        assertEquals(1, matrix.getSize(1));
        assertEquals(4, matrix.getSize(2));
        // assertEquals(4, matrix.dataLength()); is 8
        assertTrue(matrix.dataSize() >= 4);
    }

    /** Test Performance of Resize */
    @Test public void testPerformance() {
        OctaveDouble matrix = new OctaveDouble(30, 0);
        final long allowedTime;
//        if (matrix instanceof HasBeenInstrumented) {
	allowedTime = 1800;//1800;
//        } else {
//            allowedTime = 300;
//        }
        long t = System.currentTimeMillis();
        // 4125 was the number of containers in a real job.
        for (int pos = 1; pos <= 4125; ++pos) {
            matrix.set(4.2, 1, pos);
            matrix.set(4.2, 30, pos);
        }
        long timeused = System.currentTimeMillis() - t;
        if (timeused > allowedTime) {
            fail("Performance test didn't finish in " + allowedTime + 
		 "ms (used " + timeused + "ms)");
        }

        matrix = new OctaveDouble(0, 30);
        t = System.currentTimeMillis();
        // 1000 is just some random number
        for (int pos = 1; pos <= 1000; ++pos) {
            matrix.set(4.2, pos, 1);
            matrix.set(4.2, pos, 30);
        }
        timeused = System.currentTimeMillis() - t;
        if (timeused > allowedTime) {
            fail("Performance test didn't finish in " + allowedTime + 
		 "ms (used " + timeused + "ms)");
        }
    }

    /**
     * Test {@link OctaveComplex} resizes correctly
     */
    @Test public void testResize2() {
        final OctaveDouble d = new OctaveDouble(1, 1);
        d.set(22, 1, 2);
        d.set(33, 2, 2);
        assertEquals( 0.0, d.get(1, 1), 0.0);
        assertEquals(22.0, d.get(1, 2), 0.0);
        assertEquals( 0.0, d.get(2, 1), 0.0);
        assertEquals(33.0, d.get(2, 2), 0.0);
    }

    /* -------------------------------------------------------------------- *
     * framework.                                                           *
     * -------------------------------------------------------------------- */


    /**
     * Runs the test case.
     *
     * Uncomment either the textual UI, Swing UI, or AWT UI.
     */
    public static void main(String[] args) {
	Actions.runFromMain();
    }
}
