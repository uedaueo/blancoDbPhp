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

import java.util.List;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgMethod;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.commons.util.BlancoNameAdjuster;
import blanco.commons.util.BlancoNameUtil;
import blanco.db.common.expander.BlancoDbAbstractMethod;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure;

/**
 * 個別のメソッドを展開するためのクラス。
 * 
 * @author Tosiki Iga
 */
public class GetRowMethodPhp extends BlancoDbAbstractMethod {
    public GetRowMethodPhp(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod("getRow",
                "現在の行のデータをオブジェクトとして取得します。");
        fCgClass.getMethodList().add(cgMethod);

        /*
         * シングル属性が有効である場合には protectedとします。
         */
        if (fSqlInfo.getSingle()) {
            cgMethod.setAccess("protected");
        }

        // 行オブジェクトの型名を取得します。
        final String rowObjectType = fDbSetting.getBasePackage() + ".row."
                + BlancoNameAdjuster.toClassName(fSqlInfo.getName()) + "Row";

        cgMethod.setReturn(fCgFactory.createReturn(rowObjectType, "行オブジェクト"));

        final List listDesc = cgMethod.getLangDoc().getDescriptionList();

        if (fSqlInfo.getSingle()) {
            listDesc.add("シングル属性が有効なのでスコープをprotectedとします。");
            listDesc.add("このメソッドの代わりに GetSingleRowメソッドを利用してください。");
        } else {
            listDesc.add("このメソッドを呼び出す前に、next()などのカーソルを操作するメソッドを呼び出す必要があります。");
        }

        final List listLine = cgMethod.getLineList();

        listLine.add("$result = new "
                + BlancoNameUtil.trimJavaPackage(rowObjectType) + "();");

        int indexCol = 0;
        for (int index = 0; index < fSqlInfo.getResultSetColumnList().size(); index++) {
            final BlancoDbMetaDataColumnStructure columnStructure = (BlancoDbMetaDataColumnStructure) fSqlInfo
                    .getResultSetColumnList().get(index);

            listLine.add("$result->set"
                    + BlancoNameAdjuster.toClassName(columnStructure.getName())
                    + "($this->fCurrentRow[" + index + "]);");

            indexCol++;
        }

        listLine.add("");

        listLine.add("return $result;");
    }
}