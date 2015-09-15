/*
 * blanco Framework
 * Copyright (C) 2004-2006 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db.expander.query.invoker;

import java.util.List;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgMethod;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.db.common.expander.BlancoDbAbstractMethod;
import blanco.db.common.util.BlancoDbUtil;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;
import blanco.db.expander.exception.NoRowModifiedExceptionClassPhp;
import blanco.db.expander.exception.TooManyRowsModifiedExceptionClassPhp;

/**
 * 個別のメソッドを展開するためのクラス。
 * 
 * @author Tosiki Iga
 */
public class ExecuteSingleUpdateMethodPhp extends BlancoDbAbstractMethod {
    public ExecuteSingleUpdateMethodPhp(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod(
                "executeSingleUpdate", "実行型クエリを実行します。");
        fCgClass.getMethodList().add(cgMethod);

        cgMethod.getLangDoc().getDescriptionList().add(
                "データベースの制約違反が発生した場合には IntegrityConstraintException 例外が発生します。");
        cgMethod.getLangDoc().getDescriptionList().add("");

        cgMethod.getLangDoc().getDescriptionList().add("single属性が有効");
        cgMethod.getLangDoc().getDescriptionList().add(
                "実行結果が1件以外の場合には、NotSingleRowExceptionクラスを");
        cgMethod.getLangDoc().getDescriptionList().add("派生したクラスの例外が発生します。");

        final List listLine = cgMethod.getLineList();

        listLine.add("$result = $this->executeUpdate();");
        listLine.add("");

        fCgSourceFile.getImportList().add(
                BlancoDbUtil.getRuntimePackage(fDbSetting) + ".exception."
                        + NoRowModifiedExceptionClassPhp.CLASS_NAME);
        fCgSourceFile.getImportList().add(
                BlancoDbUtil.getRuntimePackage(fDbSetting) + ".exception."
                        + TooManyRowsModifiedExceptionClassPhp.CLASS_NAME);

        listLine.add("if ($result == 0) {");
        listLine.add("throw new " + NoRowModifiedExceptionClassPhp.CLASS_NAME
                + "(\"行が一件も変更されませんでした。\");");
        listLine.add("} else if ($result > 1) {");
        listLine.add("$message = \"一件を超える行が変更されてしまいました。影響のあった件数:\" . $result;");
        listLine.add("throw new "
                + TooManyRowsModifiedExceptionClassPhp.CLASS_NAME
                + "($message);");
        listLine.add("}");
    }
}