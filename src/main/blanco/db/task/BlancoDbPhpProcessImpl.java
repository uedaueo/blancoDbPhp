/*
 * blanco Framework
 * Copyright (C) 2004-2009 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db.task;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import blanco.commons.util.BlancoStringUtil;
import blanco.db.BlancoDbConstantsPhp;
import blanco.db.BlancoDbXml2PhpClass;
import blanco.db.common.BlancoDbMeta2Xml;
import blanco.db.common.BlancoDbTableMeta2Xml;
import blanco.db.common.stringgroup.BlancoDbExecuteSqlStringGroup;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.resourcebundle.BlancoDbPhpResourceBundle;
import blanco.db.task.valueobject.BlancoDbPhpProcessInput;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure;

public class BlancoDbPhpProcessImpl implements BlancoDbPhpProcess {
    /**
     * リソースバンドルアクセサオブジェクトを記憶します。
     */
    private final BlancoDbPhpResourceBundle fBundle = new BlancoDbPhpResourceBundle();

    /**
     * {@inheritDoc}
     */
    public int execute(final BlancoDbPhpProcessInput input) throws IOException,
            IllegalArgumentException {
        System.out.println("- " + BlancoDbConstantsPhp.PRODUCT_NAME + " ("
                + BlancoDbConstantsPhp.VERSION + ")");

        try {
            System.out.println("db: begin.");
            final long startMills = System.currentTimeMillis();

            final File blancoTmpDbTableDirectory = new File(input.getTmpdir()
                    + BlancoDbConstantsPhp.TARGET_SUBDIRECTORY + "/table");
            final File blancoTmpDbSqlDirectory = new File(input.getTmpdir()
                    + BlancoDbConstantsPhp.TARGET_SUBDIRECTORY + "/sql");
            blancoTmpDbTableDirectory.mkdirs();
            blancoTmpDbSqlDirectory.mkdirs();

            final BlancoDbSetting dbSetting = new BlancoDbSetting();
            dbSetting.setTargetDir(input.getTargetdir());
            dbSetting.setBasePackage(input.getBasepackage());
            dbSetting.setRuntimePackage(input.getRuntimepackage());

            dbSetting.setJdbcdriver(input.getJdbcdriver());
            dbSetting.setJdbcurl(input.getJdbcurl());
            dbSetting.setJdbcuser(input.getJdbcuser());
            dbSetting.setJdbcpassword(input.getJdbcpassword());
            if (BlancoStringUtil.null2Blank(input.getJdbcdriverfile()).length() > 0) {
                dbSetting.setJdbcdriverfile(input.getJdbcdriverfile());
            }
            dbSetting.setEncoding(input.getEncoding());

            // ステートメントタイムアウト値はPHP版にはありません。

            dbSetting.setExecuteSql(new BlancoDbExecuteSqlStringGroup()
                    .convertToInt(input.getExecutesql()));
            if (dbSetting.getExecuteSql() == BlancoDbExecuteSqlStringGroup.NOT_DEFINED) {
                throw new IllegalArgumentException("executesqlとして不正な値("
                        + input.getExecutesql() + ")が与えられました。");
            }

            if (input.getSchema() != null) {
                // スキーマ名を指定。
                dbSetting.setSchema(input.getSchema());
            }

            if (input.getTable() == null || input.getTable().equals("true")) {
                // 単一表アクセサを自動生成
                final BlancoDbTableMeta2Xml tableMeta2Xml = new BlancoDbTableMeta2Xml() {
                    public boolean progress(int progressCurrent,
                            int progressTotal, String progressItem) {
                        // 常にtrueを返します。
                        return true;
                    }

                    protected boolean isSkipTypeForSimpleTable(
                            final BlancoDbMetaDataColumnStructure columnStructure) {
                        // PHP版ではバイナリ型やlongvarcharを通常の表アクセスと同時に扱うことができると仮定して実装されています。
                        return false;
                    }
                };
                tableMeta2Xml.setFormatSql(true);
                tableMeta2Xml.process(dbSetting, blancoTmpDbTableDirectory);

                // XMLファイルを元にR/Oマッピングを自動生成
                final BlancoDbXml2PhpClass generator = new BlancoDbXml2PhpClass() {
                    public boolean progress(int progressCurrent,
                            int progressTotal, String progressItem) {
                        // 常にtrueを返します。
                        return true;
                    }
                };
                if (input.getLog().equals("true")) {
                    dbSetting.setLogging(true);
                }
                generator.process(dbSetting, blancoTmpDbTableDirectory);
            }

            if (input.getSql() == null || input.getSql().equals("true")) {
                final File fileMetadir = new File(input.getMetadir());
                if (fileMetadir.exists() == false) {
                    throw new IllegalArgumentException(fBundle
                            .getAnttaskErr001(input.getMetadir()));
                }

                final BlancoDbMeta2Xml meta2Xml = new BlancoDbMeta2Xml();
                meta2Xml.setCacheMeta2Xml(input.getCache().equals("true"));
                meta2Xml.processDirectory(fileMetadir, blancoTmpDbSqlDirectory
                        .getAbsolutePath());

                // XMLファイルを元にR/Oマッピングを自動生成
                final BlancoDbXml2PhpClass generator = new BlancoDbXml2PhpClass() {
                    public boolean progress(int progressCurrent,
                            int progressTotal, String progressItem) {
                        // 常にtrueを返します。
                        return true;
                    }
                };
                if (input.getLog().equals("true")) {
                    dbSetting.setLogging(true);
                }
                generator.process(dbSetting, blancoTmpDbSqlDirectory);
            }

            final long endMills = System.currentTimeMillis() - startMills;
            System.out.println("db: end: " + (endMills / 1000) + " sec.");
        } catch (SQLException e) {
            throw new IllegalArgumentException(fBundle.getTaskErr001()
                    + e.toString());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(fBundle.getTaskErr002()
                    + e.toString());
        } catch (SAXException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(fBundle.getTaskErr003()
                    + e.toString());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(fBundle.getTaskErr004()
                    + e.toString());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(fBundle.getTaskErr005()
                    + e.toString());
        } catch (TransformerException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(fBundle.getTaskErr006()
                    + e.toString());
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public boolean progress(final String argProgressMessage) {
        System.out.println(argProgressMessage);
        return false;
    }
}
