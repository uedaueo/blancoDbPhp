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
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;

/**
 * QueryクラスのfCurrentRowフィールドです。
 * 
 * @author IGA Tosiki
 */
public class CurrentRowFieldPhp extends BlancoDbAbstractField {
    /**
     * QueryクラスのfCurrentRowフィールドのコンストラクタです。
     * 
     * @author IGA Tosiki
     */
    public CurrentRowFieldPhp(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgField cgField = fCgFactory.createField("fCurrentRow",
                "array", "現在フェッチしている行の内容。");
        fCgClass.getFieldList().add(cgField);

        /*
         * ジェネレーションギャップデザインパターンが利用可能になる目的で、スコープはprotectedとします。
         */
        cgField.setAccess("protected");
    }
}
