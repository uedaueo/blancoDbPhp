/*
 * blanco Framework
 * Copyright (C) 2004-2006 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db.expander.exception;

import java.util.List;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgField;
import blanco.cg.valueobject.BlancoCgMethod;
import blanco.cg.valueobject.BlancoCgSourceFile;

/**
 * データベースの処理の結果、1行もデータが変更されなかったことを示す例外クラス
 * 
 * このクラスが生成するクラスはblancoDbが生成したソースコードで利用されます
 * 
 * @author IGA Tosiki
 */
public class NoRowModifiedExceptionClassPhp {
    /**
     * 例外クラスのクラス名
     */
    public static final String CLASS_NAME = "NoRowModifiedException";

    /**
     * コンストラクタに示すJavaDoc説明。
     */
    private static final String CONSTRUCTOR_JAVADOC = "データベースの処理の結果、1行もデータが変更されなかったことを示す例外クラスのインスタンスを生成します。";

    /**
     * blancoCg オブジェクトファクトリ。
     */
    private BlancoCgObjectFactory fCgFactory = null;

    /**
     * このクラスが含まれるソースコード。
     */
    private BlancoCgSourceFile fCgSourceFile = null;

    private String fPackage = null;

    public NoRowModifiedExceptionClassPhp(
            final BlancoCgObjectFactory cgFactory, final String argPackage) {
        fCgFactory = cgFactory;
        fCgSourceFile = fCgFactory.createSourceFile(argPackage,
                "This code is generated by blanco Framework.");

        fPackage = argPackage;
    }

    public BlancoCgSourceFile expand() {
        final BlancoCgClass cgClass = fCgFactory.createClass(CLASS_NAME, null);
        fCgSourceFile.getClassList().add(cgClass);

        cgClass.getExtendClassList().add(
                fCgFactory.createType(fPackage + ".NotSingleRowException"));

        {
            final List listDesc = cgClass.getLangDoc().getDescriptionList();

            listDesc.add("データベースの処理の結果、1行もデータが変更されなかったことを示す例外クラス");
            listDesc.add("このクラスはblancoDbが生成したソースコードで利用されます");
            listDesc.add("※このクラスは、ソースコード自動生成後のファイルとして利用されます。");
            listDesc.add("");
            listDesc.add("@author blanco Framework");
        }

        {
            final BlancoCgField cgField = fCgFactory.createField(
                    "SQLSTATE_NOROWMODIFIED", "string", null);
            cgClass.getFieldList().add(cgField);
            cgField.setAccess("protected");
            cgField.setStatic(true);
            cgField.setFinal(true);
            cgField.setDefault("\"00102\"");
            cgField.getLangDoc().getDescriptionList().add(
                    "このクラスを表現するSQLStateコード。");
            cgField
                    .getLangDoc()
                    .getDescriptionList()
                    .add(
                            "※このクラスを利用する際には、SQLStateには頼らずに例外クラスの型によって状態を判断するようにしてください。");
        }

        {
            final BlancoCgMethod cgMethod = fCgFactory.createMethod(CLASS_NAME,
                    CONSTRUCTOR_JAVADOC);
            cgClass.getMethodList().add(cgMethod);

            cgMethod.setConstructor(true);
            cgMethod.getParameterList().add(
                    fCgFactory.createParameter("reason", "string", "例外の説明"));

            cgMethod
                    .setSuperclassInvocation("parent::__construct($reason . self::SQLSTATE_NOROWMODIFIED)");
        }

        // required 文を出力しない ... 将来的には xls で指定するように？
        fCgSourceFile.setIsImport(false);

        return fCgSourceFile;
    }
}