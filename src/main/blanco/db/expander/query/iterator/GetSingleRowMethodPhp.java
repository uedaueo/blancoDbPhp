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
import blanco.db.common.expander.BlancoDbAbstractMethod;
import blanco.db.common.util.BlancoDbUtil;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;
import blanco.db.expander.exception.NoRowFoundExceptionClassPhp;
import blanco.db.expander.exception.TooManyRowsFoundExceptionClassPhp;

/**
 * 個別のメソッドを展開するためのクラス。
 * 
 * シングル属性がtrueの場合にのみ、このクラスは利用されます
 * 
 * @author Tosiki Iga
 */
public class GetSingleRowMethodPhp extends BlancoDbAbstractMethod {
    public GetSingleRowMethodPhp(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod("getSingleRow",
                "現在の行のデータをオブジェクトとして取得します。");
        fCgClass.getMethodList().add(cgMethod);

        // 行オブジェクトの型名を取得します。
        final String rowObjectType = fDbSetting.getBasePackage() + ".row."
                + BlancoNameAdjuster.toClassName(fSqlInfo.getName()) + "Row";

        cgMethod.setReturn(fCgFactory.createReturn(rowObjectType, "行オブジェクト"));

        final List listDesc = cgMethod.getLangDoc().getDescriptionList();

        listDesc.add("このメソッドを利用する場合には、next()などのカーソルを操作するメソッドとは併用しないでください。");
        listDesc.add("");
        listDesc.add("single属性が有効");
        listDesc.add("検索結果が1件以外の場合には、NotSingleRowExceptionクラスを");
        listDesc.add("派生したクラスの例外が発生します。");

        final List listLine = cgMethod.getLineList();

        fCgSourceFile.getImportList().add(
                BlancoDbUtil.getRuntimePackage(fDbSetting) + ".exception."
                        + NoRowFoundExceptionClassPhp.CLASS_NAME);

        listLine.add("if ($this->next() == FALSE) {");
        listLine.add("throw new NoRowFoundException(\"行が検索できませんでした。\");");
        listLine.add("}");
        listLine.add("");

        listLine.add("$result = $this->getRow();");
        listLine.add("");

        fCgSourceFile.getImportList().add(
                BlancoDbUtil.getRuntimePackage(fDbSetting) + ".exception."
                        + TooManyRowsFoundExceptionClassPhp.CLASS_NAME);

        // 1行を超えて変更があったかどうかをチェック。
        listLine.add("if ($this->next()) {");
        listLine
                .add("throw new TooManyRowsFoundException(\"1件以上の行が検索されました。\");");
        listLine.add("}");
        listLine.add("");

        listLine.add("return $result;");
    }
}