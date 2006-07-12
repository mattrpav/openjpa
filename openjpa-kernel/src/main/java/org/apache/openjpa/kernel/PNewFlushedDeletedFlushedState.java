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
package org.apache.openjpa.kernel;

/**
 * <p>Lifecycle state.</p>
 * <p/>
 * <p>Represents an instance that was made persistent within the
 * current	transaction, flushed, deleted, and flushed.</p>
 *
 * @author Patrick Linskey
 */
class PNewFlushedDeletedFlushedState
    extends PNewFlushedDeletedState {

    void initialize(StateManagerImpl context) {
    }

    PCState persist(StateManagerImpl context) {
        context.eraseFlush();
        return PNEW;
    }
}
