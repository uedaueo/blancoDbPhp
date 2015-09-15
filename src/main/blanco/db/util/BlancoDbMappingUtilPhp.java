/*
 * blanco Framework
 * Copyright (C) 2004-2006 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db.util;

import java.sql.Types;

import blanco.dbmetadata.BlancoDbMetaDataUtil;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure;

/**
 * blancoDbの型マッピングに関するメソッドを集めたクラス。
 * 
 * @author ToshikiIga
 */
public final class BlancoDbMappingUtilPhp {
    /**
     * PDOとしての型名を取得します。
     * 
     * @param columnStructure
     * @return
     */
    public static final String getPdoType(
            final BlancoDbMetaDataColumnStructure columnStructure) {
        switch (columnStructure.getDataType()) {
        case Types.BIT:
        case Types.BOOLEAN:
            return "PDO::PARAM_BOOL";
        case Types.TINYINT:
        case Types.SMALLINT:
        case Types.INTEGER:
        case Types.BIGINT:
            return "PDO::PARAM_INT";
        case Types.REAL:
        case Types.FLOAT:
        case Types.DOUBLE:
        case Types.NUMERIC:
        case Types.DECIMAL:
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.DATE:
        case Types.TIME:
        case Types.TIMESTAMP:
        case Types.LONGVARCHAR:
        case Types.CLOB:
            return "PDO::PARAM_STR";
        case Types.BINARY:
        case Types.VARBINARY:
        case Types.LONGVARBINARY:
        case Types.BLOB:
            return "PDO::PARAM_LOB";
        case Types.JAVA_OBJECT:
        case Types.DISTINCT:
        case Types.STRUCT:
        case Types.ARRAY:
        case Types.NULL:
        case Types.OTHER:
        case Types.REF:
        case Types.DATALINK:
        default:
            throw new IllegalArgumentException("SetInputParameter: 列パラメータ["
                    + columnStructure.getName()
                    + "]("
                    + BlancoDbMetaDataUtil
                            .convertJdbcDataTypeToString(columnStructure
                                    .getDataType())
                    + ")のバインド: 処理できないSQL型("
                    + columnStructure.getDataType()
                    + "/"
                    + BlancoDbMetaDataUtil
                            .convertJdbcDataTypeToString(columnStructure
                                    .getDataType()) + ")が指定されました。");
        }
    }

    /**
     * PHPとしての型名を取得します。
     * 
     * @param columnStructure
     * @return
     */
    public static final String getPhpType(
            final BlancoDbMetaDataColumnStructure columnStructure) {
        switch (columnStructure.getDataType()) {
        case Types.BIT:
        case Types.BOOLEAN:
            return "boolean";
        case Types.TINYINT:
        case Types.SMALLINT:
        case Types.INTEGER:
        case Types.BIGINT:
            return "integer";
        case Types.REAL:
        case Types.FLOAT:
        case Types.DOUBLE:
        case Types.NUMERIC:
        case Types.DECIMAL:
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.DATE:
        case Types.TIME:
        case Types.TIMESTAMP:
        case Types.LONGVARCHAR:
        case Types.CLOB:
            return "string";
        case Types.BINARY:
        case Types.VARBINARY:
        case Types.LONGVARBINARY:
        case Types.BLOB:
            return "object";
        case Types.JAVA_OBJECT:
        case Types.DISTINCT:
        case Types.STRUCT:
        case Types.ARRAY:
        case Types.NULL:
        case Types.OTHER:
        case Types.REF:
        case Types.DATALINK:
        default:
            throw new IllegalArgumentException("SetInputParameter: 列パラメータ["
                    + columnStructure.getName()
                    + "]("
                    + BlancoDbMetaDataUtil
                            .convertJdbcDataTypeToString(columnStructure
                                    .getDataType())
                    + ")のバインド: 処理できないSQL型("
                    + columnStructure.getDataType()
                    + "/"
                    + BlancoDbMetaDataUtil
                            .convertJdbcDataTypeToString(columnStructure
                                    .getDataType()) + ")が指定されました。");
        }
    }
}
