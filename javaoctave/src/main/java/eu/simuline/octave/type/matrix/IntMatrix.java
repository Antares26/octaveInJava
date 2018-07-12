/*
 * Copyright 2010 Ange Optimization ApS
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
package eu.simuline.octave.type.matrix;

import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * General matrix of <code>int</code> values. 
 */
// used as superclass of class OctaveInt only 
public abstract class IntMatrix 
    extends AbstractGenericMatrix<int[], IntArrayList> {

    /**
     * @param size
     */
    public IntMatrix(final int... size) {
        super(size);
    }

    /**
     * Constructor that reuses the input data. 
     * 
     * @param dataA
     * @param size
     */
    public IntMatrix(final int[] dataA, final int... size) {
        super(dataA, size);
    }

    // superfluous? 
    /**
     * Copy constructor. 
     * 
     * @param o
     */
    public IntMatrix(final IntMatrix o) {
        super(o);
    }

    protected final int[] newD(final int size) {
        return new int[size];
    }

    protected final IntArrayList​ newL(final int size) {
	IntArrayList​ list = new IntArrayList​(size);
	list.size(size);
	return list;
    }

    protected final int newL(int[] data, final int size) {
	this.dataL = new IntArrayList​(data);
	this.dataL.size(size);
	return data.length;
    }



    public final int dataLength() {
        return this.dataA.length;
    }

    protected final boolean dataEquals(final int usedLength,
				       final int[] otherData) {
        for (int i = 0; i < usedLength; i++) {
	    assert this.dataA[i] == this.dataL.get(i);
            if (this.dataA[i] != otherData[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Set the value resizing by need. 
     * 
     * @param value
     * @param pos
     * @see #setPlain(int, int)
     */
    public final void set(final int value, final int... pos) {
        resizeUp(pos);
        setPlain(value, pos2ind(pos));
    }

    /**
     * Set the value assuming resize is not necessary. 
     * 
     * @param value
     * @param pos
     * @see #set(int, int[])
     */
    public final void setPlain(final int value, final int pos) {
        this.dataA[pos] = value;
	this.dataL.set(pos, value);
    }

    // api-docs inherited from AbstractGenericMatrix 
    public final void setPlain(final String value, final int pos) {
	this.dataA[pos] = Integer.parseInt(value.trim());
	this.dataL.set(pos, Integer.parseInt(value.trim()));
    }

    /**
     * Get the value. 
     * 
     * @param pos
     * @return value at pos
     */
    public final int get(final int... pos) {
	assert this.dataL.getInt(pos2ind(pos)) == this.dataA[pos2ind(pos)];
        //return this.dataA[pos2ind(pos)];
	return this.dataL.getInt(pos2ind(pos));
    }

    public final String getPlainString(int pos) {
	assert this.dataL.getInt(pos) == this.dataA[pos];
	return Integer.toString(this.dataL.getInt(pos));
	//return Integer.toString(this.dataA[pos]);
    }


}
