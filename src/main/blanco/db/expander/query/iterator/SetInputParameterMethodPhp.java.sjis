/*
 * blanco Framework
 * Copyright (C) 2004-2006 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db.expander.query.iterator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgMethod;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.db.common.expander.BlancoDbAbstractMethod;
import blanco.db.common.util.BlancoDbQueryParserUtil;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;
import blanco.db.util.BlancoDbMappingUtilPhp;
import blanco.dbmetadata.BlancoDbMetaDataUtil;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure;

/**
 * 個別のメソッドを展開するためのクラス。
 * 
 * @author Tosiki Iga
 */
public class SetInputParameterMethodPhp extends BlancoDbAbstractMethod {
    public SetInputParameterMethodPhp(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod(
                "setInputParameter", "クエリに与える入力パラメータをセットします。");
        fCgClass.getMethodList().add(cgMethod);

        for (int index = 0; index < fSqlInfo.getInParameterList().size(); index++) {
            BlancoDbMetaDataColumnStructure columnStructure = (BlancoDbMetaDataColumnStructure) fSqlInfo
                    .getInParameterList().get(index);

            cgMethod
                    .getParameterList()
                    .add(
                            fCgFactory
                                    .createParameter(
                                            columnStructure.getName(),
                                            BlancoDbMappingUtilPhp
                                                    .getPhpType(columnStructure),
                                            "'"
                                                    + columnStructure.getName()
                                                    + "' SQL入力パラメータ。DB上の型は["
                                                    + BlancoDbMetaDataUtil
                                                            .convertJdbcDataTypeToString(columnStructure
                                                                    .getDataType())
                                                    + "]"));
        }

        final List listLine = cgMethod.getLineList();

        // statementが未確保であるばあい、強制的にprepareStatementを呼び出します。
        listLine.add("if ($this->fStatement == NULL) {");
        listLine.add("$this->prepareStatement();");
        listLine.add("}");

        final BlancoDbQueryParserUtil query = new BlancoDbQueryParserUtil(
                fSqlInfo.getQuery());

        // 既に処理をしたパラメータのマップ。
        final HashMap mapProcessedParam = new HashMap();

        final Iterator ite = fSqlInfo.getInParameterList().iterator();
        for (int index = 1; ite.hasNext(); index++) {
            // SQL文からパラメータを発見しています。
            final BlancoDbMetaDataColumnStructure columnStructure = (BlancoDbMetaDataColumnStructure) ite
                    .next();

            final int[] listCol = query.getSqlParameters(columnStructure
                    .getName());
            if (listCol == null) {
                throw new IllegalArgumentException("SQL定義ID["
                        + fSqlInfo.getName() + "]の SQL入力パラメータ["
                        + columnStructure.getName() + "]が結びついていせん.");
            }
            for (int iteSame = 0; iteSame < listCol.length; iteSame++) {
                if (mapProcessedParam.get(columnStructure.getName()) != null) {
                    // 既に処理をされています。
                    // PHP版の場合にはSQLパラメータは名前引き当てであるので、2度目は処理せずにスキップします。
                    continue;
                }
                // 既に処理済のパラメータとして記憶します。
                mapProcessedParam.put(columnStructure.getName(),
                        columnStructure);

                listLine.add("");

                listLine.add("// SQL入力パラメータ[" + columnStructure.getName()
                        + "]を設定します。");
                listLine.add("$this->fStatement->bindParam(':"
                        + columnStructure.getName() + "', $"
                        + columnStructure.getName() + ", "
                        + BlancoDbMappingUtilPhp.getPdoType(columnStructure)
                        + ");");
            }
        }
    }
}