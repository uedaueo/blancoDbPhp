/*
 * blanco Framework
 * Copyright (C) 2004-2006 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db.expander.query.field;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgField;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.db.common.expander.BlancoDbAbstractField;
import blanco.db.common.resourcebundle.BlancoDbCommonResourceBundle;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;

/**
 * QueryクラスのfConnectionフィールドです。
 * 
 * @author IGA Tosiki
 */
public class AlternativeConnectionFlgFieldPhp extends BlancoDbAbstractField {

    private final BlancoDbCommonResourceBundle fbundle = new BlancoDbCommonResourceBundle();

    /**
     * QueryクラスのfConnectionフィールドのコンストラクタです。
     *
     * @author IGA Tosiki
     */
    public AlternativeConnectionFlgFieldPhp(final BlancoDbSetting argDbSetting,
                                            final BlancoDbSqlInfoStructure argSqlInfo,
                                            final BlancoCgObjectFactory argCgFactory,
                                            final BlancoCgSourceFile argCgSourceFile,
                                            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        /*
         * 本当は blancoCg で pdo. を外したい
         */
        final BlancoCgField cgField = fCgFactory.createField("fAlternativeConnectionFlg",
                "boolean", "接続先が複数の場合に利用されます。");
        fCgClass.getFieldList().add(cgField);

        /*
         * ジェネレーションギャップデザインパターンが利用可能になる目的で、スコープはprotectedとします。
         */
        cgField.setAccess("protected");

        /*
         * デフォルト値を挿入します。
         * 接続方式が独自実装(ORIGINAL) かつ 接続先が複数(ALTERNATIVE)の場合にtrue
         */
        if(fSqlInfo.getConnectionMethod().equalsIgnoreCase(fbundle.getConnectionmethodOriginal())
                && fSqlInfo.getConnectTo().equalsIgnoreCase(fbundle.getConnecttoAlternative())){
            cgField.setDefault("true");
        }else{
            cgField.setDefault("false");
        }

    }
}
