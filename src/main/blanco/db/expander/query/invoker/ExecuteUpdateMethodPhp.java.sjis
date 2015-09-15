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
import blanco.commons.util.BlancoNameAdjuster;
import blanco.db.common.expander.BlancoDbAbstractMethod;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure;

/**
 * 個別のメソッドを展開するためのクラス。
 */
public class ExecuteUpdateMethodPhp extends BlancoDbAbstractMethod {
    public ExecuteUpdateMethodPhp(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod(
                "executeUpdate", "実行型クエリを実行します。");
        fCgClass.getMethodList().add(cgMethod);

        cgMethod.setReturn(fCgFactory.createReturn("integer", "処理された行数"));

        /*
         * シングル属性が有効である場合には protectedとします。
         */
        if (fSqlInfo.getSingle()) {
            cgMethod.setAccess("protected");
        } else {
            // publicのままです。
        }

        cgMethod.getLangDoc().getDescriptionList().add(
                "データベースの制約違反が発生した場合には IntegrityConstraintException 例外が発生します。");

        if (fSqlInfo.getSingle()) {
            cgMethod.getLangDoc().getDescriptionList().add("");
            cgMethod.getLangDoc().getDescriptionList().add(
                    "シングル属性が有効なのでスコープをprotectedとします。");
            cgMethod.getLangDoc().getDescriptionList().add(
                    "このメソッドの代わりに ExecuteSingleUpdateメソッドを利用してください。");
        }

        final List listLine = cgMethod.getLineList();

        // statementが未確保であるばあい、強制的にprepareStatementを呼び出します。
        listLine.add("if ($this->fStatement == NULL) {");
        listLine
                .add("// PreparedStatementが未取得の状態なので、ExecuteNonQuery()実行に先立ちPrepareStatement()メソッドを呼び出して取得します。");
        listLine.add("$this->prepareStatement();");
        listLine.add("}");

        listLine.add("");

        String parameteres = "";
        for (int index = 0; index < fSqlInfo.getInParameterList().size(); index++) {
            final BlancoDbMetaDataColumnStructure columnStructure = (BlancoDbMetaDataColumnStructure) fSqlInfo
                    .getInParameterList().get(index);

            if (index != 0) {
                parameteres += ", ";
            }
            parameteres += "$this->f"
                    + BlancoNameAdjuster.toClassName(columnStructure.getName());
        }

        // 例外処理を含めて展開します。
        listLine.add("// TODO 例外処理が入っていません。");
        listLine.add("$result = $this->fStatement->execute();");
        listLine.add("return $this->fStatement->rowCount();");
    }
}