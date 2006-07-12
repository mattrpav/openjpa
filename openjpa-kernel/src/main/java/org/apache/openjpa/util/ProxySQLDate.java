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

import java.io.ObjectStreamException;
import java.sql.Date;

import org.apache.openjpa.kernel.OpenJPAStateManager;

/**
 * <p>Extension of the {@link Date} type that calls the <code>dirty</code>
 * method on its owning persistence capable instance on modification.  This
 * class does not support modification via any deprecated method of the
 * date class.</p>
 *
 * @author Abe White
 * @nojavadoc
 */
public class ProxySQLDate
    extends Date
    implements ProxyDate {

    private transient OpenJPAStateManager _sm = null;
    private transient int _field = -1;

    public ProxySQLDate() {
        super(System.currentTimeMillis());
    }

    public ProxySQLDate(long time) {
        super(time);
    }

    public ProxyDate newInstance() {
        return new ProxySQLDate();
    }

    public void setOwner(OpenJPAStateManager sm, int field) {
        _sm = sm;
        _field = field;
    }

    public OpenJPAStateManager getOwner() {
        return _sm;
    }

    public int getOwnerField() {
        return _field;
    }

    public ChangeTracker getChangeTracker() {
        return null;
    }

    public Object copy(Object orig) {
        return new Date(((Date) orig).getTime());
    }

    public void setYear(int val) {
        Proxies.dirty(this);
        super.setYear(val);
    }

    public void setMonth(int val) {
        Proxies.dirty(this);
        super.setMonth(val);
    }

    public void setDate(int val) {
        Proxies.dirty(this);
        super.setDate(val);
    }

    public void setHours(int val) {
        Proxies.dirty(this);
        super.setHours(val);
    }

    public void setMinutes(int val) {
        Proxies.dirty(this);
        super.setMinutes(val);
    }

    public void setSeconds(int val) {
        Proxies.dirty(this);
        super.setSeconds(val);
    }

    public void setTime(long millis) {
        Proxies.dirty(this);
        super.setTime(millis);
    }

    protected Object writeReplace()
        throws ObjectStreamException {
        if (_sm != null && _sm.isDetached())
            return this;
        return new Date(getTime());
    }
}
