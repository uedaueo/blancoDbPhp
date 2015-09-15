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
import blanco.db.common.expander.BlancoDbAbstractMethod;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;

/**
 * 個別のメソッドを展開するためのクラス。
 */
public class NextMethodPhp extends BlancoDbAbstractMethod {
    public NextMethodPhp(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod("next",
                "カーソルを現在の位置から1行次へ移動します。");
        fCgClass.getMethodList().add(cgMethod);

        /*
         * シングル属性が有効である場合には protectedとします。
         */
        if (fSqlInfo.getSingle()) {
            cgMethod.setAccess("protected");
        }

        cgMethod.setReturn(fCgFactory.createReturn("boolean",
                "新しい現在の行が有効な場合はTRUE、それ以上の行がない場合はFALSE。"));

        cgMethod.getLangDoc().getDescriptionList().add("");
        if (fSqlInfo.getSingle()) {
            cgMethod.getLangDoc().getDescriptionList().add(
                    "シングル属性が有効なのでスコープをprotectedとします。");
        }

        final List listLine = cgMethod.getLineList();

        // resultSetが未確保であるばあい、強制的にexecuteQueryを呼び出します。
        listLine.add("if ($this->fCurrentRow == NULL) {");
        listLine.add("$this->executeQuery();");
        listLine.add("}");
        listLine.add("");
        listLine.add("$result = $this->fStatement->fetch();");
        listLine.add("if (($result !== FALSE)) {");
        listLine.add("$this->fCurrentRow = $result;");
        listLine.add("return TRUE;");
        listLine.add("}");
        listLine.add("return FALSE;");
    }
}