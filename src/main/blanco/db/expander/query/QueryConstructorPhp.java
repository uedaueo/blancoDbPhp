/*
 * blanco Framework
 * Copyright (C) 2004-2006 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db.expander.query;

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
public class QueryConstructorPhp extends BlancoDbAbstractMethod {
    public QueryConstructorPhp(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod(fCgClass
                .getName(), fCgClass.getName() + "クラスのコンストラクタです。");
        fCgClass.getMethodList().add(cgMethod);

        cgMethod.getParameterList()
                .add(
                        fCgFactory.createParameter("connection", "pdo.PDO",
                                "データベース接続"));

        cgMethod.getLangDoc().getDescriptionList().add(
                "※注意：クラスを利用後、最後に必ずclose()メソッドを呼び出す必要があります。");

        cgMethod.setConstructor(true);

        final List listLine = cgMethod.getLineList();

        listLine.add("$this->fConnection = $connection;");
    }
}