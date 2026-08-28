package com.library.config;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.dialect.pagination.LimitLimitHandler;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo;

import java.sql.Types;

/**
 * Minimal Hibernate 6 dialect for SQLite.
 * SQLite uses dynamic typing, so most JDBC type codes map to a handful of
 * storage classes (INTEGER, REAL, TEXT, BLOB).
 */
public class SQLiteDialect extends Dialect {

    public SQLiteDialect() {
        super();
    }

    public SQLiteDialect(DialectResolutionInfo info) {
        super(info);
    }

    @Override
    protected String columnType(int sqlTypeCode) {
        switch (sqlTypeCode) {
            case Types.BIT:
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.BIGINT:
                return "integer";
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
                return "real";
            case Types.BOOLEAN:
                return "integer";
            case Types.DATE:
                return "date";
            case Types.TIME:
                return "time";
            case Types.TIMESTAMP:
                return "timestamp";
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
            case Types.BLOB:
                return "blob";
            case Types.CLOB:
                return "text";
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            default:
                return "text";
        }
    }

    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return new SQLiteIdentityColumnSupport();
    }

    @Override
    public LimitHandler getLimitHandler() {
        return LimitLimitHandler.INSTANCE;
    }

    @Override
    public boolean supportsFetchClause(org.hibernate.query.sqm.FetchClauseType type) {
        return false;
    }

    private static class SQLiteIdentityColumnSupport extends IdentityColumnSupportImpl {
        @Override
        public boolean supportsIdentityColumns() {
            return true;
        }

        @Override
        public String getIdentitySelectString(String table, String column, int type) {
            return "select last_insert_rowid()";
        }

        @Override
        public String getIdentityColumnString(int type) {
            return "integer";
        }
    }
}
