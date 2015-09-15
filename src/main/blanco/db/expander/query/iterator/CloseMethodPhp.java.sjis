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
public class CloseMethodPhp extends BlancoDbAbstractMethod {
    public CloseMethodPhp(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod("close",
                "利用されていたデータベースリソースを開放します。");
        fCgClass.getMethodList().add(cgMethod);

        cgMethod.getLangDoc().getDescriptionList().add(
                "クラスの利用が終わった際に必ずこのメソッドを呼び出してください。");

        final List listLine = cgMethod.getLineList();

        listLine.add("$this->fCurrentRow = NULL;");
        listLine.add("");
        listLine.add("if ($this->fStatement != NULL) {");
        listLine.add("$this->fStatement->closeCursor();");
        listLine.add("$this->fStatement = NULL;");
        listLine.add("}");
    }
}