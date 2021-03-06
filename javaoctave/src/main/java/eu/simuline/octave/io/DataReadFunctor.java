/*
 * Copyright 2008, 2009 Ange Optimization ApS
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
package eu.simuline.octave.io;

import static eu.simuline.octave.io.OctaveIO.readerReadLine;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.Map;

import eu.simuline.octave.exception.OctaveParseException;
import eu.simuline.octave.exec.ReadFunctor;
import eu.simuline.octave.type.OctaveObject;

/**
 * Functor that reads a single variable named {@link #name} 
 * into {@link #data} via {@link #doReads(Reader)}. 
 */
// ER: Very strange: whereas this read functor reads a single variable only 
// the according write functor writes a map: 
// see OctaveIO 
final class DataReadFunctor implements ReadFunctor {

    /**
     * The name of the variable to be read. 
     */
    private final String name;

    /**
     * After {@link #doReads(Reader)} returns, this contains the read data. 
     */
    private OctaveObject data;

    /**
     * @param name
     */
    DataReadFunctor(final String name) {
        this.name = name;
    }

    /**
     * @param reader
     */
    @Override
    public void doReads(final Reader reader) {
        final BufferedReader bufferedReader = new BufferedReader(reader);
        final String createByOctaveLine = readerReadLine(bufferedReader);
        if (createByOctaveLine == null || 
	    !createByOctaveLine.startsWith("# Created by Octave")) {
            throw new OctaveParseException
		("Not created by Octave?: '" + createByOctaveLine + "'");
        }
        final Map<String, OctaveObject> map = 
	    OctaveIO.readWithName(bufferedReader);
        if (map.size() != 1) {
            throw new OctaveParseException
		("Expected map of size 1 but got " + map + "'");
        }
        if (!map.containsKey(this.name)) {
            throw new OctaveParseException
		("Expected variable named '" + this.name + 
		 "' but got '" + map + "'");
        }
        this.data = map.get(this.name);
    }

    /**
     * @return the data
     */
    public OctaveObject getData() {
        return this.data;
    }

}
