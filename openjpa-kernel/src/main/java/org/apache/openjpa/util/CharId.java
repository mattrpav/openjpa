/*
 * Copyright 2006 The Apache Software Foundation.
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
package org.apache.openjpa.util;

/**
 * {@link OpenJPAId} subclass appropriate for char fields.
 *
 * @author Steve Kim
 */
public final class CharId extends OpenJPAId {

    private final char key;

    public CharId(Class cls, Character key) {
        this(cls, (key == null) ? (char) 0 : key.charValue());
    }

    public CharId(Class cls, String key) {
        this(cls, (key == null) ? (char) 0 : key.charAt(0));
    }

    public CharId(Class cls, char key) {
        super(cls);
        this.key = key;
    }

    public CharId(Class cls, char key, boolean subs) {
        super(cls, subs);
        this.key = key;
    }

    public char getId() {
        return key;
    }

    public Object getIdObject() {
        return new Character(key);
    }

    public String toString() {
        return String.valueOf(key);
    }

    protected int idHash() {
        return key;
    }

    protected boolean idEquals(OpenJPAId o) {
        return key == ((CharId) o).key;
    }
}
