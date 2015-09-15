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
 * 
 * @author tosiki iga
 */
public class PrepareStatementMethod2Php extends BlancoDbAbstractMethod {
    public PrepareStatementMethod2Php(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod(
                "prepareStatementSql", "クエリのプリコンパイルに相当します。");
        fCgClass.getMethodList().add(cgMethod);

        cgMethod.getParameterList()
                .add(
                        fCgFactory.createParameter("query", "string",
                                "利用したいクエリ(SQL文)"));

        final List listDesc = cgMethod.getLangDoc().getDescriptionList();

        listDesc.add("動的に内容が変化するようなSQLを実行する必要がある場合にのみ、こちらのメソッドを利用します。");

        listDesc
                .add("※SQL文そのものをパラメータとして与えることができて自由度が高い一方、SQLインジェクションと呼ばれるセキュリティホールが発生する危険がある点に注意した上で利用してください。");
        listDesc.add("内部的には SQL文をフィールド変数に記憶します。");

        final List listLine = cgMethod.getLineList();

        listLine.add("$this->close();");
        listLine
                .add("$this->fStatement = $this->fConnection->prepare($query);");
    }
}