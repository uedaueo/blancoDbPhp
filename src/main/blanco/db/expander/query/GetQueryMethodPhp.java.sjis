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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgMethod;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.commons.util.BlancoJavaSourceUtil;
import blanco.commons.util.BlancoStringUtil;
import blanco.db.common.expander.BlancoDbAbstractMethod;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;

/**
 * 個別のメソッドを展開するためのクラス。
 */
public class GetQueryMethodPhp extends BlancoDbAbstractMethod {
    public GetQueryMethodPhp(final BlancoDbSetting argDbSetting,
            final BlancoDbSqlInfoStructure argSqlInfo,
            final BlancoCgObjectFactory argCgFactory,
            final BlancoCgSourceFile argCgSourceFile,
            final BlancoCgClass argCgClass) {
        super(argDbSetting, argSqlInfo, argCgFactory, argCgSourceFile,
                argCgClass);
    }

    public void expand() {
        final BlancoCgMethod cgMethod = fCgFactory.createMethod("getQuery",
                "クエリの取得メソッド");
        fCgClass.getMethodList().add(cgMethod);

        cgMethod.setReturn(fCgFactory.createReturn("string", "実際に実行されるSQL文"));

        cgMethod.getLangDoc().getDescriptionList().add("実際に実行するSQL文を戻します。");

        final List listLine = cgMethod.getLineList();

        final String escapedQuery = BlancoJavaSourceUtil
                .escapeStringAsJavaSource(fSqlInfo.getQuery());

        // クエリの #パラメータの:への変換
        final String actualSql = getNaturalSqlStringForPhp(escapedQuery);

        // SQL文を改行の文字列で分断します。
        final String[] sqlLines = splitString(actualSql, "\\n");

        for (int index = 0; index < sqlLines.length; index++) {
            String line = sqlLines[index];
            if (line.endsWith("\\n")) {
                line = line.substring(0, line.length() - 2) + " ";
            }

            if (index == 0) {
                listLine.add("$buf = '" + line + "';");
            } else {
                listLine.add("$buf = $buf . '" + line + "';");
            }
        }

        listLine.add("return $buf;");
    }

    /**
     * 与えられた文字列を指定の文字列を持って分割します。
     * 
     * このAPIでは、入力文字列から消えて失われる文字はありません。<br>
     * 共通関数化の候補API
     * 
     * @param originalString
     * @param delimiterString
     * @return
     */
    public static String[] splitString(final String originalString,
            final String delimiterString) {
        final ArrayList result = new ArrayList();
        String nextString = originalString;

        for (;;) {
            if (nextString.length() == 0) {
                break;
            }
            final int find = nextString.indexOf(delimiterString);
            if (find >= 0
                    && find + delimiterString.length() <= nextString.length()) {
                final String item = nextString.substring(0, find
                        + delimiterString.length());
                result.add(item);
                nextString = nextString.substring(find
                        + delimiterString.length());
            } else {
                result.add(nextString);
                break;
            }
        }

        final String[] resultStringArray = new String[result.size()];
        result.toArray(resultStringArray);
        return resultStringArray;
    }

    /**
     * Iteratorに実際に張り込まれるナチュラルなSQL文
     * 
     * PDOでは 「:置換名称」となります。
     * 
     * @return
     */
    public static String getNaturalSqlStringForPhp(
            final String originalSqlQueryString) {
        final String SZ_PARAMETER_FOR_SQL_INPUT_PARAMETER = "#[a-zA-Z0-9.\\-_\\P{InBasicLatin}]*\\b|#.*$";

        final Matcher matcher = Pattern.compile(
                SZ_PARAMETER_FOR_SQL_INPUT_PARAMETER).matcher(
                originalSqlQueryString);
        String resultSql = originalSqlQueryString;
        for (int index = 1; matcher.find(); index++) {
            String name = matcher.group();
            // 先頭の#を除去したうえで処理を行います。
            name = name.substring(1, name.length());
            resultSql = BlancoStringUtil.replaceAll(resultSql, "#" + name, ":"
                    + name);
        }
        return resultSql;
    }
}