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
package dk.ange.octave;

import java.io.Reader;
import java.io.StringReader;

import dk.ange.octave.exception.OctaveException;
import dk.ange.octave.type.OctaveScalar;

/**
 * @author Kim Hansen
 */
public class RunOctave {

    /**
     * @param args
     * @throws OctaveException
     */
    public static void main(final String[] args) throws OctaveException {
        final Octave octave = new Octave();
        try {
            octave.set("a", new OctaveScalar(42));
            octave.execute("a");
            System.out.println("Java: a = " + new OctaveScalar(octave.get("a")).getDouble());
            octave.execute("a=a+10");
            System.out.println("Java: a = " + new OctaveScalar(octave.get("a")).getDouble());

            final Reader outputReader = octave.executeReader(new StringReader("a\na=a+10;\na"));
            while (true) {
                final int c = outputReader.read();
                if (c == -1) {
                    break;
                }
                System.out.print((char) c);
            }
            outputReader.close();

            octave.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        System.out.println("END.");
    }

}
