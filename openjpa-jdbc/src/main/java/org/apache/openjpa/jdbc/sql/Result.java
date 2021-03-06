/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.openjpa.jdbc.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.openjpa.jdbc.kernel.JDBCFetchConfiguration;
import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.meta.FieldMapping;
import org.apache.openjpa.jdbc.meta.JavaSQLTypes;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.lib.util.Closeable;

/**
 * A result from the execution of a query or stored procedure. This
 * interface is aligned closely with the {@link java.sql.ResultSet}, so you
 * can expect like-named methods to have similar semantics. However, the
 * interface has been distilled and simplified, so a result object could
 * easily mask information sources other than a result set.
 *  For more flexible customization of data loading, see the
 * {@link org.apache.openjpa.kernel.PCResultObjectProvider}.
 *
 * @author Abe White
 * @see ResultSetResult
 */
public interface Result
    extends Closeable {

    /**
     * The eager result for the given key, or null if none.
     */
    Object getEager(FieldMapping key);

    /**
     * The eager result for the given key, or null if none.
     */
    void putEager(FieldMapping key, Object res);

    /**
     * Return a new joins instance to use for traversing to related data.
     */
    Joins newJoins();

    /**
     * Free the resources used by this result; do <strong>not</strong>
     * close the SQL connection.
     */
    @Override void close();

    /**
     * Set to true if row locking has been issued for the row.
     */
    void setLocking(boolean locking);

    /**
     * If true, then any results loaded from this Result
     * will be locked in the database.
     */
    boolean isLocking();

    /**
     * Return true if the result supports random access.
     */
    boolean supportsRandomAccess()
        throws SQLException;

    /**
     * Move to the given <strong>0-based</strong> row in the result, or
     * return false if the row does not exist. This method will only be
     * called if the result supports random access.
     */
    boolean absolute(int row)
        throws SQLException;

    /**
     * Advance to the next row, or return false if there are no more rows
     * in the result.
     */
    boolean next()
        throws SQLException;

    /**
     * Push back the last result. In other words, just ignore the next call
     * to {@link #next}. After the first time this method is called,
     * additional calls before a call to {@link #next} or {@link #absolute}
     * should have no further affect.
     */
    void pushBack()
        throws SQLException;

    /**
     * Return the number of rows in this result.
     */
    int size()
        throws SQLException;

    /**
     * Return true if the given id or column is available in the result.
     */
    boolean contains(Object obj)
        throws SQLException;

    /**
     * Return true if all the given ids or columns are available in the result.
     */
    boolean containsAll(Object[] objs)
        throws SQLException;

    /**
     * Return true if the given column is available in the result.
     */
    boolean contains(Column col, Joins joins)
        throws SQLException;

    /**
     * Return true if all the given columns are available in the result.
     */
    boolean containsAll(Column[] cols, Joins joins)
        throws SQLException;

    /**
     * If this is the result of a UNION used to select a hierarchy of
     * mappings, the base mapping represented by the current row.
     * This information is not available after getting any eager results
     * from the row.
     */
    ClassMapping getBaseMapping();

    /**
     * If this is the result of a UNION used to select a hierarchy of
     * mappings, the base mapping represented by the current row.
     * This information is not available after getting any eager results
     * from the row.
     */
    void setBaseMapping(ClassMapping mapping);

    /**
     * If this is the result used to select a toMany relationship,
     * the mappedByFieldMapping is field mapping representing
     * the inverse relationship. This is to avoid unneeded
     * extra sql to retrieve the eager inverse field.
     */
    FieldMapping getMappedByFieldMapping();

    /**
     * If this is the result used to select a toMany relationship,
     * the mappedByFieldMapping is field mapping representing
     * the inverse relationship. This is to avoid unneeded
     * extra sql to retrieve the eager inverse field.
     */
    void setMappedByFieldMapping(FieldMapping fieldMapping);

    /**
     * If this is the result used to select a toMany relationship,
     * the mappedByValue is value of the owner of the toMany relationship.
     * This is to avoid unneeded extra sql to retrieve the eager inverse field.
     */
    Object getMappedByValue();

    /**
     * If this is the result used to select a toMany relationship,
     * the mappedByValue is value of the owner of the toMany relationship.
     * This is to avoid unneeded extra sql to retrieve the eager inverse field.
     */
    void setMappedByValue(Object mappedByValue);

    /**
     * The index of the select within the UNION that the current row
     * corresponds to, or 0.
     */
    int indexOf();

    /**
     * Load a pc object using the given store manager.
     */
    Object load(ClassMapping mapping, JDBCStore store,
        JDBCFetchConfiguration fetch)
        throws SQLException;

    /**
     * Load a pc object using the given store manager.
     */
    Object load(ClassMapping mapping, JDBCStore store,
        JDBCFetchConfiguration fetch, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column or id; may not be supported
     * by results that are not backed by a SQL result set.
     */
    Array getArray(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id; may not be supported
     * by results that are not backed by a SQL result set.
     */
    InputStream getAsciiStream(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id.
     */
    BigDecimal getBigDecimal(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id.
     */
    BigInteger getBigInteger(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id; may not be supported
     * by results that are not backed by a SQL result set.
     */
    InputStream getBinaryStream(Object obj)
        throws SQLException;

    InputStream getLOBStream(JDBCStore store, Object obj)
        throws SQLException;
    /**
     * Return the value stored in the given column or id; may not be supported
     * by results that are not backed by a SQL result set.
     */
    Blob getBlob(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id.
     */
    boolean getBoolean(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id.
     */
    byte getByte(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id; may not be supported
     * by results that are not backed by a SQL result set.
     */
    byte[] getBytes(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id.
     */
    Calendar getCalendar(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id.
     */
    char getChar(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id; may not be supported
     * by results that are not backed by a SQL result set.
     */
    Reader getCharacterStream(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id; may not be supported
     * by results that are not backed by a SQL result set.
     */
    Clob getClob(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id.
     */
    Date getDate(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id; may not be supported
     * by results that are not backed by a SQL result set.
     */
    java.sql.Date getDate(Object obj, Calendar cal)
        throws SQLException;

    /**
     * Return the value stored in the given column or id.
     */
    double getDouble(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id.
     */
    float getFloat(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id.
     */
    int getInt(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id.
     */
    Locale getLocale(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id.
     */
    long getLong(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id.
     */
    Number getNumber(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id.
     *
     * @param obj the column or id whose data to fetch
     * @param metaType the type code from
     * {@link org.apache.openjpa.meta.JavaTypes} or {@link JavaSQLTypes} for the
     * type of the data; if <code>obj</code> is a column, you may specify -1
     * to use the column's recorded java type
     * @param arg some JDBC data access methods use an argument, such
     * as a {@link Calendar} or {@link Map}
     */
    Object getObject(Object obj, int metaType, Object arg)
        throws SQLException;

    /**
     * Return the value stored in the given column or id; may not be supported
     * by results that are not backed by a SQL result set.
     */
    Object getSQLObject(Object obj, Map map)
        throws SQLException;

    /**
     * Return the value stored in the given column or id; may not be supported
     * by results that are not backed by a SQL result set.
     */
    Ref getRef(Object obj, Map map)
        throws SQLException;

    /**
     * Return the value stored in the given column or id.
     */
    short getShort(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id.
     */
    String getString(Object obj)
        throws SQLException;

    /**
     * Return the value stored in the given column or id; may not be supported
     * by results that are not backed by a SQL result set.
     */
    Time getTime(Object obj, Calendar cal)
        throws SQLException;

    /**
     * Return the value stored in the given column or id; may not be supported
     * by results that are not backed by a SQL result set.
     */
    Timestamp getTimestamp(Object obj, Calendar cal)
        throws SQLException;

    /**
     * Return the value stored in the given column; may not be supported
     * by results that are not backed by a SQL result set.
     */
    Array getArray(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column; may not be supported
     * by results that are not backed by a SQL result set.
     */
    InputStream getAsciiStream(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column.
     */
    BigDecimal getBigDecimal(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column.
     */
    BigInteger getBigInteger(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column; may not be supported
     * by results that are not backed by a SQL result set.
     */
    InputStream getBinaryStream(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column; may not be supported
     * by results that are not backed by a SQL result set.
     */
    Blob getBlob(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column.
     */
    boolean getBoolean(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column.
     */
    byte getByte(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column; may not be supported
     * by results that are not backed by a SQL result set.
     */
    byte[] getBytes(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column.
     */
    Calendar getCalendar(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column.
     */
    char getChar(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column; may not be supported
     * by results that are not backed by a SQL result set.
     */
    Reader getCharacterStream(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column; may not be supported
     * by results that are not backed by a SQL result set.
     */
    Clob getClob(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column.
     */
    Date getDate(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column; may not be supported
     * by results that are not backed by a SQL result set.
     */
    java.sql.Date getDate(Column col, Calendar cal, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column.
     */
    double getDouble(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column.
     */
    float getFloat(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column.
     */
    int getInt(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column.
     */
    Locale getLocale(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column.
     */
    long getLong(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column.
     */
    Number getNumber(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column.
     *
     * @param col the column whose data to fetch
     * @param arg some JDBC data access methods use an argument, such
     * as a {@link Calendar} or {@link Map}
     */
    Object getObject(Column col, Object arg, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column; may not be supported
     * by results that are not backed by a SQL result set.
     */
    Object getSQLObject(Column col, Map map, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column; may not be supported
     * by results that are not backed by a SQL result set.
     */
    Ref getRef(Column col, Map map, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column.
     */
    short getShort(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column.
     */
    String getString(Column col, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column; may not be supported
     * by results that are not backed by a SQL result set.
     */
    Time getTime(Column col, Calendar cal, Joins joins)
        throws SQLException;

    /**
     * Return the value stored in the given column; may not be supported
     * by results that are not backed by a SQL result set.
     */
    Timestamp getTimestamp(Column col, Calendar cal, Joins joins)
        throws SQLException;

    /**
     * Return true if the last value fetched was null.
     */
    boolean wasNull()
        throws SQLException;

    /**
     * Informs this receiver about the application element for which a
     * subsequent data request will be made.
     */
    void startDataRequest(Object mapping);

    /**
     * Ends a data request. Must be called in conjunction with
     * {@link #startDataRequest}. The calls can be nested as follws<br />
     * <pre> startDataRequest (relation); startDataRequest (relationsField);
     * getObject("COLUMN_Y"); endDataRequest (); endDataRequest ();
     * </pre>
     */
    void endDataRequest();
}
